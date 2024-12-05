package com.digeloper.homework.config

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import io.kotest.extensions.spring.SpringExtension
import org.springframework.boot.test.context.TestConfiguration

@TestConfiguration
class ProjectConfig: AbstractProjectConfig() {
    override fun extensions(): List<Extension> = listOf(SpringExtension)
}