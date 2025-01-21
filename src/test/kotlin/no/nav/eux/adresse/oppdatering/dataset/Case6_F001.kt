package no.nav.eux.adresse.oppdatering.dataset

import no.nav.eux.adresse.oppdatering.kafka.model.document.KafkaRinaDocument
import java.time.OffsetDateTime

val case6_kafkaRinaDocumentVersions = KafkaRinaDocument.Version(
    id = 1
)

val case6_kafkaRinaDocumentCreator = KafkaRinaDocument.Creator(
    organisation = KafkaRinaDocument.Creator.Organisation(
        name = "NAV"
    )
)

val case6_kafkaRinaDocumentMetadata = KafkaRinaDocument.Metadata(
    id = "6",
    type = "F001",
    caseId = 6,
    versions = listOf(case6_kafkaRinaDocumentVersions),
    creationDate = OffsetDateTime.now(),
    creator = case6_kafkaRinaDocumentCreator,
    status = "received",
    direction = "IN",
)

val case6_kafkaRinaDocumentPayload = KafkaRinaDocument.Payload(
    documentMetadata = case6_kafkaRinaDocumentMetadata
)

val case6_F001 = KafkaRinaDocument(
    documentEventType = "RECEIVE_DOCUMENT",
    buc = "FB_BUC_01",
    payLoad = case6_kafkaRinaDocumentPayload
)