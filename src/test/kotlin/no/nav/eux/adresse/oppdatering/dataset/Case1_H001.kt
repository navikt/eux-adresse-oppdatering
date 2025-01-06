package no.nav.eux.adresse.oppdatering.dataset

import no.nav.eux.adresse.oppdatering.kafka.model.document.*
import java.time.OffsetDateTime

val kafkaRinaDocumentVersions = KafkaRinaDocumentVersions(
    id = 1
)

val kafkaRinaDocumentCreator = KafkaRinaDocumentCreator(
    organisation = KafkaRinaDocumentCreator.Organisation(
        name = "NAV"
    )
)

val kafkaRinaDocumentMetadata = KafkaRinaDocumentMetadata(
    id = "1",
    type = "H001",
    caseId = 1,
    versions = listOf(kafkaRinaDocumentVersions),
    creationDate = OffsetDateTime.now(),
    creator = kafkaRinaDocumentCreator,
    status = "received"
)

val kafkaRinaDocumentPayload = KafkaRinaDocumentPayload(
    documentMetadata = kafkaRinaDocumentMetadata
)

val case1H001 = KafkaRinaDocument(
    documentEventType = "RECEIVE_DOCUMENT",
    buc = "H_BUC_01",
    payLoad = kafkaRinaDocumentPayload
)