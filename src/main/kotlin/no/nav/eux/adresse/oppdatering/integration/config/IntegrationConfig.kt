package no.nav.eux.adresse.oppdatering.integration.config

import graphql.scalars.ExtendedScalars
import graphql.schema.idl.RuntimeWiring
import no.nav.eux.adresse.oppdatering.integration.security.BearerToken
import no.nav.eux.adresse.oppdatering.integration.security.BearerTokenService
import no.nav.eux.adresse.oppdatering.integration.security.BearerTokenService.Client
import no.nav.eux.adresse.oppdatering.integration.security.BearerTokenService.Client.*
import no.nav.eux.adresse.oppdatering.integration.security.ClientProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader
import org.springframework.graphql.client.HttpSyncGraphQlClient
import org.springframework.graphql.execution.RuntimeWiringConfigurer
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Configuration
class IntegrationConfig {

    @Bean
    fun clientTokens() = HashMap<Client, BearerToken>()

    @Bean
    fun euxRinaApiRestClient(
        clientProperties: ClientProperties,
        bearerTokenService: BearerTokenService
    ) = RestClient
        .builder()
        .baseUrl(clientProperties.euxRinaApi.url)
        .requestInterceptor(bearerTokenService interceptorFor EUX_RINA_API)
        .build()

    @Bean
    fun pdlMottakRestClient(
        clientProperties: ClientProperties,
        bearerTokenService: BearerTokenService
    ) = RestClient
        .builder()
        .baseUrl(clientProperties.pdlMottak.url)
        .requestInterceptor(bearerTokenService interceptorFor PDL_MOTTAK)
        .build()

    @Bean
    fun pdlHttpSyncGraphQlClient(
        pdlApiRestClient: RestClient
    ) = HttpSyncGraphQlClient
        .create(pdlApiRestClient)

    @Bean
    fun pdlApiRestClient(
        clientProperties: ClientProperties,
        bearerTokenService: BearerTokenService
    ) = RestClient
        .builder()
        .baseUrl("${clientProperties.pdlApi.url}/graphql")
        .requestInterceptor(bearerTokenService interceptorFor PDL_API)
        .build()

    infix fun BearerTokenService.interceptorFor(
        client: Client
    ) = ClientHttpRequestInterceptor { request, body, execution ->
        val token = fetch(client).token
        request.headers.setBearerAuth(token)
        execution.execute(request, body)
    }
}

@Configuration
class GraphQLConfig : RuntimeWiringConfigurer {
    override fun configure(builder: RuntimeWiring.Builder) {
        builder.scalar(ExtendedScalars.Date)
        builder.scalar(ExtendedScalars.DateTime)
        builder.scalar(ExtendedScalars.GraphQLLong)
    }
}

@Component
class GraphqlSpecs(
    val resourceLoader: ResourceLoader
) {
    fun read(fileName: String): String = resourceLoader
        .getResource("classpath:graphql/$fileName")
        .inputStream
        .bufferedReader()
        .use { it.readText() }
}
