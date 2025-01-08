package com.digeloper.homework.helper

import com.digeloper.homework.constants.DigeloperError
import com.digeloper.homework.exception.DigeloperException
import org.apache.commons.lang3.StringUtils
import reactor.core.publisher.Flux
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CSVHelper {
    companion object {
        fun <T> Flux<String>.csvToObject(transform: (List<String>) -> T): Flux<T> {
            return this.map { line ->
                try {
                    val columns = StringUtils.deleteWhitespace(line).split(",")
                    transform(columns)
                } catch (e: Exception) {
                    throw DigeloperException(DigeloperError.INVALID_FORMAT_EMPLOYEE_CVS)
                }
            }
        }

        fun String.csvDateToLocalDate(): LocalDate {
            val date = this.replace(".", "")
            return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"))
        }
    }
}