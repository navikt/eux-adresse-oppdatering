package no.nav.eux.adresse.oppdatering.kafka.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "kafka.properties.consumer")
data class KafkaConsumerProperties(
    val maxPollInterval: Integer,
    val sessionTimeout: Integer
)

