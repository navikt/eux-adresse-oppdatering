package no.nav.eux.adresse.oppdatering.integration.client.pdl

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.adresse.oppdatering.integration.client.pdl.model.PdlPerson
import no.nav.eux.adresse.oppdatering.integration.config.GraphqlSpecs
import org.springframework.graphql.client.HttpSyncGraphQlClient
import org.springframework.stereotype.Component

@Component
class PdlApiClient(
    val pdlHttpSyncGraphQlClient: HttpSyncGraphQlClient,
    val graphqlSpecs: GraphqlSpecs,
) {

    val log = logger {}

    fun hentAdresser(personId: String, buc: String): String {
        val query = graphqlSpecs.read("pdl-adresser-query.graphql")
        val response = pdlHttpSyncGraphQlClient
            .mutate()
            .header("Behandlingsnummer", buc.behandlingsnummer)
            .build()
            .document(query)
            .variable("ident", personId)
            .retrieveSync("hentPerson")
            .toEntity(PdlPerson::class.java)
        log.info { "Response: $response" }
        return response?.toString() ?: "No data"
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
