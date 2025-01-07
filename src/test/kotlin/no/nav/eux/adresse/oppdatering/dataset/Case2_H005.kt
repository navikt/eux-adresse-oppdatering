package no.nav.eux.adresse.oppdatering.dataset

import no.nav.eux.adresse.oppdatering.kafka.model.document.KafkaRinaDocument
import java.time.OffsetDateTime

val case2_kafkaRinaDocumentVersions = KafkaRinaDocument.Version(
    id = 2
)

val case2_kafkaRinaDocumentCreator = KafkaRinaDocument.Creator(
    organisation = KafkaRinaDocument.Creator.Organisation(
        name = "NAV"
    )
)

val case2_kafkaRinaDocumentMetadata = KafkaRinaDocument.Metadata(
    id = "2",
    type = "H005",
    caseId = 2,
    versions = listOf(case2_kafkaRinaDocumentVersions),
    creationDate = OffsetDateTime.now(),
    creator = case2_kafkaRinaDocumentCreator,
    status = "received",
    direction = "IN",
)

val case2_kafkaRinaDocumentPayload = KafkaRinaDocument.Payload(
    documentMetadata = case2_kafkaRinaDocumentMetadata
)

val case2_H005 = KafkaRinaDocument(
    documentEventType = "RECEIVE_DOCUMENT",
    buc = "H_BUC_01",
    payLoad = case2_kafkaRinaDocumentPayload
)