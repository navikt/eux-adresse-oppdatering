package no.nav.eux.adresse.oppdatering.integration.client.eux.rina.api.model

data class EuxRinaSakOversiktV3(
    val fnr: String?,
    val fornavn: String?,
    val etternavn: String?,
    val foedselsdato: String?,
    val motparter: List<Motpart>,
) {
    data class Motpart(
        val formatertNavn: String,
        val motpartLandkode: String,
    )

    val motpartFormatertNavn
        get(): String = motparter.firstOrNull()?.formatertNavn ?: "EESSI (ukjent motpart)"

    val motpartLandkode
        get(): String? = motparter.firstOrNull()?.motpartLandkode

}
