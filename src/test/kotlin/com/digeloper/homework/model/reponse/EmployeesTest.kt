package com.digeloper.homework.model.reponse

import com.digeloper.homework.model.dto.EmployeeInfoDtoTest.Companion.EMPLOYEE_INFO_DTO_CHUL
import com.digeloper.homework.model.dto.EmployeeInfoDtoTest.Companion.EMPLOYEE_INFO_DTO_YOUNG
import com.digeloper.homework.model.response.Employees
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class EmployeesTest : BehaviorSpec({
    given("Employees 데이터 클래스가 초기화될 때") {
        `when`("유효한 필드 값을 사용하여 객체를 생성하면") {
            val employees = Employees(
                totalCount = 10,
                page = 0,
                pageSize = 2,
                employees = listOf(EMPLOYEE_INFO_DTO_CHUL, EMPLOYEE_INFO_DTO_YOUNG)
            )

            then("모든 필드가 올바르게 초기화되어야 한다") {
                employees.totalCount shouldBe 10
                employees.page shouldBe 0
                employees.pageSize shouldBe 2
                employees.employees shouldHaveSize 2
            }
        }
    }
})
