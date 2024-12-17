package no.nav.eux.adresse.oppdatering.kafka.listener

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class EuxRinaCaseEventsKafkaStringListener() {

    val log = logger {}

    @KafkaListener(
        id = "eux-adresse-oppdatering-string",
        topics = ["\${kafka.topics.eux-rina-document-events-v1}"],
        containerFactory = "rinaDocumentKafkaListenerContainerFactoryString"
    )
    fun document(consumerRecord: ConsumerRecord<String, String>) {
        println("document event: ")
        println()
        println(consumerRecord.value())
        println()
    }
}
