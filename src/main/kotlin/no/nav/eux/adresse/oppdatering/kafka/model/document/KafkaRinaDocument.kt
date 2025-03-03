package no.nav.eux.adresse.oppdatering.kafka.model.document

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.OffsetDateTime

data class KafkaRinaDocument(
    val documentEventType: String,
    val buc: String,
    val payLoad: Payload
) {
    val kilde: String
        get() = payLoad.documentMetadata.creator.organisation.name

    data class Creator(
        val organisation: Organisation
    ) {
        data class Organisation(
            val name: String
        )
    }

    data class Metadata(
        val id: String,
        val type: String,
        val caseId: Long,
        val versions: List<Version>,
        val creationDate: OffsetDateTime,
        val creator: Creator,
        val status: String?,
        val direction: String?,
    )

    data class Payload(
        @JsonProperty("DOCUMENT_METADATA")
        val documentMetadata: Metadata
    )

    data class Version(
        val id: Int
    )

    val direction: String?
        get() = payLoad.documentMetadata.direction
}
