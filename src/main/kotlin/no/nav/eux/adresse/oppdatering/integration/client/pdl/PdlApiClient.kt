package no.nav.eux.adresse.oppdatering.integration.client.pdl

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.adresse.oppdatering.integration.client.pdl.model.HentPerson
import no.nav.eux.adresse.oppdatering.integration.config.GraphqlSpecs
import org.springframework.graphql.client.HttpSyncGraphQlClient
import org.springframework.stereotype.Component


@Component
class PdlApiClient(
    val pdlHttpSyncGraphQlClient: HttpSyncGraphQlClient,
    val graphQLFileReader: GraphqlSpecs,
) {

    val log = logger {}

    fun hentAdresser(personId: String): String {
        val query = graphQLFileReader.read("pdl-adresser-query.graphql")
        val response = pdlHttpSyncGraphQlClient.document(query)
            .variable("ident", personId)
            .retrieveSync("hentPerson")
            .toEntity(HentPerson::class.java)
        log.info { "Response: $response" }
        return response?.toString() ?: "No data"
    }
}
