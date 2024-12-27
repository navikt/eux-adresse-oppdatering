package no.nav.eux.adresse.oppdatering.service

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.adresse.oppdatering.integration.client.pdl.PdlMottakClient
import no.nav.eux.adresse.oppdatering.integration.client.pdl.model.toPdlUtenlandskAdresse
import no.nav.eux.adresse.oppdatering.model.Adresse
import org.springframework.stereotype.Service

@Service
class PdlService(
    val pdlMottakClient: PdlMottakClient
) {

    val log = logger {}

    fun oppdaterKontaktadresse(adresse: Adresse, kilde: String, ident: String) {
        pdlMottakClient endringsmeld adresse.toPdlUtenlandskAdresse(kilde, ident, "KONTAKTADRESSE")
        log.info { "Endringsmelding for kontaktadresse sendt til PDL" }
    }

    fun oppdaterBostedsadresse(adresse: Adresse, kilde: String, ident: String) {
        pdlMottakClient endringsmeld adresse.toPdlUtenlandskAdresse(kilde, ident, "BOSTEDSADRESSE")
        log.info { "Endringsmelding for bostedsadresse sendt til PDL" }
    }

    fun oppdaterOppholdsadresse(adresse: Adresse, kilde: String, ident: String) {
        pdlMottakClient endringsmeld adresse.toPdlUtenlandskAdresse(kilde, ident, "OPPHOLDSADRESSE")
        log.info { "Endringsmelding for oppholdsadresse sendt til PDL" }
    }
}
