package no.nav.eux.adresse.oppdatering.service

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.adresse.oppdatering.integration.client.eux.rina.api.model.EuxRinaApiDokument
import no.nav.eux.adresse.oppdatering.model.Adresse

val log = logger {}

val EuxRinaApiDokument.Adresse.validertAdresse: Adresse
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

fun Adresse.adressenavnNummerValidert(): String? =
    when {
        adressenavnNummer in ordForUkjent -> {
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
        bygningEtasjeLeilighet in ordForUkjent -> {
            log.info { "BygningEtasjeLeilighet definert som $bygningEtasjeLeilighet, setter feltet til null" }
            null
        }

        bygningEtasjeLeilighet?.none { it.isLetter() } == true -> {
            log.info { "BygningEtasjeLeilighet har ingen bokstaver, setter feltet til null" }
            null
        }

        else -> {
            bygningEtasjeLeilighet
        }
    }

fun Adresse.byStedValidert(): String? =
    when {
        bySted in ordForUkjent -> {
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
        postkode in ordForUkjent -> {
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
        regionDistriktOmraade in ordForUkjent -> {
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
    if (adressenavnNummer != null && ordForPostboks.any { it in adressenavnNummer })
        copy(adressenavnNummer = null, postboksNummerNavn = adressenavnNummer)
    else this
