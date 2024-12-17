package no.nav.eux.adresse.oppdatering.integration.config

import no.nav.eux.adresse.oppdatering.integration.security.BearerToken
import no.nav.eux.adresse.oppdatering.integration.security.BearerTokenService
import no.nav.eux.adresse.oppdatering.integration.security.BearerTokenService.Client
import no.nav.eux.adresse.oppdatering.integration.security.ClientProperties.ApiClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.ClientHttpRequestInterceptor

@Configuration
class IntegrationConfig {

    @Bean
    fun clientTokens() = HashMap<Client, BearerToken>()

    fun bearerTokenInterceptor(
        bearerTokenService: BearerTokenService,
        client: Client
    ): ClientHttpRequestInterceptor {
        return ClientHttpRequestInterceptor { request, body, execution ->
            val token = bearerTokenService.fetch(client).token
            request.headers.setBearerAuth(token)
            execution.execute(request, body)
        }
    }

}
