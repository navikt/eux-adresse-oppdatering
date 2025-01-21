package no.nav.eux.adresse.oppdatering.integration.client.eux.rina.api.model

data class EuxRinaApiDokument(
    val nav: Nav,
    val horisontal: Horisontal?,
    val sed: String,
    val sedGVer: String,
    val sedVer: String
) {
    data class Nav(
        val bruker: Bruker?,
        val annenperson: AnnenPerson?,
        val ektefelle: Ektefelle?,
    )

    data class Bruker(
        val adresse: List<Adresse>?,
        val person: Person
    )

    data class AnnenPerson(
        val adresse: List<Adresse>?,
        val person: Person
    )

    data class Ektefelle(
        val adresse: List<Adresse>?,
        val person: Person
    )

    data class Adresse(
        val type: String?,
        val gate: String?,
        val landkode: String?,
        val by: String?,
        val bygning: String?,
        val region: String?,
        val postnummer: String?,
    )

    data class Person(
        val kjoenn: String,
        val etternavn: String,
        val fornavn: String,
        val pin: List<Pin>?,
        val foedselsdato: String
    )

    data class Pin(
        val identifikator: String,
        val landkode: String,
    )

    data class Horisontal(
        val anmodningominformasjon: AnmodningOmInformasjon?
    )

    data class AnmodningOmInformasjon(
        val fastslaabosted: FastslaBosted?
    )

    data class FastslaBosted(
        val bruker: Bruker?
    ) {
        data class Bruker(
            val adresse: List<Adresse>?
        )
    }

    val identNor: String?
        get() = nav.bruker?.person?.pin?.firstOrNull { it.landkode == "NOR" }?.identifikator

    val identNorEktefelle: String?
        get() = nav.annenperson?.person?.pin?.firstOrNull { it.landkode == "NOR" }?.identifikator

    val identNorAnnenPerson: String?
        get() = nav.ektefelle?.person?.pin?.firstOrNull { it.landkode == "NOR" }?.identifikator
}