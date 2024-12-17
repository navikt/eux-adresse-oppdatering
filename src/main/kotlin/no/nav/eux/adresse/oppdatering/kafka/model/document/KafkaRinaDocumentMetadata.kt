package no.nav.eux.adresse.oppdatering.kafka.model.document

import java.time.OffsetDateTime

data class KafkaRinaDocumentMetadata(
    val id: String,
    val type: String,
    val caseId: Long,
    val versions: List<KafkaRinaDocumentVersions>,
    val creationDate: OffsetDateTime,
)
