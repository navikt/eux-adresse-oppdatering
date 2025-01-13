package no.nav.eux.adresse.oppdatering.dataset

import no.nav.eux.adresse.oppdatering.kafka.model.document.KafkaRinaDocument
import java.time.OffsetDateTime

val case4_kafkaRinaDocumentVersions = KafkaRinaDocument.Version(
    id = 4
)

val case4_kafkaRinaDocumentCreator = KafkaRinaDocument.Creator(
    organisation = KafkaRinaDocument.Creator.Organisation(
        name = "NAV"
    )
)

val case4_kafkaRinaDocumentMetadata = KafkaRinaDocument.Metadata(
    id = "4",
    type = "H001",
    caseId = 4,
    versions = listOf(case4_kafkaRinaDocumentVersions),
    creationDate = OffsetDateTime.now(),
    creator = case4_kafkaRinaDocumentCreator,
    status = "received",
    direction = "IN",
)

val case4_kafkaRinaDocumentPayload = KafkaRinaDocument.Payload(
    documentMetadata = case4_kafkaRinaDocumentMetadata
)

val case4_H001 = KafkaRinaDocument(
    documentEventType = "RECEIVE_DOCUMENT",
    buc = "H_BUC_01",
    payLoad = case4_kafkaRinaDocumentPayload
)