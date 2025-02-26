package no.nav.eux.adresse.oppdatering.dataset

import no.nav.eux.adresse.oppdatering.kafka.model.document.KafkaRinaDocument
import java.time.OffsetDateTime

val case7_kafkaRinaDocumentVersions = KafkaRinaDocument.Version(
    id = 1
)

val case7_kafkaRinaDocumentCreator = KafkaRinaDocument.Creator(
    organisation = KafkaRinaDocument.Creator.Organisation(
        name = "NAV"
    )
)

val case7_kafkaRinaDocumentMetadata = KafkaRinaDocument.Metadata(
    id = "7",
    type = "H055",
    caseId = 7,
    versions = listOf(case7_kafkaRinaDocumentVersions),
    creationDate = OffsetDateTime.now(),
    creator = case7_kafkaRinaDocumentCreator,
    status = "received",
    direction = "IN",
)

val case7_kafkaRinaDocumentPayload = KafkaRinaDocument.Payload(
    documentMetadata = case7_kafkaRinaDocumentMetadata
)

val case7_H055 = KafkaRinaDocument(
    documentEventType = "RECEIVE_DOCUMENT",
    buc = "S_BUC_12",
    payLoad = case7_kafkaRinaDocumentPayload
)