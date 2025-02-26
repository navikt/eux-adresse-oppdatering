package no.nav.eux.adresse.oppdatering

import no.nav.eux.adresse.oppdatering.common.kafkaTopicRinaDocumentEvents
import no.nav.eux.adresse.oppdatering.dataset.*
import org.awaitility.kotlin.await
import org.awaitility.kotlin.until
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class AdresseOppdateringTest : AbstractTest() {

    @Test
    fun adresseOppdatering() {
        kafkaTopicRinaDocumentEvents send case1_H001
        await until { requestBodies["/api/v1/endringer"]?.size == 3 }
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
        kafkaTopicRinaDocumentEvents send case4_H001
        await until { requestBodies["/api/v1/endringer"]?.size == 7 }
        "/api/v1/endringer" requestNumber 6 shouldEqual "/dataset/forventet/endringer-kontaktadresse-postboks.json"
        kafkaTopicRinaDocumentEvents send case5_H001
        await until { requestBodies["/api/v1/endringer"]?.size == 8 }
        "/api/v1/endringer" requestNumber 7 shouldEqual "/dataset/forventet/endringer-kontaktadresse-postboks.json"
        kafkaTopicRinaDocumentEvents send case6_F001
        await until { requestBodies["/api/v1/endringer"]?.size == 10 }
        "/api/v1/endringer" requestNumber 8 shouldEqual "/dataset/forventet/endringer-f001-ektefelle.json"
        "/api/v1/endringer" requestNumber 9 shouldEqual "/dataset/forventet/endringer-f001-annenperson.json"
        kafkaTopicRinaDocumentEvents send case7_H055
        await until { requestBodies["/api/v1/endringer"]?.size == 11 }
        "/api/v1/endringer" requestNumber 10 shouldEqual "/dataset/forventet/endringer-bostedsadresse.json"
    }

    @AfterEach
    fun printResultat() {
        skrivUtEksterneKall()
    }

}
