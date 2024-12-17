package no.nav.eux.adresse.oppdatering.integration.client.eux.rina.api.model

data class EuxRinaApiDokument(
    val nav: Nav,
    val sed: String,
    val sedGVer: String,
    val sedVer: String
) {
    data class Nav(
        val bruker: Bruker
    )

    data class Bruker(
        val adresse: List<Adresse>,
        val person: Person
    )

    data class Adresse(
        val type: String,
        val gate: String,
        val land: String,
        val by: String
    )

    data class Person(
        val kjoenn: String,
        val etternavn: String,
        val fornavn: String,
        val foedselsdato: String
    )
}
