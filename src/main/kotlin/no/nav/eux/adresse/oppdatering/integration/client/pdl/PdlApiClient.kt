package no.nav.eux.adresse.oppdatering.integration.client.pdl

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.adresse.oppdatering.integration.client.pdl.model.PdlPerson
import no.nav.eux.adresse.oppdatering.integration.config.GraphqlSpecs
import org.springframework.graphql.client.FieldAccessException
import org.springframework.graphql.client.HttpSyncGraphQlClient
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component

@Component
class PdlApiClient(
    val pdlHttpSyncGraphQlClient: HttpSyncGraphQlClient,
    val graphqlSpecs: GraphqlSpecs,
) {

    val log = logger {}


    @Retryable(
        maxAttempts = 9,
        backoff = Backoff(delay = 1000, multiplier = 2.0),
        noRetryFor = [
            FieldAccessException::class,
            UgyldigIdentException::class
        ]
    )
    fun hentAdresser(personId: String, buc: String): PdlPerson = callWithPdlErrorHandling {
        val query = graphqlSpecs.read("pdl-adresser-query.graphql")
        pdlHttpSyncGraphQlClient
            .mutate()
            .header("Behandlingsnummer", buc.behandlingsnummer)
            .build()
            .document(query)
            .variable("ident", personId)
            .retrieveSync("hentPerson")
            .toEntity(PdlPerson::class.java)
    }

    private val String.behandlingsnummer
        get() =
            when (split("_").first()) {
                "H" -> "B139,B271,B284,B286,B287,B299"
                "UB" -> "B286"
                "FB" -> "B284"
                "S" -> "B139"
                else -> ""
            }
}

fun <T> callWithPdlErrorHandling(pdlCall: () -> T?): T =
    try {
        pdlCall() ?: throw RuntimeException("Fant ikke entitet")
    } catch (e: FieldAccessException) {
        val id = e.response.errors.first().extensions["id"]
        if (id == "ugyldig_ident")
            throw UgyldigIdentException()
        else
            throw e
    }

class UgyldigIdentException : RuntimeException()
