package no.nav.eux.adresse.oppdatering.kafka.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "kafka.properties.ssl")
data class KafkaSslProperties(
    val keystore: StoreProperties,
    val truststore: StoreProperties
)

data class StoreProperties(
    val type: String,
    val location: String,
    val password: String
)
