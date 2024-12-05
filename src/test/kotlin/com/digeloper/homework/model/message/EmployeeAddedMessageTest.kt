package com.digeloper.homework.model.message

import com.digeloper.homework.config.JacksonConfig
import com.digeloper.homework.model.document.EmployeeTest.Companion.EMPLOYEE
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import

@Import(JacksonConfig::class)
class EmployeeAddedMessageTest(
    @Autowired
    private val objectMapper: ObjectMapper
) : BehaviorSpec({
    Given("EmployeeAddedMessage 객체가 있을 때") {
        val employees = listOf(EMPLOYEE)
        val message = EmployeeAddedMessage(employees)

        When("직렬화하여 JSON으로 변환하면") {
            val json = objectMapper.writeValueAsString(message)
            Then("JSON에 type 필드가 포함되어야 한다") {
                json.contains("\"type\":\"EmployeeAddedMessage\"") shouldBe true
            }

            Then("직원 목록이 포함되어야 한다") {
                json.contains("\"employees\":[{\"name\":\"홍길동\",\"email\":\"hong@degeloper.com\",\"tel\":\"010-1234-5678\",\"joined\":[2024,1,1]") shouldBe true
            }
        }
    }
})