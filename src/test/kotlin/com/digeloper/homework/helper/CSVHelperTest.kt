package com.digeloper.homework.helper

import com.digeloper.homework.constants.DigeloperError
import com.digeloper.homework.helper.CSVHelper.Companion.csvDateToLocalDate
import com.digeloper.homework.helper.CSVHelper.Companion.csvToObject
import com.digeloper.homework.model.dto.EmployeeDto
import com.digeloper.homework.model.dto.EmployeeDtoTest.Companion.EMPLOYEE_DTO_CHUL
import com.digeloper.homework.model.dto.EmployeeDtoTest.Companion.EMPLOYEE_DTO_YOUNG
import io.kotest.core.spec.style.BehaviorSpec
import reactor.core.publisher.Flux
import reactor.test.StepVerifier

class CSVHelperTest : BehaviorSpec({
    Given("CSV 데이터를 Flux<String>으로 제공했을 때") {
        When("모든 데이터가 올바른 형식일 경우") {
            val csvData = Flux.just(
                "김철수, chul@degeloper.com, 010-2345-6789, 2024.01.01",
                "김영희, young@degeloper.com, 010-3456-7890, 2024.01.01"
            )

            val employeesDto = csvData.csvToObject { columns ->
                EmployeeDto(
                    name = columns[0],
                    email = columns[1],
                    tel = columns[2],
                    joined = columns[3].csvDateToLocalDate()
                )
            }

            val result = StepVerifier.create(employeesDto.collectList())
            then("Flux<EmployeeDto>로 변환되어야 한다") {
                result.expectSubscription()
                    .expectNext(listOf(EMPLOYEE_DTO_CHUL, EMPLOYEE_DTO_YOUNG))
                    .verifyComplete()
            }
        }

        When("데이터가 잘못된 형식일 경우") {
            val invalidCsvData = Flux.just(
                "홍길동, hong@degeloper.com, 010-1234-5678",                 // JOINED 컬럼 누락
                "김철수, , 010-2345-6789, 2024.01.01",                       // EMAIL 컬럼 빈값
                "김영희, young@degeloper.com, , 010-3456-7890, 2024.01.01"   // 컬럼값이 많은경우
            )

            val invalidEmployeesDto = invalidCsvData.csvToObject { columns ->
                EmployeeDto(
                    name = columns[0],
                    email = columns[1],
                    tel = columns[2],
                    joined = columns[3].csvDateToLocalDate()
                )
            }

            val result = StepVerifier.create(invalidEmployeesDto.collectList())
            Then("DigeloperException 이 발생해야 한다") {
                result.expectSubscription()
                    .verifyErrorMatches { it.message.equals(DigeloperError.INVALID_FORMAT_EMPLOYEE_CVS.desc) }
            }
        }

        When("JOINED 컬럼의 날짜 형식이 잘못된 경우") {
            val invalidDateCsvData = Flux.just(
                "홍길동, hong@degeloper.com, 010-1234-5678, 202401AA"
            )

            val invalidEmployeesDto = invalidDateCsvData.csvToObject { columns ->
                EmployeeDto(
                    name = columns[0],
                    email = columns[1],
                    tel = columns[2],
                    joined = columns[3].csvDateToLocalDate()
                )
            }

            val result = StepVerifier.create(invalidEmployeesDto.collectList())
            Then("DigeloperException 이 발생해야 한다") {
                result.expectSubscription()
                    .verifyErrorMatches { it.message.equals(DigeloperError.INVALID_FORMAT_EMPLOYEE_CVS.desc) }
            }
        }
    }
})
