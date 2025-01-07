package no.nav.eux.adresse.oppdatering.integration.client.pdl.model

import java.time.LocalDate

data class PdlPerson(
    val adressebeskyttelse: List<Adressebeskyttelse>?,
    val bostedsadresse: List<Bostedsadresse>?,
    val oppholdsadresse: List<Oppholdsadresse>?,
    val kontaktadresse: List<Kontaktadresse>?,
    val doedsfall: Doedsfall?,
) {

    val dead get() = doedsfall?.doedsdato != null

    data class Adressebeskyttelse(
        val gradering: String
    )

    data class Bostedsadresse(
        val vegadresse: Vegadresse?,
        val utenlandskAdresse: UtenlandskAdresse?,
        val gyldigFraOgMed: String?,
        val gyldigTilOgMed: String?,
        val metadata: Metadata
    )

    data class Oppholdsadresse(
        val vegadresse: Vegadresse?,
        val utenlandskAdresse: UtenlandskAdresse?,
        val gyldigFraOgMed: String?,
        val metadata: Metadata
    )

    data class Kontaktadresse(
        val coAdressenavn: String?,
        val type: String,
        val gyldigFraOgMed: String?,
        val gyldigTilOgMed: String?,
        val folkeregistermetadata: Folkeregistermetadata?,
        val metadata: Metadata,
        val postadresseIFrittFormat: PostadresseIFrittFormat?,
        val vegadresse: Vegadresse?,
        val utenlandskAdresse: UtenlandskAdresse?,
        val utenlandskAdresseIFrittFormat: UtenlandskAdresseIFrittFormat?
    )

    data class Metadata(
        val endringer: List<Endring>?,
        val master: String?,
        val opplysningsId: String?,
        val historisk: Boolean?
    ) {
        data class Endring(
            val kilde: String?,
            val registrert: String?,
            val registrertAv: String?,
            val systemkilde: String?,
            val type: String?
        )
    }

    data class Folkeregistermetadata(
        val gyldighetstidspunkt: String?
    )

    data class Vegadresse(
        val husbokstav: String?,
        val husnummer: String?,
        val adressenavn: String?,
        val postnummer: String?,
        val kommunenummer: String?,
        val bydelsnummer: String?
    )

    data class PostadresseIFrittFormat(
        val adresselinje1: String?,
        val adresselinje2: String?,
        val adresselinje3: String?,
        val postnummer: String?
    )

    data class UtenlandskAdresse(
        val adressenavnNummer: String?,
        val bygningEtasjeLeilighet: String?,
        val postboksNummerNavn: String?,
        val postkode: String?,
        val bySted: String?,
        val regionDistriktOmraade: String?,
        val landkode: String?
    )

    data class UtenlandskAdresseIFrittFormat(
        val adresselinje1: String?,
        val adresselinje2: String?,
        val adresselinje3: String?,
        val byEllerStedsnavn: String?,
        val landkode: String?,
        val postkode: String?
    )

    data class Doedsfall(
        val doedsdato: LocalDate? = null,
        val folkeregistermetadata: Folkeregistermetadata? = null,
        val metadata: Metadata
    )
}
