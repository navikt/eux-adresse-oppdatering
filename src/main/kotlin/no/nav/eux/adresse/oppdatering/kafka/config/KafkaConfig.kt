package no.nav.eux.adresse.oppdatering.kafka.config

import com.fasterxml.jackson.core.StreamReadConstraints
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JSR310Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.jakarta.xmlbind.JakartaXmlBindAnnotationModule
import no.nav.eux.adresse.oppdatering.kafka.model.document.KafkaRinaDocument
import org.apache.kafka.clients.CommonClientConfigs.SECURITY_PROTOCOL_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.*
import org.apache.kafka.common.config.SslConfigs.*
import org.apache.kafka.common.serialization.ByteArraySerializer
import org.apache.kafka.common.serialization.Serializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.listener.ContainerProperties.AckMode.MANUAL
import org.springframework.kafka.support.serializer.DelegatingByTypeSerializer
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer.VALUE_DEFAULT_TYPE
import org.springframework.kafka.support.serializer.JsonSerializer
import java.time.Duration.ofSeconds

@Configuration
class KafkaConfig(
    @Value("\${kafka.bootstrap-servers}")
    val bootstrapServers: String,
    @Value("\${kafka.properties.security.protocol}")
    val securityProtocol: String,
    val kafkaSslProperties: KafkaSslProperties
) {

    @Bean
    fun rinaDocumentKafkaListenerContainerFactory() = kafkaListenerContainerFactory<KafkaRinaDocument>()

    @Bean
    fun producerFactory(): ProducerFactory<String, Any> =
        DefaultKafkaProducerFactory(
            producerConfiguration<KafkaRinaDocument>(), StringSerializer(),
            DelegatingByTypeSerializer(
                mapOf<Class<*>, Serializer<*>>(
                    ByteArray::class.java to ByteArraySerializer(),
                    KafkaRinaDocument::class.java to JsonSerializer<Any>()
                )
            )
        )

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, Any> =
        KafkaTemplate(producerFactory())

    private inline fun <reified T : Any> kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, T> {
        val containerFactory = ConcurrentKafkaListenerContainerFactory<String, T>().apply {
            containerProperties.setAuthExceptionRetryInterval(ofSeconds(4L))
            containerProperties.ackMode = MANUAL
        }
        containerFactory.setConsumerFactory(docConsumerFactory<T>())
        return containerFactory
    }

    private inline fun <reified T : Any> docConsumerFactory(): ConsumerFactory<String, T> =
        DefaultKafkaConsumerFactory<String, T>(
            producerConfiguration<T>()
        )

    private inline fun <reified T> producerConfiguration() = mapOf(
        BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
        KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
        VALUE_DESERIALIZER_CLASS_CONFIG to ErrorHandlingDeserializer::class.java,
        KEY_DESERIALIZER_CLASS to StringDeserializer::class.java.name,
        VALUE_DESERIALIZER_CLASS to JsonDeserializer::class.java.name,
        ENABLE_AUTO_COMMIT_CONFIG to false,
        VALUE_DEFAULT_TYPE to T::class.java.name,
        SECURITY_PROTOCOL_CONFIG to securityProtocol,
        SSL_KEYSTORE_TYPE_CONFIG to kafkaSslProperties.keystore.type,
        SSL_KEYSTORE_LOCATION_CONFIG to kafkaSslProperties.keystore.location,
        SSL_KEYSTORE_PASSWORD_CONFIG to kafkaSslProperties.keystore.password,
        SSL_TRUSTSTORE_TYPE_CONFIG to kafkaSslProperties.truststore.type,
        SSL_TRUSTSTORE_LOCATION_CONFIG to kafkaSslProperties.truststore.location,
        SSL_TRUSTSTORE_PASSWORD_CONFIG to kafkaSslProperties.truststore.password
    )
    @Bean
    fun objectMapper() : ObjectMapper =
        JsonMapper.builder().addModules(JakartaXmlBindAnnotationModule(), JSR310Module(), Jdk8Module(), JavaTimeModule()).build()




}