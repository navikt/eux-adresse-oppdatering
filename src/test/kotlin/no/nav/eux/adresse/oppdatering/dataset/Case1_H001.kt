package no.nav.eux.adresse.oppdatering.dataset

import no.nav.eux.adresse.oppdatering.kafka.model.document.KafkaRinaDocument
import java.time.OffsetDateTime

val kafkaRinaDocumentVersions = KafkaRinaDocument.Version(
    id = 1
)

val kafkaRinaDocumentCreator = KafkaRinaDocument.Creator(
    organisation = KafkaRinaDocument.Creator.Organisation(
        name = "NAV"
    )
)

val kafkaRinaDocumentMetadata = KafkaRinaDocument.Metadata(
    id = "1",
    type = "H001",
    caseId = 1,
    versions = listOf(kafkaRinaDocumentVersions),
    creationDate = OffsetDateTime.now(),
    creator = kafkaRinaDocumentCreator,
    status = "received",
    direction = "IN",
)

val kafkaRinaDocumentPayload = KafkaRinaDocument.Payload(
    documentMetadata = kafkaRinaDocumentMetadata
)

val case1H001 = KafkaRinaDocument(
    documentEventType = "RECEIVE_DOCUMENT",
    buc = "H_BUC_01",
    payLoad = kafkaRinaDocumentPayload
)