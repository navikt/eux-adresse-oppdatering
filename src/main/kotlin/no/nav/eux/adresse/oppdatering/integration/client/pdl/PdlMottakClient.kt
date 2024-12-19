package no.nav.eux.adresse.oppdatering.integration.client.pdl

import no.nav.eux.adresse.oppdatering.integration.client.pdl.model.OpprettPdlKontaktadresse
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class PdlMottakClient(
    val pdlMottakRestClient: RestClient
) {

    fun endringsmelding(opprettPdlKontaktadresse: OpprettPdlKontaktadresse) {
        println("sender til pdl:")
        println(opprettPdlKontaktadresse)
        println()
        pdlMottakRestClient
            .post()
            .uri("/api/v1/endringer")
            .contentType(APPLICATION_JSON)
            .body(opprettPdlKontaktadresse)
            .retrieve()
            .toBodilessEntity()
    }

}
