package com.digeloper.homework.config

import com.digeloper.homework.mapper.EmployeeMapper
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.ApplicationContext

@WebFluxTest(value = [MapperConfig::class])
class MapperConfigTest : StringSpec() {
    @Autowired
    private lateinit var applicationContext: ApplicationContext

    init {
        "employeeMapper 빈이 정상적으로 등록되었는지 확인" {
            val employeeMapper = applicationContext.getBean(EmployeeMapper::class.java)
            employeeMapper shouldNotBe null
        }
    }
}
