package no.nav.eux.adresse.oppdatering.integration.client.eux.rina.api.model

data class EuxRinaSakOversiktV3(
    val fnr: String?,
    val motparter: List<Motpart>,
) {
    data class Motpart(
        val motpartLandkode: String,
    )

    val motpartLandkode
        get(): String? = motparter.firstOrNull()?.motpartLandkode

}
