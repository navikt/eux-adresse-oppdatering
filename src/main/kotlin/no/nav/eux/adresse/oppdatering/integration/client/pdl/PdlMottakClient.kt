package no.nav.eux.adresse.oppdatering.integration.client.pdl

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.adresse.oppdatering.integration.client.pdl.model.PdlEndringsstatus
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import java.lang.Thread.sleep

@Component
class PdlMottakClient(
    val pdlMottakRestClient: RestClient
) {

    val log = logger {}

    infix fun endringsmeld(adresse: Any) {
        log.info { "sender til pdl: $adresse" }
        val entity = pdlMottakRestClient
            .post()
            .uri("/api/v1/endringer")
            .contentType(APPLICATION_JSON)
            .body(adresse)
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
        maxAttempts: Int = 60
    ) {
        val entity = pdlMottakRestClient
            .get()
            .uri(location)
            .retrieve()
            .body<List<PdlEndringsstatus>>()
            ?.firstOrNull()
        if (entity != null) {
            when (val status = entity.status.statusType) {
                "PENDING" -> verifiser(attempt, maxAttempts, status, location)
                "ERROR" -> log.error { "Feil ved verifisering: $entity" }
                "DONE" -> log.info { "Endringsmelding verifisert (forsøk: $attempt)" }
                else -> log.error { "Ukjent status: $status" }
            }
            log.info { "Status på verifisering: ${entity.status.statusType}" }
        } else {
            log.error { "Kunne ikke hente status på verifisering" }
        }
    }

    private fun verifiser(
        attempt: Int,
        maxAttempts: Int,
        status: String,
        location: String
    ) {
        if (attempt < maxAttempts) {
            if (attempt % 5 == 1) log.info { "Status på verifisering: $status, forsøker igjen om 1 sekund" }
            sleep(1000)
            verifiser(location, attempt + 1)
        } else {
            log.error { "Kunne ikke verifisere endringsmelding etter $maxAttempts forsøk" }
        }
    }
}
