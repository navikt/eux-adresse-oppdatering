package no.nav.eux.adresse.oppdatering.integration.client.eux.rina.api

import no.nav.eux.adresse.oppdatering.integration.client.eux.rina.api.model.EuxRinaApiDokument
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Component
class EuxRinaApiClient(
    val euxRinaApiRestClient: RestClient
) {

    fun dokument(rinasakId: Long, sedId: String) = euxRinaApiRestClient
        .get()
        .uri("/cpi/buc/$rinasakId/sed/$sedId")
        .accept(APPLICATION_JSON)
        .retrieve()
        .body<EuxRinaApiDokument>()

}
