package com.digeloper.homework.service

import com.digeloper.homework.constants.DigeloperError
import com.digeloper.homework.exception.DigeloperException
import com.digeloper.homework.model.document.EmployeeTest.Companion.EMPLOYEE_CHUL
import com.digeloper.homework.model.document.EmployeeTest.Companion.EMPLOYEE_YOUNG
import com.digeloper.homework.model.hash.EmployeeInfoRedisTest.Companion.EMPLOYEE_INFO_CHUL
import com.digeloper.homework.model.hash.EmployeeInfoRedisTest.Companion.EMPLOYEE_INFO_YOUNG
import com.digeloper.homework.model.message.EmployeeAddedMessage
import com.digeloper.homework.service.KafkaProducer.Companion.TOPIC_ADDED
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.apache.kafka.clients.consumer.ConsumerRecord
import reactor.core.publisher.Flux


class KafkaConsumerTest : BehaviorSpec({
    val employeeInfoService = mockk<EmployeeInfoService>()
    val kafkaConsumer = KafkaConsumer(employeeInfoService)
    val employeeAddedMessage = EmployeeAddedMessage(employees = listOf(EMPLOYEE_CHUL, EMPLOYEE_YOUNG))

    Given("KafkaConsumer가 Kafka 메시지를 수신했을 때") {
        When("정상적인 EmployeeAddedMessage가 전달된 경우") {
            every { employeeInfoService.setEmployeesInfos(any()) } returns Flux.just(EMPLOYEE_INFO_CHUL, EMPLOYEE_INFO_YOUNG)

            Then("employeeInfoService 의 setEmployeesInfos 가 호출되어야 한다") {
                val record = ConsumerRecord<String, EmployeeAddedMessage>(TOPIC_ADDED, 0, 0, null, employeeAddedMessage)
                kafkaConsumer.consume(record)
                verify {
                    employeeInfoService.setEmployeesInfos(withArg {
                        it.collectList().block()!!.size shouldBe 2
                    })
                }
            }
        }

        When("EmployeeAddedMessage가 비어있는 경우") {
            every { employeeInfoService.setEmployeesInfos(any()) } returns Flux.empty()
            Then("employeeInfoService는 비어있는 Flux를 처리해야 한다") {
                val record = ConsumerRecord<String, EmployeeAddedMessage>(TOPIC_ADDED, 0, 0, null, EmployeeAddedMessage(employees = emptyList()))
                kafkaConsumer.consume(record)
                verify {
                    employeeInfoService.setEmployeesInfos(withArg {
                        it.collectList().block()!!.isEmpty() shouldBe true
                    })
                }
            }
        }

        When("employeeInfoService에서 예외가 발생한 경우") {
            every { employeeInfoService.setEmployeesInfos(any()) } throws DigeloperException(DigeloperError.FAILED_SAVE_EMPLOYEES_INFO)
            Then("DigeloperException이 발생해야 한다") {
                val record = ConsumerRecord<String, EmployeeAddedMessage>(TOPIC_ADDED, 0, 0, null, employeeAddedMessage)
                shouldThrow<DigeloperException> {
                    kafkaConsumer.consume(record)
                }.code shouldBe DigeloperError.FAILED_SAVE_EMPLOYEES_INFO.code
            }
        }
    }

    afterSpec {
        clearAllMocks()
    }
})
