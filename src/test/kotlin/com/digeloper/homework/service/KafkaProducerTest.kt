package com.digeloper.homework.service

import com.digeloper.homework.model.document.EmployeeTest.Companion.EMPLOYEE_CHUL
import com.digeloper.homework.model.document.EmployeeTest.Companion.EMPLOYEE_YOUNG
import com.digeloper.homework.model.message.EmployeeAddedMessage
import com.digeloper.homework.model.message.Message
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.springframework.kafka.core.KafkaTemplate

class KafkaProducerTest : BehaviorSpec({
    val kafkaTemplate = mockk<KafkaTemplate<String, Message>>(relaxed = true)
    val kafkaProducer = KafkaProducer(kafkaTemplate)

    Given("KafkaProducer가 sendMessage를 호출했을 때") {
        val testMessage = EmployeeAddedMessage(listOf(EMPLOYEE_CHUL, EMPLOYEE_YOUNG))
        When("유효한 메시지가 전달된 경우") {
            every { kafkaTemplate.send(KafkaProducer.TOPIC_ADDED, testMessage) } returns mockk(relaxed = true)

            Then("KafkaTemplate의 send 메서드가 호출되어야 한다") {
                kafkaProducer.sendMessage(testMessage)

                verify {
                    kafkaTemplate.send(KafkaProducer.TOPIC_ADDED, testMessage)
                }
            }
        }

        When("KafkaTemplate의 send 메서드에서 예외가 발생한 경우") {
            every { kafkaTemplate.send(any(), any()) } throws RuntimeException("Kafka send error")

            Then("예외가 발생해야 한다") {
                shouldThrow<RuntimeException> {
                    kafkaProducer.sendMessage(testMessage)
                }.message shouldBe "Kafka send error"
            }
        }
    }

    afterSpec {
        clearAllMocks()
    }
})
