package no.nav.eux.adresse.oppdatering.service

import no.nav.eux.adresse.oppdatering.integration.client.eux.rina.api.model.EuxRinaApiDokument
import no.nav.eux.adresse.oppdatering.integration.client.eux.rina.api.model.EuxRinaSakOversiktV3
import org.apache.commons.text.similarity.LevenshteinDistance


fun identNor(
    dokument: EuxRinaApiDokument,
    rinasak: EuxRinaSakOversiktV3
): String? =
    dokument.identNor ?: if (sammePerson(dokument, rinasak))
        rinasak.fnr
    else
        null

fun sammePerson(
    dokument: EuxRinaApiDokument,
    rinasak: EuxRinaSakOversiktV3
): Boolean =
    when {
        dokument.nav.bruker.person.foedselsdato != rinasak.foedselsdato -> {
            log.info { "Fødselsdato stemmer ikke overens mellom dokument og rinasak" }
            false
        }

        dokument.nav.bruker.person.etternavn nestenLik rinasak.etternavn -> {
            log.info { "Etternavn stemmer ikke overens mellom dokument og rinasak" }
            false
        }

        dokument.nav.bruker.person.fornavn nestenLik rinasak.fornavn -> {
            log.info { "Fornavn stemmer ikke overens mellom dokument og rinasak" }
            false
        }

        else -> true
    }

private infix fun String?.nestenLik(other: String?) =
    when {
        this == null || other == null -> true
        else -> this.lowercase() levenshteinDistance other.lowercase() < 3
    }

private infix fun String.levenshteinDistance(other: String) =
    LevenshteinDistance.getDefaultInstance().apply(this, other)