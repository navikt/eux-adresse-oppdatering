package no.nav.eux.adresse.oppdatering.mock

import okhttp3.mockwebserver.MockResponse
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE

val okMockResponse =
    MockResponse().apply {
        setResponseCode(200)
        setHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
    }

infix fun MockResponse.medBody(resource: String) =
    apply {
        setBody(resource.resource)
    }

private val String.resource
    get() = object {}
        .javaClass.getResource(this)!!.readText()
