package no.nav.eux.adresse.oppdatering.integration.client.pdl.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING
import com.fasterxml.jackson.annotation.JsonProperty
import no.nav.eux.adresse.oppdatering.model.Adresse

import java.time.LocalDate

data class PdlUtenlandskAdresse(
    val personopplysninger: List<Personopplysning>
) {

    data class Personopplysning(
        val endringsmelding: Endringsmelding,
        val ident: String,
        val endringstype: String = "OPPRETT",
        val opplysningstype: String = "KONTAKTADRESSE"
    )

    data class Endringsmelding(
        val kilde: String,
        @JsonProperty("@type")
        val type: String = "KONTAKTADRESSE",
        @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
        val gyldigFraOgMed: LocalDate = LocalDate.now(),
        @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
        val gyldigTilOgMed: LocalDate = LocalDate.now().plusYears(5),
        val adresse: Adresse
    )

    data class Adresse(
        val adressenavnNummer: String?,
        val bygningEtasjeLeilighet: String?,
        val postboksNummerNavn: String? = null,
        val postkode: String,
        val regionDistriktOmraade: String?,
        val landkode: String,
        @JsonProperty("@type")
        val type: String = "UTENLANDSK_ADRESSE"
    )
}

fun Adresse.toPdlUtenlandskAdresse(
    kilde: String,
    ident: String,
    type: String
) =
    PdlUtenlandskAdresse(
        personopplysninger = listOf(
            PdlUtenlandskAdresse.Personopplysning(
                ident = ident,
                endringstype = "OPPRETT",
                opplysningstype = type,
                endringsmelding = PdlUtenlandskAdresse.Endringsmelding(
                    kilde = kilde,
                    gyldigFraOgMed = LocalDate.now(),
                    gyldigTilOgMed = LocalDate.now().plusYears(5),
                    type = type,
                    adresse = adresse
                )
            )
        )
    )

private val Adresse.adresse
    get() = PdlUtenlandskAdresse.Adresse(
        adressenavnNummer = adressenavnNummer,
        bygningEtasjeLeilighet = bygningEtasjeLeilighet,
        postkode = postkode
            ?: throw IllegalArgumentException("Kan ikke opprette utenlandsk adresse uten postkode"),
        regionDistriktOmraade = regionDistriktOmraade,
        landkode = landkode
            ?: throw IllegalArgumentException("Kan ikke opprette utenlandsk adresse uten landkode"),
        type = "UTENLANDSK_ADRESSE"
    )
