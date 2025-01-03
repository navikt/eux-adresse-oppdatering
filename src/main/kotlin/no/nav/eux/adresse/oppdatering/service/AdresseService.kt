package no.nav.eux.adresse.oppdatering.service

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.adresse.oppdatering.integration.client.eux.rina.api.EuxRinaApiClient
import no.nav.eux.adresse.oppdatering.integration.client.eux.rina.api.model.EuxRinaApiDokument
import no.nav.eux.adresse.oppdatering.kafka.model.document.KafkaRinaDocument
import org.springframework.stereotype.Service

@Service
class AdresseService(
    val euxRinaApiClient: EuxRinaApiClient,
    val pdlService: PdlService,
) {

    val log = logger {}

    fun oppdaterPdl(kafkaRinaDocument: KafkaRinaDocument) {
        val rinasakId = kafkaRinaDocument.payLoad.documentMetadata.caseId
        val dokument = euxRinaApiClient.dokument(
            rinasakId = rinasakId,
            sedId = kafkaRinaDocument.payLoad.documentMetadata.id
        )
        log.info { "Dokument hentet fra Rina" }
        println(dokument)
        val rinasak = euxRinaApiClient.rinasak(rinasakId)
        val identNor = identNor(dokument, rinasak)
        if (identNor.isNullOrEmpty()) {
            log.info { "Ingen ident for norge funnet, avslutter oppdatering av kontaktadresser" }
            return
        }
        dokument.nav.bruker.adresse
            ?.filter { it.kanSendesTilPdl() }
            ?.forEach {
                oppdaterPdl(
                    adresse = it,
                    kilde = kafkaRinaDocument.kilde,
                    ident = identNor,
                    motpartLandkode = rinasak.motpartLandkode
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
        ident: String,
        motpartLandkode: String?
    ) {
        when (adresse.type) {
            "kontakt" -> {
                pdlService.oppdaterKontaktadresse(
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

            "bosted" -> {
                pdlService.oppdaterBostedsadresse(
                    adresse = adresse.validertAdresse,
                    kilde = kilde,
                    ident = ident,
                    motpartLandkode = motpartLandkode
                )
            }

            else -> {
                log.info { "Ukjent adresse type ${adresse.type}, adresse blir ikke sendt til PDL" }
            }
        }
    }

}
