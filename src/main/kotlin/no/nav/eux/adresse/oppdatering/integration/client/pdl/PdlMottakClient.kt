package no.nav.eux.adresse.oppdatering.integration.client.pdl

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.adresse.oppdatering.integration.client.pdl.model.PdlUtenlandskAdresse
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class PdlMottakClient(
    val pdlMottakRestClient: RestClient
) {

    val log = logger {}

    infix fun endringsmeld(pdlUtenlandskAdresse: PdlUtenlandskAdresse) {
        log.info { "sender til pdl: $pdlUtenlandskAdresse" }
        pdlMottakRestClient
            .post()
            .uri("/api/v1/endringer")
            .contentType(APPLICATION_JSON)
            .body(pdlUtenlandskAdresse)
            .retrieve()
            .toBodilessEntity()
    }

}
