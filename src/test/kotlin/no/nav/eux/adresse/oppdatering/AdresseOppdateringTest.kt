package no.nav.eux.adresse.oppdatering

import no.nav.eux.adresse.oppdatering.common.kafkaTopicRinaDocumentEvents
import no.nav.eux.adresse.oppdatering.dataset.case1_H001
import no.nav.eux.adresse.oppdatering.dataset.case2_H005
import no.nav.eux.adresse.oppdatering.dataset.case3_H001
import org.awaitility.kotlin.await
import org.awaitility.kotlin.until
import org.junit.jupiter.api.Test

class AdresseOppdateringTest : AbstractTest() {

    @Test
    fun adresseOppdatering() {
        kafkaTopicRinaDocumentEvents send case1_H001
        await until { requestBodies["/api/v1/endringer"]?.size == 3 }
        skrivUtEksterneKall()
        "/api/v1/endringer" requestNumber 0 shouldEqual "/dataset/forventet/endringer-bostedsadresse.json"
        "/api/v1/endringer" requestNumber 1 shouldEqual "/dataset/forventet/endringer-oppholdsadresse.json"
        "/api/v1/endringer" requestNumber 2 shouldEqual "/dataset/forventet/endringer-kontaktadresse.json"
        kafkaTopicRinaDocumentEvents send case2_H005
        await until { requestBodies["/api/v1/endringer"]?.size == 4 }
        "/api/v1/endringer" requestNumber 3 shouldEqual "/dataset/forventet/endringer-fastslaabosted.json"
        kafkaTopicRinaDocumentEvents send case3_H001
        await until { requestBodies["/api/v1/endringer"]?.size == 6 }
        "/api/v1/endringer" requestNumber 4 shouldEqual "/dataset/forventet/endringer-oppholdsadresse-norge.json"
        "/api/v1/endringer" requestNumber 5 shouldEqual "/dataset/forventet/endringer-kontaktadresse-norge.json"
    }

}
