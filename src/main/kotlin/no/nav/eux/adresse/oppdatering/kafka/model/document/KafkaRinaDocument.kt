package no.nav.eux.adresse.oppdatering.kafka.model.document

data class KafkaRinaDocument(
    val documentEventType: String,
    val buc: String,
    val payLoad: KafkaRinaDocumentPayload
)
