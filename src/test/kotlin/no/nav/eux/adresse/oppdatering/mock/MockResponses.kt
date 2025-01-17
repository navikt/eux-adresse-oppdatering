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
        body.contains("25105327164") -> okMockResponse medBody "/dataset/mock/pdl-api-adresser-25105327164.json"
        body.contains("25105327165") -> okMockResponse medBody "/dataset/mock/pdl-api-adresser-empty.json"
        body.contains("25105327166") -> okMockResponse medBody "/dataset/mock/pdl-api-adresser-empty.json"
        body.contains("25105327167") -> okMockResponse medBody "/dataset/mock/pdl-api-adresser-empty.json"
        body.contains("25105327168") -> okMockResponse medBody "/dataset/mock/pdl-api-adresser-empty.json"
        body.contains("25105327169") -> okMockResponse medBody "/dataset/mock/pdl-api-adresser-empty.json"
        body.contains("25105327170") -> okMockResponse medBody "/dataset/mock/pdl-api-adresser-empty.json"
        else -> defaultResponse
    }

fun mockResponseGet(request: RecordedRequest) =
    when (request.uriEndsWith) {
        "/cpi/buc/1/sed/1?domene=nav" -> okMockResponse medBody "/dataset/mock/eux-rina-api-dokument-h001.json"
        "/cpi/buc/2/sed/2?domene=nav" -> okMockResponse medBody "/dataset/mock/eux-rina-api-dokument-h005.json"
        "/cpi/buc/3/sed/3?domene=nav" -> okMockResponse medBody "/dataset/mock/eux-rina-api-dokument-h001-norge.json"
        "/cpi/buc/4/sed/4?domene=nav" -> okMockResponse medBody "/dataset/mock/eux-rina-api-dokument-h001-postboks.json"
        "/cpi/buc/5/sed/5?domene=nav" -> okMockResponse medBody "/dataset/mock/eux-rina-api-dokument-h001-ikke-id.json"
        "/cpi/buc/6/sed/6?domene=nav" -> okMockResponse medBody "/dataset/mock/eux-rina-api-dokument-f001.json"
        "/cpi/buc/7/sed/7?domene=nav" -> okMockResponse medBody "/dataset/mock/eux-rina-api-dokument-f002.json"
        "/v3/buc/1/oversikt?domene=nav" -> okMockResponse medBody "/dataset/mock/eux-rina-api-rinasak.json"
        "/v3/buc/2/oversikt?domene=nav" -> okMockResponse medBody "/dataset/mock/eux-rina-api-rinasak.json"
        "/v3/buc/3/oversikt?domene=nav" -> okMockResponse medBody "/dataset/mock/eux-rina-api-rinasak.json"
        "/v3/buc/4/oversikt?domene=nav" -> okMockResponse medBody "/dataset/mock/eux-rina-api-rinasak.json"
        "/v3/buc/5/oversikt?domene=nav" -> okMockResponse medBody "/dataset/mock/eux-rina-api-rinasak.json"
        "/v3/buc/6/oversikt?domene=nav" -> okMockResponse medBody "/dataset/mock/eux-rina-api-rinasak.json"
        "/endringsstatus" -> okMockResponse medBody "/dataset/mock/endringsstatus.json"
        else -> defaultResponse
    }

val defaultResponse =
    MockResponse().apply {
        setHeader(CONTENT_TYPE, TEXT_PLAIN)
        setBody("no mock defined")
        setResponseCode(500)
    }

val RecordedRequest.uriEndsWith get() = requestUrl.toString().split("/mock")[1]
