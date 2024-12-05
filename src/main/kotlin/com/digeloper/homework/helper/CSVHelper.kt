package com.digeloper.homework.helper

import com.digeloper.homework.constants.DigeloperError
import com.digeloper.homework.exception.DigeloperException
import com.digeloper.homework.model.dto.EmployeeDto
import org.apache.commons.lang3.StringUtils
import reactor.core.publisher.Flux
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CSVHelper {
    companion object {
        fun Flux<String>.csvToEmployeesDto(): Flux<EmployeeDto> {
            return this.map { line ->
                val columns = StringUtils.deleteWhitespace(line).split(",")
                validateColumns(columns)
                EmployeeDto(
                    name = columns[COLUMN_NAME],
                    email = columns[COLUMN_EMAIL],
                    tel = columns[COLUMN_TEL],
                    joined = csvDateToLocalDate(columns[COLUMN_JOINED])
                )
            }
        }

        private fun validateColumns(columns: List<String>) {
            if (columns.size != COLUMN_JOINED + 1) {
                throw DigeloperException(DigeloperError.INVALID_FORMAT_EMPLOYEE_CVS)
            }
        }

        private fun csvDateToLocalDate(csvDate: String): LocalDate {
            val date = csvDate.replace(".", "")
            return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"))
        }

        private const val COLUMN_NAME = 0
        private const val COLUMN_EMAIL = 1
        private const val COLUMN_TEL = 2
        private const val COLUMN_JOINED = 3
    }
}