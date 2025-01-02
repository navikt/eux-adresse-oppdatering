package no.nav.eux.adresse.oppdatering.mock

import okhttp3.mockwebserver.MockResponse

val createdEndringResponse =
    MockResponse().apply {
        setHeader("Location", "http://localhost:9500/mock/endringsstatus")
        setResponseCode(201)
    }
