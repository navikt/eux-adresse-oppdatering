package no.nav.eux.adresse.oppdatering.mock

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType.TEXT_PLAIN

fun mockResponse(request: RecordedRequest, body: String) =
    when (request.method) {
        HttpMethod.POST.name() -> mockResponsePost(request, body)
        HttpMethod.GET.name() -> mockResponseGet(request)
        else -> defaultResponse
    }

fun mockResponsePost(request: RecordedRequest, body: String) =
    when (request.uriEndsWith) {
        "/oauth2/v2.0/token" -> tokenResponse()
        "/api/v1/endringer" -> createdEndringResponse
        "/graphql" -> mockResponsePostGraphql(body)
        else -> defaultResponse
    }

fun mockResponsePostGraphql(body: String) =
    when {
        body.contains("25105327164") -> okMockResponse medBody "/dataset/pdl-api-adresser-25105327164.json"
        body.contains("25105327165") -> okMockResponse medBody "/dataset/pdl-api-adresser-25105327165.json"
        else -> defaultResponse
    }

fun mockResponseGet(request: RecordedRequest) =
    when (request.uriEndsWith) {
        "/cpi/buc/1/sed/1?domene=nav" -> okMockResponse medBody "/dataset/eux-rina-api-dokument-h001.json"
        "/cpi/buc/2/sed/2?domene=nav" -> okMockResponse medBody "/dataset/eux-rina-api-dokument-h005.json"
        "/v3/buc/1/oversikt?domene=nav" -> okMockResponse medBody "/dataset/eux-rina-api-rinasak.json"
        "/v3/buc/2/oversikt?domene=nav" -> okMockResponse medBody "/dataset/eux-rina-api-rinasak.json"
        "/endringsstatus" -> okMockResponse medBody "/dataset/endringsstatus.json"
        else -> defaultResponse
    }

val defaultResponse  =
    MockResponse().apply {
        setHeader(CONTENT_TYPE, TEXT_PLAIN)
        setBody("no mock defined")
        setResponseCode(500)
    }

val RecordedRequest.uriEndsWith get() = requestUrl.toString().split("/mock")[1]
