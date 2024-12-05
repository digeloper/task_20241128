package com.digeloper.homework.service

import com.digeloper.homework.constants.DigeloperError
import com.digeloper.homework.mapper.EmployeeMapper
import com.digeloper.homework.model.document.Employee
import com.digeloper.homework.model.document.EmployeeTest.Companion.EMPLOYEE_CHUL
import com.digeloper.homework.model.document.EmployeeTest.Companion.EMPLOYEE_YOUNG
import com.digeloper.homework.model.dto.EmployeeDtoTest.Companion.EMPLOYEE_DTO_CHUL
import com.digeloper.homework.model.dto.EmployeeDtoTest.Companion.EMPLOYEE_DTO_YOUNG
import com.digeloper.homework.repository.EmployeeRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class EmployeeServiceTest : BehaviorSpec({
    val kafkaProducer = mockk<KafkaProducer>(relaxed = true)
    val employeeMapper = mockk<EmployeeMapper>()
    val employeeRepository = mockk<EmployeeRepository>()
    val employeeService = EmployeeServiceImpl(kafkaProducer, employeeMapper, employeeRepository)

    Given("saveEmployees 메서드를 호출할 때") {
        When("중복 이메일이 없는 경우") {
            every { employeeMapper.toModel(EMPLOYEE_DTO_CHUL) } returns EMPLOYEE_CHUL
            every { employeeMapper.toModel(EMPLOYEE_DTO_YOUNG) } returns EMPLOYEE_YOUNG
            every { employeeRepository.existsByEmailIsIn(listOf(EMPLOYEE_DTO_CHUL.email, EMPLOYEE_DTO_YOUNG.email)) } returns MONO_FALSE
            every { employeeRepository.saveAll(any<Flux<Employee>>()) } returns FLUX_EMPLOYEES
            every { kafkaProducer.sendMessage(any()) } just Runs

            val result = employeeService.saveEmployees(FLUX_EMPLOYEES_DTO).collectList().block()
            Then("KafkaProducer 의 sendMessage 가 호출되어야 한다") {
                verify(exactly = 1) {
                    kafkaProducer.sendMessage(any())
                }
            }

            Then("결과로 저장된 Employee 리스트가 반환되어야 한다") {
                result?.size?.let { it shouldBeEqual 2 } ?: assert(false)
            }
        }

        When("중복 이메일이 존재하는 경우") {
            every { employeeRepository.existsByEmailIsIn(any()) } returns MONO_TRUE

            val result = StepVerifier.create(employeeService.saveEmployees(FLUX_EMPLOYEES_DTO))
            Then("예외가 발생해야 한다") {
                result.expectSubscription()
                    .verifyErrorMatches { it.message.equals(DigeloperError.FAILED_SAVE_DUPLICATE_EMPLOYEES.desc) }
            }
        }

        When("직원 저장에 실패하는 경우") {
            every { employeeMapper.toModel(EMPLOYEE_DTO_CHUL) } returns EMPLOYEE_CHUL
            every { employeeMapper.toModel(EMPLOYEE_DTO_YOUNG) } returns EMPLOYEE_YOUNG
            every { employeeRepository.existsByEmailIsIn(any()) } returns MONO_FALSE
            every { employeeRepository.saveAll(any<Flux<Employee>>()) } returns Flux.error(RuntimeException())
            every { kafkaProducer.sendMessage(any()) } just Runs

            val result = StepVerifier.create(employeeService.saveEmployees(FLUX_EMPLOYEES_DTO))
            Then("예외가 발생해야 한다") {
                result.expectSubscription()
                    .verifyErrorMatches { it.message.equals(DigeloperError.FAILED_SAVE_EMPLOYEES.desc) }
            }
        }
    }

    afterSpec {
        clearAllMocks()
    }
}) {
    companion object {
        private val MONO_TRUE = Mono.just(true)
        private val MONO_FALSE = Mono.just(false)
        private val FLUX_EMPLOYEES_DTO = Flux.just(EMPLOYEE_DTO_CHUL, EMPLOYEE_DTO_YOUNG)
        private val FLUX_EMPLOYEES = Flux.just(EMPLOYEE_CHUL, EMPLOYEE_YOUNG)
    }
}
