package no.nav.eux.adresse.oppdatering.service

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.adresse.oppdatering.integration.client.eux.rina.api.EuxRinaApiClient
import no.nav.eux.adresse.oppdatering.integration.client.eux.rina.api.model.EuxRinaApiDokument
import no.nav.eux.adresse.oppdatering.integration.client.eux.rina.api.model.EuxRinaSakOversiktV3
import no.nav.eux.adresse.oppdatering.integration.client.pdl.PdlApiClient
import no.nav.eux.adresse.oppdatering.integration.client.pdl.exception.PdlHttpClientErrorException
import no.nav.eux.adresse.oppdatering.integration.client.pdl.model.PdlPerson
import no.nav.eux.adresse.oppdatering.kafka.model.document.KafkaRinaDocument
import no.nav.eux.adresse.oppdatering.model.harDirekteSpesifisertBostedsadresse
import org.springframework.stereotype.Service

@Service
class AdresseService(
    val euxRinaApiClient: EuxRinaApiClient,
    val pdlService: PdlService,
    val pdlApiClient: PdlApiClient,
) {

    val log = logger {}

    fun oppdaterPdl(kafkaRinaDocument: KafkaRinaDocument) {
        if (kafkaRinaDocument.payLoad.documentMetadata.caseId == 1451402L) {
            log.info { "CaseId brukes for test av feilhåndtering" }
            throw RuntimeException("CaseId brukes for test av feilhåndtering")
        }
        val rinasakId = kafkaRinaDocument.payLoad.documentMetadata.caseId
        val dokument = euxRinaApiClient.dokument(
            rinasakId = rinasakId,
            sedId = kafkaRinaDocument.payLoad.documentMetadata.id
        )
        log.info { "Dokument hentet fra Rina" }
        val rinasak = euxRinaApiClient.rinasak(rinasakId)
        oppdaterAdresseBruker(dokument, rinasak, kafkaRinaDocument.buc)
        oppdaterAdresseEktefelle(dokument, rinasak, kafkaRinaDocument.buc)
        oppdaterAdresseAnnenPerson(dokument, rinasak, kafkaRinaDocument.buc)
    }

    fun oppdaterAdresseBruker(
        dokument: EuxRinaApiDokument,
        rinasak: EuxRinaSakOversiktV3,
        buc: String
    ) {
        val identNor = identNor(dokument, rinasak)
        if (identNor.isNullOrEmpty()) {
            log.info { "Ingen ident for norge funnet, avslutter oppdatering av adresser for bruker" }
        } else {
            if (dokument.sed.startsWith("F") && erMinstSedVersjon(dokument, 4, 4)) {
                dokument.nav.bruker?.person?.adresser
                    ?.filter { it.kanSendesTilPdl() }
                    ?.forEach { oppdaterPdl(it, rinasak, identNor) }
                    ?: log.info { "Ingen adresser å oppdatere på dokument/nav/bruker" }
            } else {
                dokument.nav.bruker?.adresse
                    ?.filter { it.kanSendesTilPdl() }
                    ?.forEach { oppdaterPdl(it, rinasak, identNor) }
                    ?: log.info { "Ingen adresser å oppdatere på dokument/nav/bruker" }
            }
            dokument.horisontal?.anmodningominformasjon?.fastslaabosted?.bruker?.adresse
                ?.filter { it.kanSendesTilPdl() }
                ?.forEach { oppdaterPdl(it, rinasak, identNor) }
                ?: log.info { "Ingen adresser å oppdatere på horisontal/anmodningominformasjon/fastslaabosted" }
            if (harDirekteSpesifisertBostedsadresse(dokument.sed)) {
                val bostedsadresse = dokument.nav.bruker?.bostedsadresse?.copy(type = "bosted")
                if (bostedsadresse != null && bostedsadresse.kanSendesTilPdl())
                    oppdaterPdl(bostedsadresse, rinasak, identNor)
                else
                    log.info { "Ingen bostedsadresse definert på ${dokument.sed}" }
            }
        }
    }

    fun oppdaterAdresseEktefelle(
        dokument: EuxRinaApiDokument,
        rinasak: EuxRinaSakOversiktV3,
        buc: String
    ) {
        val identNor = dokument.identNorEktefelle
        if (identNor.isNullOrEmpty())
            log.info { "Ingen ident for norge funnet, avslutter oppdatering av adresser for ektefelle" }
        else {
            if (dokument.sed.startsWith("F") && erMinstSedVersjon(dokument, 4, 4)) {
                dokument.nav.ektefelle?.person?.adresser
                    ?.filter { it.kanSendesTilPdl() }
                    ?.forEach { oppdaterPdl(it, rinasak, identNor) }
                    ?: log.info { "Ingen adresser å oppdatere på ektefelle" }
            } else {
                dokument.nav.ektefelle?.adresse
                    ?.filter { it.kanSendesTilPdl() }
                    ?.forEach { oppdaterPdl(it, rinasak, identNor) }
                    ?: log.info { "Ingen adresser å oppdatere på ektefelle" }
            }
        }
    }

    fun oppdaterAdresseAnnenPerson(
        dokument: EuxRinaApiDokument,
        rinasak: EuxRinaSakOversiktV3,
        buc: String
    ) {
        val identNor = dokument.identNorAnnenPerson
        if (identNor.isNullOrEmpty())
            log.info { "Ingen ident for norge funnet, avslutter oppdatering av adresser for annen person" }
        else {
            if (dokument.sed.startsWith("F") && erMinstSedVersjon(dokument, 4, 4)) {
                dokument.nav.annenperson?.person?.adresser
                    ?.filter { it.kanSendesTilPdl() }
                    ?.forEach { oppdaterPdl(it, rinasak, identNor) }
                    ?: log.info { "Ingen adresser å oppdatere på annen person" }
            } else {
                dokument.nav.annenperson?.adresse
                    ?.filter { it.kanSendesTilPdl() }
                    ?.forEach { oppdaterPdl(it, rinasak, identNor) }
                    ?: log.info { "Ingen adresser å oppdatere på annen person" }
            }
        }
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
        rinasak: EuxRinaSakOversiktV3,
        identNor: String
    ) {
        try {
            val eksisterendeAdresser = pdlApiClient.hentAdresser(identNor, rinasak.sakType)
            oppdaterPdl(
                adresse = adresse,
                kilde = rinasak.motpartFormatertNavn,
                ident = identNor,
                motpartLandkode = rinasak.motpartLandkode,
                pdlPerson = eksisterendeAdresser
            )
        } catch (e: PdlHttpClientErrorException) {
            log.info(e) { "PDL godtok ikke oppdateringen av adresse" }
        }
    }

    fun oppdaterPdl(
        adresse: EuxRinaApiDokument.Adresse,
        kilde: String,
        ident: String,
        motpartLandkode: String?,
        pdlPerson: PdlPerson
    ) {
        when (adresse.type) {
            "kontakt" -> pdlService.oppdaterKontaktadresse(
                adresse = adresse.transformertAdresse,
                kilde = kilde,
                ident = ident,
                eksisterendeKontaktadresser = pdlPerson.kontaktadresse
            )

            "opphold" -> pdlService.oppdaterOppholdsadresse(
                adresse = adresse.transformertAdresse,
                kilde = kilde,
                ident = ident,
                eksisterendeOppholdsadresser = pdlPerson.oppholdsadresse,
                pdlPerson.dead
            )

            "bosted" -> pdlService.oppdaterBostedsadresse(
                adresse = adresse.transformertAdresse,
                kilde = kilde,
                ident = ident,
                motpartLandkode = motpartLandkode,
                eksisterendeBostedsadresser = pdlPerson.bostedsadresse,
                pdlPerson.dead
            )

            else -> log.info { "Ukjent adresse type ${adresse.type}, adresse blir ikke sendt til PDL" }
        }
    }

    fun erMinstSedVersjon(dokument: EuxRinaApiDokument, sedGVer: Int, sedVer: Int): Boolean {
        val dokumentSedGVer = dokument.sedGVer.toIntOrNull() ?: 0
        val dokumentSedVer = dokument.sedVer.toIntOrNull() ?: 0
        return (dokumentSedGVer > sedGVer)
                || (dokumentSedGVer == sedGVer && dokumentSedVer >= sedVer)
    }

}
