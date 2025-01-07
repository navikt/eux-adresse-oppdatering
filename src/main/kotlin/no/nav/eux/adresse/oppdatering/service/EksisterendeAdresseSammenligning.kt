package no.nav.eux.adresse.oppdatering.service

import no.nav.eux.adresse.oppdatering.integration.client.pdl.model.PdlPerson
import no.nav.eux.adresse.oppdatering.model.Adresse

infix fun Adresse.finnesIKontaktadresserI(eksisterendeAdresser: List<PdlPerson.Kontaktadresse>?): Boolean =
    eksisterendeAdresser
        ?.firstOrNull()
        ?.utenlandskAdresse erNestenLik this

infix fun Adresse.finnesIOppholdsadresserI(eksisterendeAdresser: List<PdlPerson.Oppholdsadresse>?): Boolean =
    eksisterendeAdresser
        ?.firstOrNull()
        ?.utenlandskAdresse erNestenLik this

infix fun Adresse.finnesIBostedsadresserI(eksisterendeAdresser: List<PdlPerson.Bostedsadresse>?): Boolean =
    eksisterendeAdresser
        ?.firstOrNull()
        ?.utenlandskAdresse erNestenLik this

infix fun PdlPerson.UtenlandskAdresse?.erNestenLik(adresse: Adresse): Boolean =
    if (this == null)
        false
    else
        when {
            bySted != adresse.bySted -> false
            adressenavnNummer != adresse.adressenavnNummer -> false
            postboksNummerNavn != adresse.postboksNummerNavn -> false
            landkode != adresse.landkode -> false
            else -> true
        }
