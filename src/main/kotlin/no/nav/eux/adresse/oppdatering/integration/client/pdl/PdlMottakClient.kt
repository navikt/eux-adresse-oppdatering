package no.nav.eux.adresse.oppdatering.integration.client.pdl

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.adresse.oppdatering.integration.client.pdl.model.PdlUtenlandskAdresse
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Component
class PdlMottakClient(
    val pdlMottakRestClient: RestClient
) {

    val log = logger {}

    infix fun endringsmeld(pdlUtenlandskAdresse: PdlUtenlandskAdresse) {
        log.info { "sender til pdl: $pdlUtenlandskAdresse" }
        val entity = pdlMottakRestClient
            .post()
            .uri("/api/v1/endringer")
            .contentType(APPLICATION_JSON)
            .body(pdlUtenlandskAdresse)
            .retrieve()
            .toBodilessEntity()
        log.info { "Headers: ${entity.headers}" }
        entity
            .headers["Location"]
            ?.firstOrNull()
            .verifiser()
    }

    private fun String?.verifiser() {
        if (this == null)
            log.error { "Location header mangler" }
        else
            verifiser(this)
    }

    private fun verifiser(
        location: String,
        attempt: Int = 1,
        maxAttempts: Int = 10
    ) {
        val entity = pdlMottakRestClient
            .get()
            .uri(location)
            .retrieve()
            .body<String>()
        log.info { "verifisert: $entity" }
    }
}
