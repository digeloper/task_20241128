package com.digeloper.homework.controller

import com.digeloper.homework.mapper.EmployeeMapper
import com.digeloper.homework.model.document.EmployeeTest.Companion.EMPLOYEE_CHUL
import com.digeloper.homework.model.document.EmployeeTest.Companion.EMPLOYEE_YOUNG
import com.digeloper.homework.model.dto.EmployeeDtoTest
import com.digeloper.homework.service.EmployeeService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters
import reactor.core.publisher.Flux

@WebFluxTest(controllers = [EmployeeCommandController::class])
class EmployeeCommandControllerTest : ControllerTest() {
    @MockkBean
    private lateinit var employeeService : EmployeeService

    @MockkBean
    private lateinit var employeeMapper: EmployeeMapper

    private val csvData = Flux.just(
        "김철수, chul@degeloper.com, 010-2345-6789, 2024.01.01",
        "김영희, young@degeloper.com, 010-3456-7890, 2024.01.01"
    )

    private val jsonData = Flux.just(
        "[\n" +
                "  {\n" +
                "    \"name\": \"김철수\",\n" +
                "    \"email\": \"chul@degeloper.com\",\n" +
                "    \"tel\": \"010-2345-6789\",\n" +
                "    \"joined\": \"2024-01-01\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"김영희\",\n" +
                "    \"email\": \"young@degeloper.com\",\n" +
                "    \"tel\": \"010-3456-7890\",\n" +
                "    \"joined\": \"2024-01-01\"\n" +
                "  }\n" +
                "]"
    )

    init {
        describe("EmployeeCommandController") {
            context("CSV 데이터를 POST 요청으로 보냈을 때") {
                it("유효한 데이터가 전달되면 201 Created 와 저장된 데이터를 반환한다") {
                    every { employeeService.saveEmployees(any()) } returns Flux.just(EMPLOYEE_CHUL, EMPLOYEE_YOUNG)
                    every { employeeMapper.toDto(EMPLOYEE_CHUL) } returns EmployeeDtoTest.EMPLOYEE_DTO_CHUL
                    every { employeeMapper.toDto(EMPLOYEE_YOUNG) } returns EmployeeDtoTest.EMPLOYEE_DTO_YOUNG

                    webClient.post()
                        .uri("/api/employee")
                        .contentType(MediaType.parseMediaType("text/csv"))
                        .body(BodyInserters.fromPublisher(csvData, String::class.java))
                        .exchange()
                        .expectStatus().isCreated
                        .expectBody()
                        .jsonPath("$.length()").isEqualTo(2)
                        .jsonPath("$[0].name").isEqualTo(EMPLOYEE_CHUL.name)
                        .jsonPath("$[1].email").isEqualTo(EMPLOYEE_YOUNG.email)
                }
            }

            context("JSON 데이터를 POST 요청으로 보냈을 때") {
                it("유효한 데이터가 전달되면 201 Created 와 저장된 데이터를 반환한다") {
                    every { employeeService.saveEmployees(any()) } returns Flux.just(EMPLOYEE_CHUL, EMPLOYEE_YOUNG)
                    every { employeeMapper.toDto(EMPLOYEE_CHUL) } returns EmployeeDtoTest.EMPLOYEE_DTO_CHUL
                    every { employeeMapper.toDto(EMPLOYEE_YOUNG) } returns EmployeeDtoTest.EMPLOYEE_DTO_YOUNG

                    webClient.post()
                        .uri("/api/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(jsonData, String::class.java))
                        .exchange()
                        .expectStatus().isCreated
                        .expectBody()
                        .jsonPath("$.length()").isEqualTo(2)
                        .jsonPath("$[0].name").isEqualTo(EMPLOYEE_CHUL.name)
                        .jsonPath("$[1].email").isEqualTo(EMPLOYEE_YOUNG.email)
                }
            }
        }
    }
}