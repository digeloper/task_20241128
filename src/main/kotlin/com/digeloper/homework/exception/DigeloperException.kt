package com.digeloper.homework.exception

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.digeloper.homework.constants.DigeloperError

@JsonIgnoreProperties("stackTrace", "cause", "localizedMessage", "suppressed", "suppressedExceptions", "detailMessage")
open class DigeloperException : Exception {
    private var error: DigeloperError

    constructor(error: DigeloperError) : super(error.desc) {
        this.error = error
    }

    constructor(error: DigeloperError, message: String?) : super("${error.desc} ($message)") {
        this.error = error
    }

    val code: Int
        get() = error.code
}