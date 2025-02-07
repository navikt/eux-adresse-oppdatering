package no.nav.eux.adresse.oppdatering.integration.client.pdl

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.adresse.oppdatering.integration.client.pdl.exception.PdlMottakConflictException
import no.nav.eux.adresse.oppdatering.integration.client.pdl.exception.PdlMottakUnprocessableEntityException
import no.nav.eux.adresse.oppdatering.integration.client.pdl.exception.PdlNotFoundException
import no.nav.eux.adresse.oppdatering.integration.client.pdl.model.PdlEndringsstatus
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import java.lang.Thread.sleep

@Component
class PdlMottakClient(
    val pdlMottakRestClient: RestClient
) {

    val log = logger {}

    @Retryable(
        maxAttempts = 9,
        backoff = Backoff(delay = 1000, multiplier = 2.0),
        retryFor = [
            HttpServerErrorException::class,
            PdlNotFoundException::class
        ]
    )
    infix fun endringsmeld(adresse: Any) {
        val entity = callWithErrorHandling {
            pdlMottakRestClient
                .post()
                .uri("/api/v1/endringer")
                .contentType(APPLICATION_JSON)
                .body(adresse)
                .retrieve()
                .toBodilessEntity()
        }
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
                "PENDING" -> verifiser(attempt, maxAttempts, location)
                "ERROR" -> log.error { "Feil ved verifisering: $entity" }
                "DONE" -> log.info { "Endringsmelding verifisert (forsøk: $attempt)" }
                else -> log.error { "Ukjent status: $status" }
            }
            if (attempt % 10 == 5)
                log.info { "Status på verifisering: ${entity.status.statusType}, forsøk: $attempt" }
        } else {
            log.error { "Kunne ikke hente status på verifisering" }
        }
    }

    private fun verifiser(
        attempt: Int,
        maxAttempts: Int,
        location: String
    ) {
        if (attempt < maxAttempts) {
            sleep(1000)
            verifiser(location, attempt + 1)
        } else {
            log.error { "Kunne ikke verifisere endringsmelding etter $maxAttempts forsøk" }
        }
    }

    fun <T> callWithErrorHandling(pdlCall: () -> T): T =
        try {
            pdlCall()
        } catch (e: HttpClientErrorException) {
            when (e.statusCode.value()) {
                404 -> throw PdlNotFoundException(e)
                409 -> throw PdlMottakConflictException(e)
                422 -> throw PdlMottakUnprocessableEntityException(e)
                else -> throw e
            }
        }
}
