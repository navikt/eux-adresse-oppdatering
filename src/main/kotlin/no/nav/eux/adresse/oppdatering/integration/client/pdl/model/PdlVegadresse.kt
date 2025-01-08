package no.nav.eux.adresse.oppdatering.integration.client.pdl.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING
import com.fasterxml.jackson.annotation.JsonProperty
import no.nav.eux.adresse.oppdatering.model.Adresse

import java.time.LocalDate

data class PdlVegadresse(
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
        val gyldigFraOgMed: LocalDate,
        @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
        val gyldigTilOgMed: LocalDate?,
        val adresse: Adresse,
    )

    data class Adresse(
        val matrikkelId: String?,
        val bruksenhetsnummer: String?,
        val adressenavn: String?,
        val husnummer: String?,
        val husbokstav: String?,
        val tilleggsnavn: String?,
        val postnummer: String?,
        @JsonProperty("@type")
        val type: String,
    )
}

fun Adresse.toPdlVegadresse(
    kilde: String,
    ident: String,
    type: String,
    gyldigTilOgMed: LocalDate? = null,
) =
    PdlVegadresse(
        personopplysninger = listOf(
            PdlVegadresse.Personopplysning(
                ident = ident,
                endringstype = "OPPRETT",
                opplysningstype = type,
                endringsmelding = PdlVegadresse.Endringsmelding(
                    kilde = kilde,
                    gyldigFraOgMed = LocalDate.now(),
                    gyldigTilOgMed = gyldigTilOgMed,
                    type = type,
                    adresse = adresse
                )
            )
        )
    )

private val Adresse.adresse
    get() = PdlVegadresse.Adresse(
        matrikkelId = null,
        bruksenhetsnummer = null,
        adressenavn = adressenavnNummer,
        husnummer = null,
        husbokstav = null,
        tilleggsnavn = bygningEtasjeLeilighet,
        postnummer = postkode,
        type = "VEGADRESSE",
    )
