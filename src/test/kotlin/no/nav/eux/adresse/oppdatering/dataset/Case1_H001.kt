package no.nav.eux.adresse.oppdatering.dataset

import no.nav.eux.adresse.oppdatering.kafka.model.document.KafkaRinaDocument
import java.time.OffsetDateTime

val case1_kafkaRinaDocumentVersions = KafkaRinaDocument.Version(
    id = 1
)

val case1_kafkaRinaDocumentCreator = KafkaRinaDocument.Creator(
    organisation = KafkaRinaDocument.Creator.Organisation(
        name = "NAV"
    )
)

val case1_kafkaRinaDocumentMetadata = KafkaRinaDocument.Metadata(
    id = "1",
    type = "H001",
    caseId = 1,
    versions = listOf(case1_kafkaRinaDocumentVersions),
    creationDate = OffsetDateTime.now(),
    creator = case1_kafkaRinaDocumentCreator,
    status = "received",
    direction = "IN",
)

val case1_kafkaRinaDocumentPayload = KafkaRinaDocument.Payload(
    documentMetadata = case1_kafkaRinaDocumentMetadata
)

val case1_H001 = KafkaRinaDocument(
    documentEventType = "RECEIVE_DOCUMENT",
    buc = "H_BUC_01",
    payLoad = case1_kafkaRinaDocumentPayload
)