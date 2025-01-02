package no.nav.eux.adresse.oppdatering

import io.kotest.assertions.json.shouldMatchJsonResource
import no.nav.eux.adresse.oppdatering.common.kafkaTopicRinaDocumentEvents
import no.nav.eux.adresse.oppdatering.dataset.case1H001
import org.awaitility.kotlin.await
import org.awaitility.kotlin.until
import org.junit.jupiter.api.Test

class AdresseOppdateringTest : AbstractTest() {

    @Test
    fun adresseOppdatering() {
        kafkaTopicRinaDocumentEvents send case1H001
        await until {
            requestBodies["/api/v1/endringer"]?.size == 3
        }
        skrivUtEksterneKall()
        println("/api/v1/endringer" requestNumber 0)
        println("/api/v1/endringer" requestNumber 1)
        println("/api/v1/endringer" requestNumber 2)
        "/api/v1/endringer" requestNumber 0 shouldMatchJsonResource "/dataset/forventet/endringer-bostedsadresse.json"
        "/api/v1/endringer" requestNumber 1 shouldMatchJsonResource "/dataset/forventet/endringer-oppholdsadresse.json"
        "/api/v1/endringer" requestNumber 2 shouldMatchJsonResource "/dataset/forventet/endringer-kontaktadresse.json"
    }
}
