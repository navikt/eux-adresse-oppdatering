package no.nav.eux.adresse.oppdatering.integration.security

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("client")
data class ClientProperties(
    val tokenEndpoint: String,
    val id: String,
    val secret: String,
    val pdlApi: ApiClient,
    val pdlMottak: ApiClient,
    val euxRinaApi: ApiClient,
) {
    data class ApiClient(
        val url: String,
        val scope: String
    )
}
