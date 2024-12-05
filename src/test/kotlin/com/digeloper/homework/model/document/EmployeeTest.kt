package com.digeloper.homework.model.document

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.shouldNotBe
import java.time.LocalDate
import java.time.LocalDateTime

class EmployeeTest : BehaviorSpec({
    Given("Employee 객체") {
       When("Employee 객체를 생성하면") {
            Then("name은 null이 아니어야 한다") {
                EMPLOYEE.name shouldNotBe null
            }

            Then("email은 최소 3자 이상, 50자 이하이어야 한다") {
                EMPLOYEE.email.length shouldBeInRange (IntRange(3, 50))
            }

            Then("tel은 null이 아니어야 한다") {
                EMPLOYEE.tel shouldNotBe null
            }

            Then("joined는 null이 아니어야 한다") {
                EMPLOYEE.joined shouldNotBe null
            }
        }

        When("Employee 객체를 저장한 후 createdAt과 modifiedAt을 확인하면") {
            EMPLOYEE.createdAt = LocalDateTime.now()
            EMPLOYEE.modifiedAt = LocalDateTime.now()
            Then("createdAt과 modifiedAt은 null이 아니어야 한다") {
                EMPLOYEE.createdAt shouldNotBe null
                EMPLOYEE.modifiedAt shouldNotBe null
            }
        }
    }
}) {
    companion object {
        val EMPLOYEE = Employee(
            email = "hong@degeloper.com",
            name = "홍길동",
            tel = "010-1234-5678",
            joined = LocalDate.of(2024, 1,1)
        )

        val EMPLOYEE_CHUL = Employee(
            email = "chul@degeloper.com",
            name = "김철수",
            tel = "010-2345-6789",
            joined = LocalDate.of(2024, 1,1)
        )

        val EMPLOYEE_YOUNG = Employee(
            email = "young@degeloper.com",
            name = "김영희",
            tel = "010-3456-7890",
            joined = LocalDate.of(2024, 1,1)
        )
    }
}
