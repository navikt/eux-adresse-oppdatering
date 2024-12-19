package no.nav.eux.adresse.oppdatering.service

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.adresse.oppdatering.integration.client.pdl.PdlMottakClient
import no.nav.eux.adresse.oppdatering.integration.client.pdl.model.OpprettPdlKontaktadresse
import no.nav.eux.adresse.oppdatering.integration.client.pdl.model.OpprettPdlKontaktadresse.Personopplysning
import no.nav.eux.adresse.oppdatering.model.Adresse
import org.springframework.stereotype.Service

@Service
class PdlService(
    val pdlMottakClient: PdlMottakClient
) {

    val log = logger {}

    fun oppdaterKontaktadresse(
        adresse: Adresse,
        kilde: String,
        ident: String
    ) {
        val opprettPdlKontaktadresse = adresse.toOpprettPdlKontaktadresse(kilde, ident)
        pdlMottakClient.endringsmelding(opprettPdlKontaktadresse)
        log.info { "Endringsmelding for kontaktadresse sendt til PDL" }
    }
}

fun Adresse.toOpprettPdlKontaktadresse(kilde: String, ident: String) =
    OpprettPdlKontaktadresse(
        personopplysninger = listOf(
            Personopplysning(
                ident = ident,
                endringsmelding = OpprettPdlKontaktadresse.Endringsmelding(
                    kilde = kilde,
                    adresse = OpprettPdlKontaktadresse.Adresse(
                        adressenavnNummer = this.adressenavnNummer!!,
                        bygningEtasjeLeilighet = this.bygningEtasjeLeilighet!!,
                        postkode = this.postkode!!,
                        regionDistriktOmraade = this.regionDistriktOmraade!!,
                        landkode = this.landkode!!
                    )
                )
            )
        )
    )
