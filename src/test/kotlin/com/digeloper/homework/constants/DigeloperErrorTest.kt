package com.digeloper.homework.constants

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class DigeloperErrorTest : BehaviorSpec({
    Given("DigeloperError 정의 개수") {
        When("총 에러 정의 개수를 확인하면") {
            Then("에러 개수가 정확히 반환되어야 한다") {
                DigeloperError.entries.size shouldBe 7
            }
        }
    }

    Given("NOT_EXIST_EMPLOYEE 오류") {
        val error = DigeloperError.NOT_EXIST_EMPLOYEE

        When("코드와 설명을 확인하면") {
            Then("올바른 코드와 설명이 반환되어야 한다") {
                error.code shouldBe 1000
                error.desc shouldBe "직원이 존재하지 않습니다."
            }
        }
    }

    Given("NOT_EXIST_EMPLOYEE_INFO 오류") {
        val error = DigeloperError.NOT_EXIST_EMPLOYEE_INFO

        When("코드와 설명을 확인하면") {
            Then("올바른 코드와 설명이 반환되어야 한다") {
                error.code shouldBe 1001
                error.desc shouldBe "직원 정보가 존재하지 않습니다."
            }
        }
    }

    Given("FAILED_SAVE_DUPLICATE_EMPLOYEES 오류") {
        val error = DigeloperError.FAILED_SAVE_DUPLICATE_EMPLOYEES

        When("코드와 설명을 확인하면") {
            Then("올바른 코드와 설명이 반환되어야 한다") {
                error.code shouldBe 1002
                error.desc shouldBe "중복된 직원이 존재 합니다."
            }
        }
    }

    Given("FAILED_SAVE_EMPLOYEES 오류") {
        val error = DigeloperError.FAILED_SAVE_EMPLOYEES

        When("코드와 설명을 확인하면") {
            Then("올바른 코드와 설명이 반환되어야 한다") {
                error.code shouldBe 1003
                error.desc shouldBe "직원 저장이 실패했습니다."
            }
        }
    }

    Given("FAILED_SAVE_EMPLOYEES_INFO 오류") {
        val error = DigeloperError.FAILED_SAVE_EMPLOYEES_INFO

        When("코드와 설명을 확인하면") {
            Then("올바른 코드와 설명이 반환되어야 한다") {
                error.code shouldBe 1004
                error.desc shouldBe "직원 정보 등록이 실패했습니다."
            }
        }
    }

    Given("FAILED_LOAD_EMPLOYEES_INFO 오류") {
        val error = DigeloperError.FAILED_LOAD_EMPLOYEES_INFO

        When("코드와 설명을 확인하면") {
            Then("올바른 코드와 설명이 반환되어야 한다") {
                error.code shouldBe 1005
                error.desc shouldBe "직원 정보 로딩이 실패했습니다."
            }
        }
    }

    Given("INVALID_FORMAT_EMPLOYEE_CVS 오류") {
        val error = DigeloperError.INVALID_FORMAT_EMPLOYEE_CVS

        When("코드와 설명을 확인하면") {
            Then("올바른 코드와 설명이 반환되어야 한다") {
                error.code shouldBe 2000
                error.desc shouldBe "올바르지 않은 CVS 포멧입니다."
            }
        }
    }
})

