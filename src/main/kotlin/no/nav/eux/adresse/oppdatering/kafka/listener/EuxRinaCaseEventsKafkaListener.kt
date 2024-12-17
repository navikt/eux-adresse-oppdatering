package no.nav.eux.adresse.oppdatering.kafka.listener

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.adresse.oppdatering.kafka.model.document.KafkaRinaDocument
import no.nav.eux.logging.mdc
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class EuxRinaCaseEventsKafkaListener() {

    val log = logger {}

    @KafkaListener(
        id = "eux-avslutt-rinasaker-document",
        topics = ["\${kafka.topics.eux-rina-document-events-v1}"],
        containerFactory = "rinaDocumentKafkaListenerContainerFactory"
    )
    fun document(consumerRecord: ConsumerRecord<String, KafkaRinaDocument>) {
        val kafkaRinaDocument = consumerRecord.value()
        val documentMetadata = kafkaRinaDocument.payLoad.documentMetadata
        val documentEventType = kafkaRinaDocument.documentEventType
        val caseId = documentMetadata.caseId
        val bucType = kafkaRinaDocument.buc
        mdc(rinasakId = caseId, bucType = bucType)
        log.info { "Received document event of type $documentEventType for case $caseId" }
    }

}
