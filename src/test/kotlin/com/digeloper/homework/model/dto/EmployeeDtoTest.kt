package com.digeloper.homework.model.dto

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.time.LocalDate

class EmployeeDtoTest : BehaviorSpec({
    Given("EmployeeDto 인스턴스가 있을 때") {
        When("인스턴스가 생성되면") {
            val employeeDto = EmployeeDto(
                name = EMPLOYEE_DTO.name,
                email = EMPLOYEE_DTO.email,
                tel = EMPLOYEE_DTO.tel,
                joined = EMPLOYEE_DTO.joined
            )

            Then("올바른 값들이 할당되어 있어야 한다") {
                employeeDto.name shouldBe EMPLOYEE_DTO.name
                employeeDto.email shouldBe EMPLOYEE_DTO.email
                employeeDto.tel shouldBe EMPLOYEE_DTO.tel
                employeeDto.joined shouldBe EMPLOYEE_DTO.joined
            }
        }

        When("인스턴스를 복사하여 일부 값을 수정하면") {
            val modified = EMPLOYEE_DTO.copy(name = NAME_MODIFY)

            Then("원본 인스턴스는 수정되지 않아야 한다") {
                EMPLOYEE_DTO.name shouldNotBe NAME_MODIFY
            }

            Then("복사된 인스턴스는 수정된 값을 반영해야 한다") {
                modified.name shouldBe NAME_MODIFY
            }
        }
    }
}) {
    companion object {
        const val NAME_MODIFY = "new name"

        val EMPLOYEE_DTO = EmployeeDto(
            email = "hong@degeloper.com",
            name = "홍길동",
            tel = "010-1234-5678",
            joined = LocalDate.of(2024, 1,1)
        )

        val EMPLOYEE_DTO_CHUL = EmployeeDto(
            email = "chul@degeloper.com",
            name = "김철수",
            tel = "010-2345-6789",
            joined = LocalDate.of(2024, 1,1)
        )

        val EMPLOYEE_DTO_YOUNG = EmployeeDto(
            email = "young@degeloper.com",
            name = "김영희",
            tel = "010-3456-7890",
            joined = LocalDate.of(2024, 1,1)
        )
    }
}
