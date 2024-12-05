package com.digeloper.homework.controller

import com.digeloper.homework.exception.DigeloperException
import org.slf4j.LoggerFactory
import org.slf4j.event.Level
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["api/test"])
class LogTestController {
    private val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping(value = ["/log/{level}"])
    @Throws(DigeloperException::class)
    fun log(@PathVariable(value = "level", required = true) level: Level) {
        logger.atLevel(level).log("'${level.name}' 레벨 로그입니다.")
    }
}