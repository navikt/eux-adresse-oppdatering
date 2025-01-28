package no.nav.eux.adresse.oppdatering.kafka.listener

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.adresse.oppdatering.kafka.model.document.KafkaRinaDocument
import no.nav.eux.adresse.oppdatering.service.AdresseService
import no.nav.eux.logging.clearLocalMdc
import no.nav.eux.logging.mdc
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class EuxRinaCaseEventsKafkaListener(
    val adresseService: AdresseService
) {

    val log = logger {}

    @KafkaListener(
        id = "eux-adresse-oppdatering-document",
        topics = ["\${kafka.topics.eux-rina-document-events-v1}"],
        containerFactory = "rinaDocumentKafkaListenerContainerFactory"
    )
    fun document(
        consumerRecord: ConsumerRecord<String, KafkaRinaDocument>
    ) {
        try {
            val kafkaRinaDocument = consumerRecord.value()
            val documentMetadata = kafkaRinaDocument.payLoad.documentMetadata
            val documentEventType = kafkaRinaDocument.documentEventType
            val caseId = documentMetadata.caseId
            val bucType = kafkaRinaDocument.buc
            mdc(
                rinasakId = caseId.toInt(),
                bucType = bucType,
                sedType = documentMetadata.type
            )
            if (documentMetadata.direction != "IN") {
                log.info { "Dokument har ikke direction 'IN', avslutter behandling" }
            } else if (bucTilBehandling(bucType)) {
                log.info { "Mottok dokument fra Kafka av type $documentEventType" }
                adresseService.oppdaterPdl(kafkaRinaDocument)
                log.info { "Adresseoppdatering for dokument ferdigstillt" }
            } else {
                log.info { "Dokument behandles ikke ($documentEventType)" }
            }
        } catch (e: Exception) {
            log.error(e) { "Feil ved behandling av dokument" }
        } finally {
            clearLocalMdc()
        }
    }

    fun bucTilBehandling(bucType: String) =
        if (bucType == "UB_BUC_04")
            false
        else
            when (bucType.split("_").first()) {
                "H", "UB", "FB", "S" -> true
                else -> false
            }

}
