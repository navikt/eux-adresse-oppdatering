package no.nav.eux.adresse.oppdatering.kafka.model.document

data class KafkaRinaDocumentCreator(
    val organisation: Organisation
) {
    data class Organisation(
        val name: String
    )
}
