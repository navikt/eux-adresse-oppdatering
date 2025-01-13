package no.nav.eux.adresse.oppdatering.dataset

import no.nav.eux.adresse.oppdatering.kafka.model.document.KafkaRinaDocument
import java.time.OffsetDateTime

val case5_kafkaRinaDocumentVersions = KafkaRinaDocument.Version(
    id = 5
)

val case5_kafkaRinaDocumentCreator = KafkaRinaDocument.Creator(
    organisation = KafkaRinaDocument.Creator.Organisation(
        name = "NAV"
    )
)

val case5_kafkaRinaDocumentMetadata = KafkaRinaDocument.Metadata(
    id = "5",
    type = "H001",
    caseId = 5,
    versions = listOf(case5_kafkaRinaDocumentVersions),
    creationDate = OffsetDateTime.now(),
    creator = case5_kafkaRinaDocumentCreator,
    status = "received",
    direction = "IN",
)

val case5_kafkaRinaDocumentPayload = KafkaRinaDocument.Payload(
    documentMetadata = case5_kafkaRinaDocumentMetadata
)

val case5_H001 = KafkaRinaDocument(
    documentEventType = "RECEIVE_DOCUMENT",
    buc = "H_BUC_01",
    payLoad = case5_kafkaRinaDocumentPayload
)