package com.digeloper.homework.service

import com.digeloper.homework.constants.DigeloperError
import com.digeloper.homework.exception.DigeloperException
import com.digeloper.homework.model.hash.EmployeeInfo
import com.digeloper.homework.model.hash.EmployeeInfoRedisTest.Companion.EMPLOYEE_INFO
import com.digeloper.homework.model.hash.EmployeeInfoRedisTest.Companion.EMPLOYEE_INFO_CHUL
import com.digeloper.homework.model.hash.EmployeeInfoRedisTest.Companion.EMPLOYEE_INFO_YOUNG
import com.digeloper.homework.repository.EmployeeInfoRepository
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import reactor.core.publisher.Flux
import reactor.test.StepVerifier

class EmployeeInfoServiceTest : BehaviorSpec() {
    private val employeeInfoRepository = mockk<EmployeeInfoRepository>()
    private val employeeInfoService = EmployeeInfoServiceImpl(employeeInfoRepository)

    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    init {
        Given("getEmployeeInfo 메서드를 호출할 때") {
            When("이름이 존재하는 경우") {
                every { employeeInfoRepository.findByName(EMPLOYEE_INFO.name) } returns EMPLOYEE_INFO
                val result = StepVerifier.create(employeeInfoService.getEmployeeInfo(EMPLOYEE_INFO.name))
                Then("Mono 에 올바른 EmployeeInfo 가 반환되어야 한다") {
                    result.expectSubscription()
                        .assertNext{ EMPLOYEE_INFO }
                }
            }

            When("이름이 존재하지 않는 경우") {
                every { employeeInfoRepository.findByName(EMPLOYEE_INFO_CHUL.name) } returns null
                val result = StepVerifier.create(employeeInfoService.getEmployeeInfo(EMPLOYEE_INFO_CHUL.name))
                Then("예외가 발생해야 한다") {
                    result.expectSubscription()
                        .verifyErrorMatches { it.message.equals(DigeloperError.NOT_EXIST_EMPLOYEE_INFO.desc) }
                }
            }
        }

        Given("setEmployeesInfos 메서드를 호출할 때") {
            When("직원 정보를 저장할 때") {
                val employeesInfo = Flux.just(EMPLOYEE_INFO_CHUL, EMPLOYEE_INFO_YOUNG)
                every { employeeInfoRepository.saveAll(any<List<EmployeeInfo>>()) } returns listOf(EMPLOYEE_INFO_CHUL, EMPLOYEE_INFO_YOUNG)
                val result = StepVerifier.create(employeeInfoService.setEmployeesInfos(employeesInfo))
                Then("Flux 에 올바른 직원 정보가 반환되어야 한다") {
                    result.expectSubscription()
                        .expectNext(EMPLOYEE_INFO_CHUL, EMPLOYEE_INFO_YOUNG)
                }
            }

            When("직원 정보 저장에 실패할 경우") {
                val employeesInfo = Flux.just(EMPLOYEE_INFO_CHUL, EMPLOYEE_INFO_YOUNG)
                every { employeeInfoRepository.saveAll(any<List<EmployeeInfo>>()) } returns emptyList()
                val result = StepVerifier.create(employeeInfoService.setEmployeesInfos(employeesInfo))
                Then("예외가 발생해야 한다") {
                    result.expectSubscription()
                        .verifyErrorMatches { it.message.equals(DigeloperError.FAILED_SAVE_EMPLOYEES_INFO.desc) }
                }
            }
        }

        Given("getEmployeesInfos 메서드를 호출할 때") {
            When("페이지네이션을 사용하여 직원 정보를 조회할 때") {
                val pageable: Pageable = PageRequest.of(0, 10)
                every { employeeInfoRepository.findBy(pageable) } returns listOf(EMPLOYEE_INFO_CHUL, EMPLOYEE_INFO_YOUNG)
                val result = StepVerifier.create(employeeInfoService.getEmployeesInfos(pageable))
                Then("Flux 에 올바른 직원 정보가 반환되어야 한다") {
                    result.expectSubscription()
                        .expectNext(EMPLOYEE_INFO_CHUL, EMPLOYEE_INFO_YOUNG)
                }
            }

            When("페이지네이션을 사용하여 직원 정보가 없을 때") {
                val pageable: Pageable = PageRequest.of(0, 10)
                every { employeeInfoRepository.findBy(pageable) } returns emptyList()
                val result = StepVerifier.create(employeeInfoService.getEmployeesInfos(pageable))
                Then("예외가 발생해야 한다") {
                    result.expectSubscription()
                        .verifyErrorMatches { it.message.equals(DigeloperError.NOT_EXIST_EMPLOYEE_INFO.desc) }
                }
            }

            When("페이지네이션을 사용하여 직원 정보 조회시 에러날때") {
                val pageable: Pageable = PageRequest.of(0, 10)
                every { employeeInfoRepository.findBy(pageable) } throws RuntimeException()
                val result = StepVerifier.create(employeeInfoService.getEmployeesInfos(pageable))
                Then("예외가 발생해야 한다") {
                    result.expectErrorMatches {
                        it is DigeloperException && it.code == DigeloperError.FAILED_LOAD_EMPLOYEES_INFO.code
                    }.verify()
                }
            }
        }

        Given("getTotal 메서드를 호출할 때") {
            When("저장된 직원 정보가 있을 때") {
                val totalCount = 10L
                every { employeeInfoRepository.count() } returns totalCount
                val result = employeeInfoService.getTotal()
                Then("저장된 직원 숫자가 반환되어야 한다") {
                    result shouldBeEqual totalCount
                }
            }
        }

        afterSpec {
            clearAllMocks()
        }
    }
}
