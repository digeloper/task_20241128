package com.digeloper.homework.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .components(Components())
            .info(info())
    }

    private fun info(): Info {
        return Info()
            .title("Employee API")
            .description("Employee management API reference for developers")
            .version("1.0")
    }
}
