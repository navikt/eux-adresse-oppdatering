package no.nav.eux.adresse.oppdatering

import no.nav.eux.adresse.oppdatering.common.kafkaTopicRinaDocumentEvents
import no.nav.eux.adresse.oppdatering.dataset.case1H001
import org.awaitility.kotlin.await
import org.awaitility.kotlin.until
import org.awaitility.kotlin.untilNotNull
import org.junit.jupiter.api.Test

class AdresseOppdateringTest : AbstractTest() {

    @Test
    fun test() {
        kafkaTopicRinaDocumentEvents send case1H001
        await untilNotNull {
            requestBodies["/api/v1/endringer"]
        }
        await until {
            requestBodies["/api/v1/endringer"]?.size == 3
        }
        println(requestBodies["/api/v1/endringer"]?.get(0))
        println(requestBodies["/api/v1/endringer"]?.get(1))
        println(requestBodies["/api/v1/endringer"]?.get(2))

    }
}
