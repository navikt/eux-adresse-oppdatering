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

fun Adresse.toPdlVegadresseOrNull(
    kilde: String,
    ident: String,
    type: String,
    gyldigTilOgMed: LocalDate? = null,
): PdlVegadresse? {
    val adresse = adresseOrNull() ?: return null
    return PdlVegadresse(
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
}

private fun Adresse.adresseOrNull(): PdlVegadresse.Adresse? {
    val adressenavn = adressenavnOrNull(adressenavnNummer)
    val husnummer = husnummerOrNull(adressenavnNummer)
    val husbokstav = husbokstavOrNull(adressenavnNummer)
    val tilleggsnavn = tilleggsnavnOrNull(bygningEtasjeLeilighet)
    if (adressenavn == null || husnummer == null)
        return null
    return PdlVegadresse.Adresse(
        matrikkelId = null,
        bruksenhetsnummer = null,
        adressenavn = adressenavn,
        husnummer = husnummer,
        husbokstav = husbokstav,
        tilleggsnavn = tilleggsnavn,
        postnummer = postkode,
        type = "VEGADRESSE",
    )
}

fun tilleggsnavnOrNull(bygningEtasjeLeilighet: String?): String? =
    if (bygningEtasjeLeilighet.isNullOrBlank())
        null
    else bygningEtasjeLeilighet
        .trim()
        .replace("\\s+".toRegex(), " ")
        .map { if (it.isLetter()) it else "" }
        .joinToString(separator = "")
        .ifEmpty { null }

fun adressenavnOrNull(adressenavn: String?): String? =
    if (adressenavn.isNullOrBlank())
        null
    else adressenavn
        .trim()
        .replace("\\s+".toRegex(), " ")
        .split(" ")
        .takeWhile { it.all { c -> c.isLetter() } }
        .joinToString(separator = " ")

fun husbokstavOrNull(adressenavnNummer: String?): String? {
    if (adressenavnNummer.isNullOrBlank())
        return null
    val lastTwo = adressenavnNummer
        .trim()
        .replace("\\s+".toRegex(), " ")
        .split(" ")
        .takeLast(2)
    if (lastTwo.size < 2)
        return null
    val lastHasDigit = lastTwo.last().any { it.isDigit() }
    if (lastHasDigit)
        return lastTwo
            .last()
            .map { if (it.isLetter()) it else "" }
            .joinToString(separator = "")
            .ifEmpty { null }
    val secondLastHasDigit = lastTwo.first().any { it.isDigit() }
    if (secondLastHasDigit)
        return lastTwo
            .first()
            .map<Any> { if (it.isLetter()) it else "" }
            .joinToString(separator = "")
            .ifEmpty { null } ?: lastTwo.lastOrNull()
    return null
}

fun husnummerOrNull(adressenavnNummer: String?): String? {
    if (adressenavnNummer.isNullOrBlank())
        return null
    val lastTwo = adressenavnNummer
        .trim()
        .replace("\\s+".toRegex(), " ")
        .split(" ")
        .takeLast(2)
    val last = lastTwo.last()
    if (last.all { it.isDigit() })
        return last
    if (last.any { it.isDigit() })
        return last
            .map { if (it.isDigit()) it else "" }
            .joinToString(separator = "")
            .ifEmpty { null }
    val secondLast = lastTwo.first()
    if (secondLast.all { it.isDigit() })
        return secondLast
    return null
}
