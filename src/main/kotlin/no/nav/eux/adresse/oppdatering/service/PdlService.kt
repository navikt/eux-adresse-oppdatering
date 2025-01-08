package no.nav.eux.adresse.oppdatering.service

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.adresse.oppdatering.integration.client.pdl.PdlMottakClient
import no.nav.eux.adresse.oppdatering.integration.client.pdl.model.PdlPerson
import no.nav.eux.adresse.oppdatering.integration.client.pdl.model.toPdlUtenlandskAdresse
import no.nav.eux.adresse.oppdatering.integration.client.pdl.model.toPdlVegadresse
import no.nav.eux.adresse.oppdatering.model.Adresse
import org.springframework.stereotype.Service
import java.time.LocalDate.now

@Service
class PdlService(
    val pdlMottakClient: PdlMottakClient
) {

    val log = logger {}

    fun oppdaterKontaktadresse(
        adresse: Adresse,
        kilde: String,
        ident: String,
        eksisterendeKontaktadresser: List<PdlPerson.Kontaktadresse>?
    ) {
        when {
            adresse finnesIKontaktadresserI eksisterendeKontaktadresser ->
                log.info { "Kontaktadresse er ikke sendt til PDL, da adresse allerede er registrert" }

            adresse.landkode == "NOR" -> {
                pdlMottakClient endringsmeld adresse.toPdlVegadresse(
                    kilde = kilde,
                    ident = ident,
                    type = "KONTAKTADRESSE",
                    gyldigTilOgMed = now().plusYears(5)
                )
                log.info { "Endringsmelding for kontaktadresse sendt til PDL" }
            }

            else -> {
                pdlMottakClient endringsmeld adresse.toPdlUtenlandskAdresse(
                    kilde = kilde,
                    ident = ident,
                    type = "KONTAKTADRESSE",
                    gyldigTilOgMed = now().plusYears(5)
                )
                log.info { "Endringsmelding for kontaktadresse sendt til PDL" }
            }
        }
    }

    fun oppdaterOppholdsadresse(
        adresse: Adresse,
        kilde: String,
        ident: String,
        eksisterendeOppholdsadresser: List<PdlPerson.Oppholdsadresse>?,
        dead: Boolean
    ) {
        when {
            dead -> log.info { "Oppdaterer ikke oppholdsadresse for død person" }

            adresse finnesIOppholdsadresserI eksisterendeOppholdsadresser ->
                log.info { "Kontaktadresse er ikke sendt til PDL, da adresse allerede er registrert" }

            adresse.landkode == "NOR" -> {
                pdlMottakClient endringsmeld adresse.toPdlVegadresse(
                    kilde = kilde,
                    ident = ident,
                    type = "OPPHOLDSADRESSE"
                )
                log.info { "Endringsmelding for norsk oppholdsadresse sendt til PDL" }
            }

            else -> {
                pdlMottakClient endringsmeld adresse.toPdlUtenlandskAdresse(
                    kilde = kilde,
                    ident = ident,
                    type = "OPPHOLDSADRESSE"
                )
                log.info { "Endringsmelding for utenlandsk oppholdsadresse sendt til PDL" }
            }
        }
    }

    fun oppdaterBostedsadresse(
        adresse: Adresse,
        kilde: String,
        ident: String,
        motpartLandkode: String?,
        eksisterendeBostedsadresser: List<PdlPerson.Bostedsadresse>?,
        dead: Boolean
    ) {
        when {
            dead -> log.info { "Oppdaterer ikke bostedsadresse for død person" }

            adresse.landkode == "NOR" -> log.info { "Oppdaterer ikke norsk bostedsadresse" }

            adresse finnesIBostedsadresserI eksisterendeBostedsadresser ->
                log.info { "Bostedsadresse er ikke sendt til PDL, da adresse allerede er registrert" }

            motpartLandkode == null ->
                log.info { "Bostedsadresse er ikke sendt til PDL, da motpartLandkode er null" }

            motpartLandkode != adresse.landkode ->
                log.info { "Bostedsadresse er ikke sendt til PDL, da landkode ikke er lik motpartLandkode" }

            else -> {
                pdlMottakClient endringsmeld adresse.toPdlUtenlandskAdresse(
                    kilde = kilde,
                    ident = ident,
                    type = "BOSTEDSADRESSE"
                )
                log.info { "Endringsmelding for bostedsadresse sendt til PDL" }
            }
        }
    }
}
