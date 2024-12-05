package com.digeloper.homework.config

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import io.swagger.v3.oas.models.OpenAPI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.ApplicationContext

@WebFluxTest(value = [SwaggerConfig::class])
class SwaggerConfigTest : StringSpec() {
    @Autowired
    private lateinit var applicationContext: ApplicationContext

    init {
        "customOpenAPI 빈이 정상적으로 구성되었는지 확인" {
            val openAPI = applicationContext.getBean(OpenAPI::class.java)
            openAPI shouldNotBe null
        }
    }
}
