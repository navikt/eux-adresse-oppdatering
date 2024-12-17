package no.nav.eux.adresse.oppdatering.integration.security

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import no.nav.eux.adresse.oppdatering.integration.security.ClientProperties.ApiClient
import org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import java.time.LocalDateTime.now

@Service
class BearerTokenService(
    val clientProperties: ClientProperties,
    val clientTokens: Map<ApiClient, BearerToken>
) {

    val log = logger {}

    fun BearerToken?.upForRenewal() =
        if (this != null)
            now().plusSeconds(10).isAfter(expiry)
        else
            true

    fun Client.token(): BearerToken {
        val token = RestClient.create()
            .post()
            .uri(clientProperties.tokenEndpoint)
            .contentType(APPLICATION_FORM_URLENCODED)
            .accept(APPLICATION_FORM_URLENCODED)
            .body(createBody())
            .retrieve()
            .body<TokenResponse>()!!
            .bearerToken
        log.info {"Successfully retrieved token for $this" }
        return token
    }

    fun Client.createBody(): MultiValueMap<String, String> {
        val formValues = LinkedMultiValueMap<String, String>()
        formValues.add("grant_type", "client_credentials")
        formValues.add("client_id", clientProperties.id)
        formValues.add("client_secret", clientProperties.secret)
        val scope = when (this) {
            Client.PDL_API -> clientProperties.pdlApi.scope
            Client.PDL_MOTTAK -> clientProperties.pdlMottak.scope
            Client.EUX_RINA_API -> clientProperties.euxRinaApi.scope
        };
        formValues.add("scope", scope)
        return formValues
    }

    enum class Client {
        PDL_API,
        PDL_MOTTAK,
        EUX_RINA_API,
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class TokenResponse(
        @JsonProperty("token_type")
        val tokenType: String,
        @JsonProperty("expires_in")
        val expiresIn: Long,
        @JsonProperty("access_token")
        val accessToken: String
    ) {
        val bearerToken
            get() = BearerToken(accessToken, now().plusSeconds(expiresIn))
    }

}