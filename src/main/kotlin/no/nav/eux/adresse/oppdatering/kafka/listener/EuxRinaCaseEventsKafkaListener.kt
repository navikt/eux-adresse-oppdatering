package no.nav.eux.adresse.oppdatering.kafka.listener

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.adresse.oppdatering.kafka.model.document.KafkaRinaDocument
import no.nav.eux.adresse.oppdatering.service.AdresseService
import no.nav.eux.logging.clearLocalMdc
import no.nav.eux.logging.mdc
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
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
        consumerRecord: ConsumerRecord<String, KafkaRinaDocument>,
        acknowledgment: Acknowledgment
    ) {
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
        log.info { "Mottok dokument fra Kafka av type $documentEventType" }
        if (bucTilBehandling(bucType))
            adresseService.oppdaterPdl(kafkaRinaDocument)
        else
            log.info { "BUC type behandles ikke" }
        acknowledgment.acknowledge()
        log.info { "Adresseoppdatering for dokument ferdigstillt" }
        clearLocalMdc()
    }

    fun bucTilBehandling(bucType: String): Boolean =
        listOf(
            "H_BUC_01"
        )
            .contains(bucType)

}
