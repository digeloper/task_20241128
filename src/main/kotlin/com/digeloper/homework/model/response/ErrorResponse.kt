package com.digeloper.homework.model.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonUnwrapped
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@JsonIgnoreProperties("headers", "statusCode", "statusCodeValue")
class ErrorResponse<T>(data: T, status: HttpStatus = HttpStatus.BAD_REQUEST) : ResponseEntity<T>(data, status) {
    @JsonUnwrapped
    override fun getBody(): T? {
        return super.getBody()
    }
}