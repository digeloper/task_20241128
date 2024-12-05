package com.digeloper.homework.controller

import org.slf4j.event.Level
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest

@WebFluxTest(controllers = [LogTestController::class])
class LogTestControllerTest : ControllerTest() {
    init {
        describe("LogTestController") {
            context("유효한 로그 레벨이 제공된 경우") {
                it("지정된 로그 레벨로 로그가 기록되어야 한다") {
                    val level = Level.INFO
                    webClient
                        .post()
                        .uri("/api/test/log/$level")
                        .exchange()
                        .expectStatus().isOk

                }
            }

            context("잘못된 로그 레벨이 제공된 경우") {
                it("BAD_REQUEST 응답이 반환되고 로그는 기록되지 않아야 한다") {
                    val invalidLevel = "INVALID_LEVEL"
                    webClient
                        .post()
                        .uri("/api/test/log/$invalidLevel")
                        .exchange()
                        .expectStatus().isBadRequest
                }
            }
        }
    }
}