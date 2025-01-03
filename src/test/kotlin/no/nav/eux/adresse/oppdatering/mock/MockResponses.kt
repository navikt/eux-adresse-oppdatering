package no.nav.eux.adresse.oppdatering.mock

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType.TEXT_PLAIN

fun mockResponse(request: RecordedRequest) =
    when (request.method) {
        HttpMethod.POST.name() -> mockResponsePost(request)
        HttpMethod.GET.name() -> mockResponseGet(request)
        else -> defaultResponse
    }

fun mockResponsePost(request: RecordedRequest) =
    when (request.uriEndsWith) {
        "/oauth2/v2.0/token" -> tokenResponse()
        "/api/v1/endringer" -> createdEndringResponse
        else -> defaultResponse
    }

fun mockResponseGet(request: RecordedRequest) =
    when (request.uriEndsWith) {
        "/cpi/buc/1/sed/1?domene=nav" -> okMockResponse medBody "/dataset/eux-rina-api-dokument.json"
        "/v3/buc/1/oversikt?domene=nav" -> okMockResponse medBody "/dataset/eux-rina-api-rinasak.json"
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
