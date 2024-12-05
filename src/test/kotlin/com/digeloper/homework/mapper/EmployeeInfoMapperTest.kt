package com.digeloper.homework.mapper

import com.digeloper.homework.model.dto.EmployeeInfoDtoTest.Companion.EMPLOYEE_INFO_DTO
import com.digeloper.homework.model.hash.EmployeeInfoRedisTest.Companion.EMPLOYEE_INFO
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.mapstruct.factory.Mappers

class EmployeeInfoMapperTest : BehaviorSpec({
    val mapper: EmployeeInfoMapper = Mappers.getMapper(EmployeeInfoMapper::class.java)

    Given("EmployeeInfoMapper 가 정상적으로 생성되었을 때") {
        When("EmployeeInfo 객체를 EmployeeInfoDto로 변환하면") {
            val dto = mapper.toDto(EMPLOYEE_INFO)
            Then("모든 필드가 정확히 매핑되어야 한다") {
                dto?.let { it.email shouldBe EMPLOYEE_INFO.email } ?: assert(false)
                dto?.let { it.name shouldBe EMPLOYEE_INFO.name } ?: assert(false)
                dto?.let { it.tel shouldBe EMPLOYEE_INFO.tel } ?: assert(false)
                dto?.let { it.joined shouldBe EMPLOYEE_INFO.joined } ?: assert(false)
            }
        }

        When("EmployeeInfo 객체를 null 값을 입력하면") {
            val dto = mapper.toDto(null)
            Then("결과가 null이 나온다.") {
                dto shouldBe null
            }
        }

        When("EmployeeInfoDto 객체를 EmployeeInfo로 변환하면") {
            val employeeInfo = mapper.toModel(EMPLOYEE_INFO_DTO)
            Then("모든 필드가 정확히 매핑되어야 한다") {
                employeeInfo?.let { it.email shouldBe EMPLOYEE_INFO_DTO.email } ?: assert(false)
                employeeInfo?.let { it.name shouldBe EMPLOYEE_INFO_DTO.name } ?: assert(false)
                employeeInfo?.let { it.tel shouldBe EMPLOYEE_INFO_DTO.tel } ?: assert(false)
                employeeInfo?.let { it.joined shouldBe EMPLOYEE_INFO_DTO.joined } ?: assert(false)
            }
        }

        When("EmployeeInfoDto 객체를 null 값을 입력하면") {
            val employeeInfo = mapper.toModel(null)
            Then("결과가 null이 나온다.") {
                employeeInfo shouldBe null
            }
        }
    }
})
