package com.digeloper.homework.model.reponse

import com.digeloper.homework.model.response.ErrorResponse
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.string.shouldContain
import org.springframework.http.HttpStatus

class ErrorResponseTest : StringSpec({
    val objectMapper: ObjectMapper = jacksonObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)

    "ErrorResponse 가 올바르게 직렬화 및 역직렬화된다" {
        val errorResponse = ErrorResponse("에러 발생", HttpStatus.BAD_REQUEST)
        val json = objectMapper.writeValueAsString(errorResponse)
        json shouldContain "에러 발생"
    }

    "ErrorResponse 는 올바르게 상태 코드를 처리한다" {
        val errorResponse = ErrorResponse("데이터를 찾을 수 없음", HttpStatus.NOT_FOUND)
        val json = objectMapper.writeValueAsString(errorResponse)
        json shouldContain "데이터를 찾을 수 없음"
    }

    "ErrorResponse 는 올바르게 body를 반환한다" {
        val errorResponse = ErrorResponse("권한 없음", HttpStatus.UNAUTHORIZED)
        errorResponse.body shouldContain "권한 없음"
    }
})
