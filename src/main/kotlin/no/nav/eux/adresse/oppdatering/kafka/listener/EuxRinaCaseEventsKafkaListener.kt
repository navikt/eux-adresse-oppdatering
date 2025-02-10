package no.nav.eux.adresse.oppdatering.kafka.listener

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.adresse.oppdatering.kafka.model.document.KafkaRinaDocument
import no.nav.eux.adresse.oppdatering.service.AdresseService
import no.nav.eux.logging.clearLocalMdc
import no.nav.eux.logging.mdc
import org.springframework.kafka.annotation.DltHandler
import org.springframework.kafka.annotation.KafkaHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.RetryableTopic
import org.springframework.kafka.support.Acknowledgment
import org.springframework.retry.annotation.Backoff
import org.springframework.stereotype.Service

@KafkaListener(
    id = "eux-adresse-oppdatering-document",
    topics = ["\${kafka.topics.eux-rina-document-events-v1}"],
    containerFactory = "rinaDocumentKafkaListenerContainerFactory"
)
@Service
class EuxRinaCaseEventsKafkaListener(
    val adresseService: AdresseService
) {

    val log = logger {}

    @KafkaHandler
    @RetryableTopic(
        backoff = Backoff(value = 15000L),
        attempts = "3",
        autoCreateTopics = "false"
    )
    fun document(
        kafkaRinaDocument: KafkaRinaDocument,
        acknowledgment: Acknowledgment,
    ) {
        try {
            val documentMetadata = kafkaRinaDocument.payLoad.documentMetadata
            val caseId = documentMetadata.caseId
            val bucType = kafkaRinaDocument.buc
            mdc(
                rinasakId = caseId.toInt(),
                bucType = bucType,
                sedType = documentMetadata.type
            )
            if (documentMetadata.direction != "IN") {
                log.info { "Dokumentet er ikke innkommende, avslutter behandling" }
            } else if (bucTilBehandling(bucType)) {
                log.info { "Starter behandling av dokument" }
                adresseService.oppdaterPdl(kafkaRinaDocument)
                log.info { "Adresseoppdatering for dokument ferdigstillt" }
            } else {
                log.info { "Dokument av denne typen behandles ikke" }
            }
            acknowledgment.acknowledge()
        } catch (e: Exception) {
            log.error(e) { "Feil ved behandling av dokument" }
            throw e
        } finally {
            clearLocalMdc()
        }
    }

    fun bucTilBehandling(bucType: String) =
        if (bucType == "UB_BUC_04")
            false
        else
            when (bucType.split("_").first()) {
                "H", "UB", "FB", "S" -> true
                else -> false
            }

    @DltHandler
    fun dltHandler(
        kafkaRinaDocument: KafkaRinaDocument,
        acknowledgment: Acknowledgment,
    ) {
        val documentMetadata = kafkaRinaDocument.payLoad.documentMetadata
        val caseId = documentMetadata.caseId
        val bucType = kafkaRinaDocument.buc
        mdc(
            rinasakId = caseId.toInt(),
            bucType = bucType,
            sedType = documentMetadata.type
        )
        log.error { "Dokumentet har feilet 3 ganger, sendes til DLT" }
        acknowledgment.acknowledge()
    }
}
