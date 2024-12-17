package no.nav.eux.adresse.oppdatering.integration.security

import java.time.LocalDateTime

data class BearerToken(
    val token: String,
    val expiry: LocalDateTime
)
