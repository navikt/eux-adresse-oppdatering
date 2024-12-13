package no.nav.eux.adresse.oppdatering.kafka.model.document

import com.fasterxml.jackson.annotation.JsonProperty

data class KafkaRinaDocumentPayload(
    @JsonProperty("DOCUMENT_METADATA")
    val documentMetadata: KafkaRinaDocumentMetadata
)
