package com.digeloper.homework.mapper

import com.digeloper.homework.model.document.EmployeeTest.Companion.EMPLOYEE
import com.digeloper.homework.model.dto.EmployeeDtoTest.Companion.EMPLOYEE_DTO
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.mapstruct.factory.Mappers

class EmployeeMapperTest : BehaviorSpec({
    val mapper = Mappers.getMapper(EmployeeMapper::class.java)

    Given("EmployeeMapper가 정상적으로 생성되었을 때") {
        When("Employee 객체를 EmployeeDto로 변환하면") {
            val dto = mapper.toDto(EMPLOYEE)

            Then("모든 필드가 정확히 매핑되어야 한다") {
                dto?.let { it.email shouldBe EMPLOYEE.email } ?: assert(false)
                dto?.let { it.name shouldBe EMPLOYEE.name } ?: assert(false)
                dto?.let { it.tel shouldBe EMPLOYEE.tel } ?: assert(false)
                dto?.let { it.joined shouldBe EMPLOYEE.joined } ?: assert(false)
            }
        }

        When("Employee 객체를 null 값을 입력하면") {
            val dto = mapper.toDto(null)
            Then("결과가 null이 나온다.") {
                dto shouldBe null
            }
        }

        When("EmployeeDto 객체를 Employee로 변환하면") {
            val employee = mapper.toModel(EMPLOYEE_DTO)

            Then("모든 필드가 정확히 매핑되어야 한다") {
                employee?.let { it.email shouldBe EMPLOYEE_DTO.email } ?: assert(false)
                employee?.let { it.name shouldBe EMPLOYEE_DTO.name } ?: assert(false)
                employee?.let { it.tel shouldBe EMPLOYEE_DTO.tel } ?: assert(false)
                employee?.let { it.joined shouldBe EMPLOYEE_DTO.joined } ?: assert(false)
            }
        }

        When("EmployeeDto 객체를 null 값을 입력하면") {
            val employee = mapper.toModel(null)
            Then("결과가 null이 나온다.") {
                employee shouldBe null
            }
        }
    }
})
