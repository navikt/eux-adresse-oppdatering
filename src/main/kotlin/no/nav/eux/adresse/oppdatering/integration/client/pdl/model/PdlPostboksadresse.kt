package no.nav.eux.adresse.oppdatering.integration.client.pdl.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING
import com.fasterxml.jackson.annotation.JsonProperty
import no.nav.eux.adresse.oppdatering.model.Adresse

import java.time.LocalDate

data class PdlPostboksadresse(
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
        val postbokseier: String? = null,
        val postboks: String,
        val postnummer: String,
        @JsonProperty("@type")
        val type: String,
    )
}

fun Adresse.toPdlPostboksadresseOrNull(
    kilde: String,
    ident: String,
    type: String,
    gyldigFraOgMed: LocalDate? = null,
    gyldigTilOgMed: LocalDate? = null,
): PdlPostboksadresse? {
    val adresse = toPdlVegadresseOrNull() ?: return null
    return PdlPostboksadresse(
        personopplysninger = listOf(
            PdlPostboksadresse.Personopplysning(
                ident = ident,
                endringstype = "OPPRETT",
                opplysningstype = type,
                endringsmelding = PdlPostboksadresse.Endringsmelding(
                    kilde = kilde,
                    gyldigFraOgMed = gyldigFraOgMed,
                    gyldigTilOgMed = gyldigTilOgMed,
                    type = type,
                    adresse = adresse
                )
            )
        )
    )
}

private fun Adresse.toPdlVegadresseOrNull(): PdlPostboksadresse.Adresse? =
    if (postboksNummerNavn == null || postkode == null)
        null
    else
        PdlPostboksadresse.Adresse(
            postboks = postboksNummerNavn,
            postnummer = postkode,
            type = "POSTBOKSADRESSE",
        )
