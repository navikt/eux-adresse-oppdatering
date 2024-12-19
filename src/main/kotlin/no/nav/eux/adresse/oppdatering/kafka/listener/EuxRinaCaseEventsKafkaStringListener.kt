package no.nav.eux.adresse.oppdatering.kafka.listener

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class EuxRinaCaseEventsKafkaStringListener {

    @KafkaListener(
        id = "eux-adresse-oppdatering-string",
        topics = ["\${kafka.topics.eux-rina-document-events-v1}"],
        containerFactory = "rinaDocumentKafkaListenerContainerFactoryString"
    )
    fun document(consumerRecord: ConsumerRecord<String, String>) {
        println(consumerRecord.value())
    }
}
