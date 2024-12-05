package com.digeloper.homework.model.hash

import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.ReactiveValueOperations
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.LocalDate

class EmployeeInfoRedisTest : BehaviorSpec({
    val redisTemplate: ReactiveRedisTemplate<String, EmployeeInfo> = mockk()
    val valueOps: ReactiveValueOperations<String, EmployeeInfo> = mockk()

    beforeEach {
        every { redisTemplate.opsForValue() } returns valueOps
    }

    given("Redis 에 EmployeeInfo 저장") {
        `when`("EmployeeInfo 객체를 저장하고 조회하면") {
            every { valueOps.set(EMPLOYEE_INFO.email, EMPLOYEE_INFO) } returns MONO_TRUE
            every { valueOps.get(EMPLOYEE_INFO.email) } returns MONO_EMPLOYEE

            then("저장된 EmployeeInfo 가 조회되어야 한다") {
                val savedMono: Mono<Boolean> = redisTemplate.opsForValue().set(EMPLOYEE_INFO.email, EMPLOYEE_INFO)
                val findMono: Mono<EmployeeInfo> = redisTemplate.opsForValue().get(EMPLOYEE_INFO.email)
                val result = StepVerifier.create(redisTemplate.opsForValue().set(EMPLOYEE_INFO.email, EMPLOYEE_INFO))
                result.expectSubscription()
                    .assertNext{ MONO_TRUE }
                    .then {
                        redisTemplate.opsForValue()[EMPLOYEE_INFO.email]
                    }
                    .assertNext{ EMPLOYEE_INFO }
            }
        }
    }
}) {
    companion object {
        val EMPLOYEE_INFO = EmployeeInfo(
            email = "hong@degeloper.com",
            name = "홍길동",
            tel = "010-1234-5678",
            joined = LocalDate.of(2024, 1,1)
        )

        val EMPLOYEE_INFO_CHUL = EmployeeInfo(
            email = "chul@degeloper.com",
            name = "김철수",
            tel = "010-2345-6789",
            joined = LocalDate.of(2024, 1,1)
        )

        val EMPLOYEE_INFO_YOUNG = EmployeeInfo(
            email = "young@degeloper.com",
            name = "김영희",
            tel = "010-3456-7890",
            joined = LocalDate.of(2024, 1,1)
        )

        private val MONO_TRUE = Mono.just(true)
        private val MONO_EMPLOYEE = Mono.just(EMPLOYEE_INFO)
    }
}