package no.nav.eux.adresse.oppdatering.integration.config

import no.nav.eux.adresse.oppdatering.integration.security.BearerToken
import no.nav.eux.adresse.oppdatering.integration.security.ClientProperties.ApiClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class IntegrationConfig {

    @Bean
    fun clientTokens(): Map<ApiClient, BearerToken> = HashMap<ApiClient, BearerToken>()

}
