package no.nav.eux.adresse.oppdatering.service

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.adresse.oppdatering.integration.client.eux.rina.api.model.EuxRinaApiDokument
import no.nav.eux.adresse.oppdatering.model.Adresse

val log = logger {}

val EuxRinaApiDokument.Adresse.transformertAdresse: Adresse
    get() = adresse().validert()

fun EuxRinaApiDokument.Adresse.adresse(): Adresse =
    Adresse(
        adressenavnNummer = gate,
        bygningEtasjeLeilighet = bygning,
        bySted = by,
        postkode = postnummer,
        regionDistriktOmraade = region,
        landkode = landkode
    )

fun Adresse.validert() =
    copy(
        adressenavnNummer = adressenavnNummerValidert(),
        bygningEtasjeLeilighet = bygningEtasjeLeilighetValidert(),
        bySted = byStedValidert(),
        postkode = postkodeValidert(),
        regionDistriktOmraade = regionDistriktOmraadeValidert(),
    )
        .flyttAdressenavnNummerTilPostboksNummerNavn()
        .flyttBygningEtasjeLeilighetToAdresseNummer()

fun Adresse.adressenavnNummerValidert(): String? =
    when {
        adressenavnNummer?.lowercase() in ordForUkjent -> {
            log.info { "AdressenavnNummer definert som $adressenavnNummer, setter feltet til null" }
            null
        }

        adressenavnNummer?.none { it.isLetter() } == true -> {
            log.info { "AdressenavnNummer har ingen bokstaver, setter feltet til null" }
            null
        }

        else -> {
            adressenavnNummer
        }
    }

fun Adresse.bygningEtasjeLeilighetValidert(): String? =
    when {
        bygningEtasjeLeilighet?.lowercase() in ordForUkjent -> {
            log.info { "BygningEtasjeLeilighet definert som $bygningEtasjeLeilighet, setter feltet til null" }
            null
        }

        bygningEtasjeLeilighet.isNullOrBlank() -> {
            log.info { "BygningEtasjeLeilighet er tom, setter feltet til null" }
            null
        }

        else -> {
            bygningEtasjeLeilighet
        }
    }

fun Adresse.byStedValidert(): String? =
    when {
        bySted?.lowercase() in ordForUkjent -> {
            log.info { "BySted definert som $bySted, setter feltet til null" }
            null
        }

        bySted?.none { it.isLetterOrDigit() } == true -> {
            log.info { "BySted har ingen bokstaver eller tall, setter feltet til null" }
            null
        }

        else -> {
            bySted
        }
    }

fun Adresse.postkodeValidert(): String? =
    when {
        postkode?.lowercase() in ordForUkjent -> {
            log.info { "Postkode definert som $postkode, setter feltet til null" }
            null
        }

        postkode?.none { it.isLetterOrDigit() } == true -> {
            log.info { "Postkode har ingen bokstaver eller tall, setter feltet til null" }
            null
        }

        else -> {
            postkode
        }
    }

fun Adresse.regionDistriktOmraadeValidert(): String? =
    when {
        regionDistriktOmraade?.lowercase() in ordForUkjent -> {
            log.info { "RegionDistriktOmraade definert som $regionDistriktOmraade, setter feltet til null" }
            null
        }

        regionDistriktOmraade?.none { it.isLetterOrDigit() } == true -> {
            log.info { "RegionDistriktOmraade har ingen bokstaver eller tall, setter feltet til null" }
            null
        }

        else -> {
            regionDistriktOmraade
        }
    }

fun Adresse.flyttAdressenavnNummerTilPostboksNummerNavn(): Adresse =
    if (adressenavnNummer != null && ordForPostboks.any { it in adressenavnNummer.lowercase() })
        copy(adressenavnNummer = null, postboksNummerNavn = adressenavnNummer)
    else
        this

fun Adresse.flyttBygningEtasjeLeilighetToAdresseNummer(): Adresse =
    if ((adressenavnNummer != null && bygningEtasjeLeilighet != null) && bygningEtasjeLeilighet.none { it.isLetter() })
        copy(
            adressenavnNummer = "$adressenavnNummer $bygningEtasjeLeilighet",
            bygningEtasjeLeilighet = null
        )
    else
        this
