package com.digeloper.homework.config

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.ApplicationContext
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

@WebFluxTest(value = [KafkaConfig::class])
class KafkaConfigTest : StringSpec() {
    @Autowired
    private lateinit var applicationContext: ApplicationContext

    init {
        "producerFactory 빈이 정상적으로 등록되었는지 확인" {
            val producerFactory = applicationContext.getBean(ProducerFactory::class.java)
            producerFactory shouldNotBe null
        }

        "consumerFactory 빈이 정상적으로 등록되었는지 확인" {
            val consumerFactory = applicationContext.getBean(ConsumerFactory::class.java)
            consumerFactory shouldNotBe null
        }

        "kafkaListenerContainerFactory 빈이 정상적으로 등록되었는지 확인" {
            val listenerContainerFactory = applicationContext.getBean(ConcurrentKafkaListenerContainerFactory::class.java)
            listenerContainerFactory shouldNotBe null
        }

        "kafkaTemplate 빈이 정상적으로 등록되었는지 확인" {
            val kafkaTemplate = applicationContext.getBean(KafkaTemplate::class.java)
            kafkaTemplate shouldNotBe null
        }
    }
}
