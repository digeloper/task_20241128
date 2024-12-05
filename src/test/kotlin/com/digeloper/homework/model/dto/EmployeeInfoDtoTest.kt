package com.digeloper.homework.model.dto

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.time.LocalDate

class EmployeeInfoDtoTest : BehaviorSpec({
    Given("EmployeeInfoDto 인스턴스가 있을 때") {
        When("인스턴스가 생성되면") {
            val employeeInfoDto = EmployeeInfoDto(
                name = EMPLOYEE_INFO_DTO.name,
                email = EMPLOYEE_INFO_DTO.email,
                tel = EMPLOYEE_INFO_DTO.tel,
                joined = EMPLOYEE_INFO_DTO.joined
            )

            then("올바른 값들이 할당되어 있어야 한다") {
                employeeInfoDto.name shouldBe EMPLOYEE_INFO_DTO.name
                employeeInfoDto.email shouldBe EMPLOYEE_INFO_DTO.email
                employeeInfoDto.tel shouldBe EMPLOYEE_INFO_DTO.tel
                employeeInfoDto.joined shouldBe EMPLOYEE_INFO_DTO.joined
            }
        }

        When("직원 정보의 일부 값을 수정하면") {
            val modified = EMPLOYEE_INFO_DTO.copy(name = NAME_MODIFY)

            Then("원본 인스턴스는 수정되지 않아야 한다") {
                EMPLOYEE_INFO_DTO.name shouldNotBe NAME_MODIFY
            }

            Then("복사된 인스턴스는 수정된 값을 반영해야 한다") {
                modified.name shouldBe NAME_MODIFY
            }
        }
    }
}) {
    companion object {
        const val NAME_MODIFY = "new name"

        val EMPLOYEE_INFO_DTO = EmployeeInfoDto(
            email = "hong@degeloper.com",
            name = "홍길동",
            tel = "010-1234-5678",
            joined = LocalDate.of(2024, 1,1)
        )

        val EMPLOYEE_INFO_DTO_CHUL = EmployeeInfoDto(
            email = "chul@degeloper.com",
            name = "김철수",
            tel = "010-2345-6789",
            joined = LocalDate.of(2024, 1,1)
        )

        val EMPLOYEE_INFO_DTO_YOUNG = EmployeeInfoDto(
            email = "young@degeloper.com",
            name = "김영희",
            tel = "010-3456-7890",
            joined = LocalDate.of(2024, 1,1)
        )
    }
}
