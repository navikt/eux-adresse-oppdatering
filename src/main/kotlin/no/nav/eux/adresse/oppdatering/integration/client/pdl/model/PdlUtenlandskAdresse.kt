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
        val endringstype: String,
        val opplysningstype: String,
    )

    data class Endringsmelding(
        val kilde: String,
        @JsonProperty("@type")
        val type: String,
        @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
        val gyldigFraOgMed: LocalDate?,
        @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
        val gyldigTilOgMed: LocalDate?,
        val adresse: Adresse,
    )

    data class Adresse(
        val adressenavnNummer: String?,
        val bygningEtasjeLeilighet: String?,
        val postboksNummerNavn: String? = null,
        val postkode: String,
        val bySted: String?,
        val regionDistriktOmraade: String?,
        val landkode: String,
        @JsonProperty("@type")
        val type: String,
    )
}

fun Adresse.toPdlUtenlandskAdresse(
    kilde: String,
    ident: String,
    type: String,
    gyldigFraOgMed: LocalDate? = null,
    gyldigTilOgMed: LocalDate? = null,
) =
    PdlUtenlandskAdresse(
        personopplysninger = listOf(
            PdlUtenlandskAdresse.Personopplysning(
                ident = ident,
                endringstype = "OPPRETT",
                opplysningstype = type,
                endringsmelding = PdlUtenlandskAdresse.Endringsmelding(
                    kilde = kilde,
                    gyldigFraOgMed = gyldigFraOgMed,
                    gyldigTilOgMed = gyldigTilOgMed,
                    type = type,
                    adresse = adresse
                )
            )
        )
    )

private val Adresse.adresse
    get() = PdlUtenlandskAdresse.Adresse(
        adressenavnNummer = adressenavnNummer,
        postboksNummerNavn = postboksNummerNavn,
        bygningEtasjeLeilighet = bygningEtasjeLeilighet,
        postkode = postkode
            ?: throw IllegalArgumentException("Kan ikke opprette utenlandsk adresse uten postkode"),
        regionDistriktOmraade = regionDistriktOmraade,
        landkode = landkode
            ?: throw IllegalArgumentException("Kan ikke opprette utenlandsk adresse uten landkode"),
        type = "UTENLANDSK_ADRESSE",
        bySted = bySted,
    )
