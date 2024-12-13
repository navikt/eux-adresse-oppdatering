package no.nav.eux.adresse.oppdatering

import no.nav.eux.adresse.oppdatering.kafka.config.KafkaSslProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(
    KafkaSslProperties::class
)
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
