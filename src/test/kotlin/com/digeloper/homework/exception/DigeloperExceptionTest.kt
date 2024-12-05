package com.digeloper.homework.exception

import com.digeloper.homework.constants.DigeloperError
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.assertThrows

class DigeloperExceptionTest : BehaviorSpec({

    val objectMapper: ObjectMapper = jacksonObjectMapper()

    given("DigeloperException 기본 생성자가 호출될 때") {
        `when`("NOT_EXIST_EMPLOYEE 에러로 예외가 생성되면") {
            val exception = DigeloperException(DigeloperError.NOT_EXIST_EMPLOYEE)
            then("에러 코드와 메시지가 정확해야 한다") {
                exception.code shouldBe DigeloperError.NOT_EXIST_EMPLOYEE.code
                exception.message shouldBe DigeloperError.NOT_EXIST_EMPLOYEE.desc
            }
        }
    }

    given("DigeloperException 커스텀 메시지 생성자가 호출될 때") {
        `when`("추가 메시지와 함께 예외가 생성되면") {
            val customMessage = "추가 정보가 포함된 메시지"
            val exception = DigeloperException(DigeloperError.FAILED_SAVE_DUPLICATE_EMPLOYEES, customMessage)
            then("에러 코드와 메시지가 정확해야 한다") {
                exception.code shouldBe DigeloperError.FAILED_SAVE_DUPLICATE_EMPLOYEES.code
                exception.message shouldBe "${DigeloperError.FAILED_SAVE_DUPLICATE_EMPLOYEES.desc} ($customMessage)"
            }
        }
    }

    given("DigeloperException이 JSON으로 직렬화될 때") {
        `when`("INVALID_FORMAT_EMPLOYEE_CVS 에러로 예외가 생성되면") {
            val exception = DigeloperException(DigeloperError.INVALID_FORMAT_EMPLOYEE_CVS)
            val json = objectMapper.writeValueAsString(exception)

            then("stackTrace 같은 불필요한 필드는 제외되어야 한다") {
                json.contains("stackTrace") shouldBe false
                json.contains("cause") shouldBe false
            }
            then("에러 코드와 메시지만 JSON에 포함되어야 한다") {
                json shouldBe """{"code":${DigeloperError.INVALID_FORMAT_EMPLOYEE_CVS.code},"message":"${DigeloperError.INVALID_FORMAT_EMPLOYEE_CVS.desc}"}"""
            }
        }
    }

    given("예외가 발생할 때") {
        `when`("DigeloperException이 throw 되면") {
            then("적절한 에러 코드와 메시지를 포함해야 한다") {
                val exception = assertThrows<DigeloperException> {
                    throw DigeloperException(DigeloperError.FAILED_SAVE_EMPLOYEES_INFO)
                }
                exception.code shouldBe DigeloperError.FAILED_SAVE_EMPLOYEES_INFO.code
                exception.message shouldBe DigeloperError.FAILED_SAVE_EMPLOYEES_INFO.desc
            }
        }
    }
})
