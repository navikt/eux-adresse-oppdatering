package no.nav.eux.adresse.oppdatering.integration.config

import no.nav.eux.adresse.oppdatering.integration.security.BearerToken
import no.nav.eux.adresse.oppdatering.integration.security.BearerTokenService
import no.nav.eux.adresse.oppdatering.integration.security.BearerTokenService.Client
import no.nav.eux.adresse.oppdatering.integration.security.BearerTokenService.Client.EUX_RINA_API
import no.nav.eux.adresse.oppdatering.integration.security.ClientProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.web.client.RestClient

@Configuration
class IntegrationConfig {

    @Bean
    fun clientTokens() = HashMap<Client, BearerToken>()

    @Bean
    fun euxRinaApi(
        clientProperties: ClientProperties,
        bearerTokenService: BearerTokenService
    ) = RestClient
        .builder()
        .baseUrl("${clientProperties.euxRinaApi.url}/cpi")
        .requestInterceptor(bearerTokenService interceptorFor EUX_RINA_API)
        .build()

    infix fun BearerTokenService.interceptorFor(
        client: Client
    ) = ClientHttpRequestInterceptor { request, body, execution ->
        val token = fetch(client).token
        request.headers.setBearerAuth(token)
        execution.execute(request, body)
    }

}
