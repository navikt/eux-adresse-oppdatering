package no.nav.eux.adresse.oppdatering.integration.client.eux.rina.api

import no.nav.eux.adresse.oppdatering.integration.client.eux.rina.api.model.EuxRinaApiDokument
import no.nav.eux.adresse.oppdatering.integration.client.eux.rina.api.model.EuxRinaSakOversiktV3
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Component
class EuxRinaApiClient(
    val euxRinaApiRestClient: RestClient
) {

    @Retryable(maxAttempts = 9, backoff = Backoff(delay = 1000, multiplier = 2.0))
    fun dokument(rinasakId: Long, sedId: String) = euxRinaApiRestClient
        .get()
        .uri("/cpi/buc/$rinasakId/sed/$sedId?domene=nav")
        .accept(APPLICATION_JSON)
        .retrieve()
        .body<EuxRinaApiDokument>()
        ?: throw RuntimeException("Dokument ikke funnet")

    @Retryable(maxAttempts = 9, backoff = Backoff(delay = 1000, multiplier = 2.0))
    fun rinasak(rinasakId: Long) = euxRinaApiRestClient
        .get()
        .uri("/v3/buc/$rinasakId/oversikt?domene=nav")
        .accept(APPLICATION_JSON)
        .retrieve()
        .body<EuxRinaSakOversiktV3>()
        ?: throw RuntimeException("Rinasak ikke funnet")
}
