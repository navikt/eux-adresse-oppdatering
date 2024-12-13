package no.nav.eux.adresse.oppdatering.kafka.config

import org.apache.kafka.clients.CommonClientConfigs.SECURITY_PROTOCOL_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.*
import org.apache.kafka.common.config.SslConfigs.*
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS
import org.springframework.kafka.support.serializer.JsonDeserializer

@Configuration
class KafkaConfig(
    @Value("\${kafka.bootstrap-servers}")
    val bootstrapServers: String,
    @Value("\${kafka.properties.security.protocol}")
    val securityProtocol: String,
    val kafkaSslProperties: KafkaSslProperties
) {

//    @Bean
//    fun rinaDocumentKafkaListenerContainerFactory() = kafkaListenerContainerFactory<KafkaRinaDocument>()

    @Bean
    fun rinaDocumentKafkaListenerContainerFactory() = kafkaListenerContainerFactory<String>()

    private inline fun <reified T> kafkaListenerContainerFactory() =
        ConcurrentKafkaListenerContainerFactory<String, T>()
            .apply { consumerFactory = docConsumerFactory<T>() }

    private inline fun <reified T> docConsumerFactory(): ConsumerFactory<String, T> =
        DefaultKafkaConsumerFactory(
            mapOf(
                BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
                KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
                VALUE_DESERIALIZER_CLASS_CONFIG to ErrorHandlingDeserializer::class.java,
                KEY_DESERIALIZER_CLASS to StringDeserializer::class.java.name,
                VALUE_DESERIALIZER_CLASS to JsonDeserializer::class.java.name,
//                VALUE_DEFAULT_TYPE to T::class.java.name,
                SECURITY_PROTOCOL_CONFIG to securityProtocol,
                SSL_KEYSTORE_TYPE_CONFIG to kafkaSslProperties.keystore.type,
                SSL_KEYSTORE_LOCATION_CONFIG to kafkaSslProperties.keystore.location,
                SSL_KEYSTORE_PASSWORD_CONFIG to kafkaSslProperties.keystore.password,
                SSL_TRUSTSTORE_TYPE_CONFIG to kafkaSslProperties.truststore.type,
                SSL_TRUSTSTORE_LOCATION_CONFIG to kafkaSslProperties.truststore.location,
                SSL_TRUSTSTORE_PASSWORD_CONFIG to kafkaSslProperties.truststore.password
            )
        )
}
