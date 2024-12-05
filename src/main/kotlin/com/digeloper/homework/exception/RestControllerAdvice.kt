package com.digeloper.homework.exception

import com.digeloper.homework.model.response.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@ControllerAdvice(annotations = [RestController::class])
class RestControllerAdvice {
    @ExceptionHandler(value = [DigeloperException::class])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun handleGranary(ex: DigeloperException): ErrorResponse<DigeloperException> {
        return ErrorResponse(ex, HttpStatus.BAD_REQUEST)
    }
}