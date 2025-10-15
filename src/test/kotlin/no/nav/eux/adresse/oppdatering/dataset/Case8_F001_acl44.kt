package no.nav.eux.adresse.oppdatering.dataset

import no.nav.eux.adresse.oppdatering.kafka.model.document.KafkaRinaDocument
import java.time.OffsetDateTime

val case_8kafkaRinaDocumentVersions = KafkaRinaDocument.Version(
    id = 1
)

val case_8kafkaRinaDocumentCreator = KafkaRinaDocument.Creator(
    organisation = KafkaRinaDocument.Creator.Organisation(
        name = "NAV"
    )
)

val case_8kafkaRinaDocumentMetadata = KafkaRinaDocument.Metadata(
    id = "8",
    type = "F001",
    caseId = 8,
    versions = listOf(case_8kafkaRinaDocumentVersions),
    creationDate = OffsetDateTime.now(),
    creator = case_8kafkaRinaDocumentCreator,
    status = "received",
    direction = "IN",
)

val case_8kafkaRinaDocumentPayload = KafkaRinaDocument.Payload(
    documentMetadata = case_8kafkaRinaDocumentMetadata
)

val case_8F001 = KafkaRinaDocument(
    documentEventType = "RECEIVE_DOCUMENT",
    buc = "FB_BUC_01",
    payLoad = case_8kafkaRinaDocumentPayload
)