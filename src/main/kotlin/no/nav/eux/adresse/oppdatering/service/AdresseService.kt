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
        val kontaktadresse = dokument.kontaktadresse()
        log.info { "Kontaktadresse hentet fra dokument" }
        println(kontaktadresse)
        pdlService.oppdaterKontaktadresse(
            adresse = kontaktadresse!!,
            kilde = kafkaRinaDocument.payLoad.documentMetadata.creator.organisation.name,
            ident = dokument.nav.bruker.person.pin.firstOrNull { it.landkode == "NOR" }?.identifikator!!
        )
        log.info { "Kontaktadresse oppdatert" }
    }

    fun EuxRinaApiDokument.kontaktadresse(): Adresse? {
        val adresse = nav.bruker.adresse.firstOrNull { it.type == "kontakt" }
        return adresse?.let {
            Adresse(
                adressenavnNummer = it.gate,
                bygningEtasjeLeilighet = it.bygning,
                bySted = it.by,
                postkode = it.postnummer,
                regionDistriktOmraade = it.region,
                landkode = it.landkode
            )
        }
    }

}
