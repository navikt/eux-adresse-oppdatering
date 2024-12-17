package no.nav.eux.adresse.oppdatering.service

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.adresse.oppdatering.integration.client.eux.rina.api.EuxRinaApiClient
import no.nav.eux.adresse.oppdatering.kafka.model.document.KafkaRinaDocument
import org.springframework.stereotype.Service

@Service
class AdresseService(
    val euxRinaApiClient: EuxRinaApiClient
) {

    val log = logger {}

    fun oppdaterPdl(kafkaRinaDocument: KafkaRinaDocument) {
        val dokument = euxRinaApiClient.dokument(
            rinasakId = kafkaRinaDocument.payLoad.documentMetadata.caseId,
            sedId = kafkaRinaDocument.payLoad.documentMetadata.id
        )
        log.info { "doc fra rina: $dokument" }
    }
}