package no.nav.eux.adresse.oppdatering.model

data class Adresse(
    val adressenavnNummer: String?,
    val bygningEtasjeLeilighet: String?,
    val bySted: String?,
    val postkode: String?,
    val regionDistriktOmraade: String?,
    val landkode: String?,
)
