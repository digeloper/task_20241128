package com.digeloper.homework.controller

import com.digeloper.homework.constants.DigeloperError
import com.digeloper.homework.mapper.EmployeeInfoMapper
import com.digeloper.homework.model.dto.EmployeeInfoDtoTest.Companion.EMPLOYEE_INFO_DTO
import com.digeloper.homework.model.hash.EmployeeInfoRedisTest.Companion.EMPLOYEE_INFO
import com.digeloper.homework.model.hash.EmployeeInfoRedisTest.Companion.EMPLOYEE_INFO_CHUL
import com.digeloper.homework.model.hash.EmployeeInfoRedisTest.Companion.EMPLOYEE_INFO_YOUNG
import com.digeloper.homework.service.EmployeeInfoService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import com.digeloper.homework.model.dto.EmployeeInfoDto
import org.springframework.data.domain.PageRequest
import com.digeloper.homework.exception.DigeloperException
import com.digeloper.homework.model.dto.EmployeeInfoDtoTest.Companion.EMPLOYEE_INFO_DTO_CHUL
import com.digeloper.homework.model.dto.EmployeeInfoDtoTest.Companion.EMPLOYEE_INFO_DTO_YOUNG
import com.digeloper.homework.model.response.Employees

@WebFluxTest(controllers = [EmployeeQueryController::class])
class EmployeeQueryControllerTest : ControllerTest() {
    @MockkBean
    private lateinit var employeeInfoService : EmployeeInfoService

    @MockkBean
    private lateinit var employeeInfoMapper: EmployeeInfoMapper

    init {
        describe("GET: /api/employee 요청") {
            it("직원 목록을 반환해야 한다") {
                val pageable = PageRequest.of(0, 10)
                every { employeeInfoService.getEmployeesInfos(pageable) } returns Flux.just(EMPLOYEE_INFO_CHUL, EMPLOYEE_INFO_YOUNG)
                every { employeeInfoService.getTotal() } returns 10
                every { employeeInfoMapper.toDto(EMPLOYEE_INFO_CHUL) } returns EMPLOYEE_INFO_DTO_CHUL
                every { employeeInfoMapper.toDto(EMPLOYEE_INFO_YOUNG) } returns EMPLOYEE_INFO_DTO_YOUNG

                webClient.get()
                    .uri("/api/employee?page=0&pageSize=10")
                    .exchange()
                    .expectStatus().isOk
                    .expectBody(Employees::class.java)
                    .isEqualTo(Employees(10, 0, 10, listOf(EMPLOYEE_INFO_DTO_CHUL, EMPLOYEE_INFO_DTO_YOUNG)))
            }

            it("서비스 호출 실패 시 오류를 반환해야 한다") {
                val pageable = PageRequest.of(0, 10)

                every { employeeInfoService.getEmployeesInfos(pageable) } returns Flux.error(DigeloperException(DigeloperError.FAILED_LOAD_EMPLOYEES_INFO))

                webClient.get()
                    .uri("/api/employee?page=0&pageSize=10")
                    .exchange()
                    .expectStatus().isBadRequest
                    .expectBody()
                    .jsonPath("$.message").isEqualTo(DigeloperError.FAILED_LOAD_EMPLOYEES_INFO.desc)
            }
        }

        describe("GET: /api/employee/{name} 요청") {
            it("특정 직원을 반환해야 한다") {
                every { employeeInfoService.getEmployeeInfo(EMPLOYEE_INFO.name) } returns Mono.just(EMPLOYEE_INFO)
                every { employeeInfoMapper.toDto(any()) } returns EMPLOYEE_INFO_DTO

                webClient.get()
                    .uri("/api/employee/${EMPLOYEE_INFO.name}")
                    .exchange()
                    .expectStatus().isOk
                    .expectBody(EmployeeInfoDto::class.java)
                    .isEqualTo(EMPLOYEE_INFO_DTO)
            }

            it("직원이 없을 경우 오류를 반환해야 한다") {
                every { employeeInfoService.getEmployeeInfo(EMPLOYEE_INFO.name) } returns Mono.error(DigeloperException(DigeloperError.NOT_EXIST_EMPLOYEE_INFO))

                webClient.get()
                    .uri("/api/employee/${EMPLOYEE_INFO.name}")
                    .exchange()
                    .expectStatus().isBadRequest
                    .expectBody()
                    .jsonPath("$.message").isEqualTo(DigeloperError.NOT_EXIST_EMPLOYEE_INFO.desc)
            }
        }
    }
}