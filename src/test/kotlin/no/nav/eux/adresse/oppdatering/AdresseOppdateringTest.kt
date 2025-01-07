package no.nav.eux.adresse.oppdatering

import no.nav.eux.adresse.oppdatering.common.kafkaTopicRinaDocumentEvents
import no.nav.eux.adresse.oppdatering.dataset.case1H001
import org.awaitility.kotlin.await
import org.awaitility.kotlin.until
import org.junit.jupiter.api.Test

class AdresseOppdateringTest : AbstractTest() {

    @Test
    fun adresseOppdatering() {
        kafkaTopicRinaDocumentEvents send case1H001
        await until { requestBodies["/api/v1/endringer"]?.size == 3 }
        skrivUtEksterneKall()
        "/api/v1/endringer" requestNumber 0 shouldEqual "/dataset/forventet/endringer-bostedsadresse.json"
        "/api/v1/endringer" requestNumber 1 shouldEqual "/dataset/forventet/endringer-oppholdsadresse.json"
        "/api/v1/endringer" requestNumber 2 shouldEqual "/dataset/forventet/endringer-kontaktadresse.json"
    }

}
