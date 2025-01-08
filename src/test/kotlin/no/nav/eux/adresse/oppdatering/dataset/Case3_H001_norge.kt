package no.nav.eux.adresse.oppdatering.dataset

import no.nav.eux.adresse.oppdatering.kafka.model.document.KafkaRinaDocument
import java.time.OffsetDateTime

val case3_kafkaRinaDocumentVersions = KafkaRinaDocument.Version(
    id = 3
)

val case3_kafkaRinaDocumentCreator = KafkaRinaDocument.Creator(
    organisation = KafkaRinaDocument.Creator.Organisation(
        name = "NAV"
    )
)

val case3_kafkaRinaDocumentMetadata = KafkaRinaDocument.Metadata(
    id = "3",
    type = "H001",
    caseId = 3,
    versions = listOf(case3_kafkaRinaDocumentVersions),
    creationDate = OffsetDateTime.now(),
    creator = case3_kafkaRinaDocumentCreator,
    status = "received",
    direction = "IN",
)

val case3_kafkaRinaDocumentPayload = KafkaRinaDocument.Payload(
    documentMetadata = case3_kafkaRinaDocumentMetadata
)

val case3_H001 = KafkaRinaDocument(
    documentEventType = "RECEIVE_DOCUMENT",
    buc = "H_BUC_01",
    payLoad = case3_kafkaRinaDocumentPayload
)