package no.nav.eux.adresse.oppdatering.service

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.adresse.oppdatering.integration.client.eux.rina.api.EuxRinaApiClient
import no.nav.eux.adresse.oppdatering.integration.client.eux.rina.api.model.EuxRinaApiDokument
import no.nav.eux.adresse.oppdatering.kafka.model.document.KafkaRinaDocument
import no.nav.eux.adresse.oppdatering.model.Adresse
import org.springframework.stereotype.Service

@Service
class AdresseService(
    val euxRinaApiClient: EuxRinaApiClient,
    val pdlService: PdlService,
) {

    val log = logger {}

    fun oppdaterPdl(kafkaRinaDocument: KafkaRinaDocument) {
        val dokument = euxRinaApiClient.dokument(
            rinasakId = kafkaRinaDocument.payLoad.documentMetadata.caseId,
            sedId = kafkaRinaDocument.payLoad.documentMetadata.id
        ) ?: throw RuntimeException("Dokument ikke funnet")
        log.info { "Dokument hentet fra Rina" }
        println(dokument)
        dokument.nav.bruker.adresse
            ?.filter { it.kanSendesTilPdl() }
            ?.forEach {
                oppdaterPdl(
                    adresse = it,
                    kilde = kafkaRinaDocument.kilde,
                    ident = dokument.identNor!!
                )
            }
            ?: log.info { "Ingen adresser å oppdatere på dokument/nav/bruker" }
        log.info { "Oppdatering av kontaktadresser ferdig" }
    }

    fun EuxRinaApiDokument.Adresse.kanSendesTilPdl(): Boolean =
        when {
            landkode.isNullOrBlank() -> false
                .also { log.info { "Landkode er null, adresse blir ikke sendt til PDL" } }

            postnummer.isNullOrBlank() -> false
                .also { log.info { "Postnummer er null, adresse blir ikke sendt til PDL" } }

            else -> true
        }

    fun oppdaterPdl(
        adresse: EuxRinaApiDokument.Adresse,
        kilde: String,
        ident: String
    ) {
        when (adresse.type) {
            "kontakt" -> {
                pdlService.oppdaterKontaktadresse(
                    adresse = adresse.validertAdresse,
                    kilde = kilde,
                    ident = ident
                )
            }

            "bosted" -> {
                pdlService.oppdaterBostedsadresse(
                    adresse = adresse.validertAdresse,
                    kilde = kilde,
                    ident = ident
                )
            }

            "opphold" -> {
                pdlService.oppdaterOppholdsadresse(
                    adresse = adresse.validertAdresse,
                    kilde = kilde,
                    ident = ident
                )
            }

            else -> {
                log.info { "Ukjent adresse type ${adresse.type}, adresse blir ikke sendt til PDL" }
            }
        }
    }

    val EuxRinaApiDokument.Adresse.validertAdresse: Adresse
        get() = adresse().validertAdresse()

    fun EuxRinaApiDokument.Adresse.adresse(): Adresse =
        Adresse(
            adressenavnNummer = gate,
            bygningEtasjeLeilighet = bygning,
            bySted = by,
            postkode = postnummer,
            regionDistriktOmraade = region,
            landkode = landkode
        )

    fun Adresse.validertAdresse() =
        copy(
            adressenavnNummer = adressenavnNummerValidert(),
            postboksNummerNavn = adressenavnNummerValidert()
        )
            .flyttAdressenavnNummerTilPostboksNummerNavn()

    fun Adresse.adressenavnNummerValidert(): String? =
        when {
            adressenavnNummer in ordForUkjent -> {
                log.info { "Gate definert som $adressenavnNummer, setter feltet til null" }
                null
            }

            adressenavnNummer?.none { it.isLetter() } == true -> {
                log.info { "Ingen bokstaver, setter feltet til null" }
                null
            }

            else -> {
                adressenavnNummer
            }
        }

    fun Adresse.flyttAdressenavnNummerTilPostboksNummerNavn(): Adresse =
        if (adressenavnNummer != null && ordForPostboks.any { it in adressenavnNummer })
            copy(adressenavnNummer = null, postboksNummerNavn = adressenavnNummer)
        else this

}
