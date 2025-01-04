package no.nav.eux.adresse.oppdatering

import io.kotest.assertions.json.shouldEqualSpecifiedJson
import no.nav.eux.adresse.oppdatering.mock.RequestBodies
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.kafka.ConfluentKafkaContainer
import org.testcontainers.utility.DockerImageName

@ActiveProfiles("test")
@SpringBootTest(
    classes = [Application::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
abstract class AbstractTest {

    companion object {

        @JvmStatic
        @Container
        val kafka = ConfluentKafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka")
        )

        @JvmStatic
        @DynamicPropertySource
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("kafka.bootstrap-servers", kafka::getBootstrapServers)
            registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers)
        }
    }

    infix fun String.send(topic: Any) =
        kafkaTemplate.send(this, topic)

    @Autowired
    lateinit var requestBodies: RequestBodies

    @Autowired
    lateinit var kafkaTemplate: KafkaTemplate<String, Any>

    infix fun String.requestNumber(number: Int) =
        requestBodies[this]!![number]

    fun skrivUtEksterneKall() {
        println("Følgende requests ble utført i prosessen:")
        requestBodies.forEach { println("Path: ${it.key}, body: ${it.value}") }
    }

    infix fun String.shouldEqual(resource: String) {
        this shouldEqualSpecifiedJson resource.resource
    }

    private val String.resource
        get() = object {}
            .javaClass.getResource(this)!!.readText()

}
