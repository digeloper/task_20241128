package com.digeloper.homework.controller

import com.digeloper.homework.exception.DigeloperException
import com.digeloper.homework.mapper.EmployeeMapper
import com.digeloper.homework.model.dto.EmployeeDto
import com.digeloper.homework.service.EmployeeService
import com.digeloper.homework.helper.CSVHelper.Companion.csvToEmployeesDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping(value = ["api"])
class EmployeeCommandController(
    @Autowired
    private val employeeMapper: EmployeeMapper,

    @Autowired
    private val employeeService: EmployeeService
) {
    @PostMapping(value = ["/employee"], consumes = ["text/csv"])
    @Throws(DigeloperException::class)
    fun saveCSVEmployees(
        @RequestBody employeesCSV: Flux<String>
    ): ResponseEntity<Mono<MutableList<EmployeeDto>>> {
        val employeesDto = employeesCSV.csvToEmployeesDto()
        val savedEmployeesDto = saveEmployees(employeesDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployeesDto)
    }

    @PostMapping(value = ["/employee"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    @Throws(DigeloperException::class)
    fun saveJsonEmployees(
        @RequestBody employeesDto: Flux<EmployeeDto>
    ): ResponseEntity<Mono<MutableList<EmployeeDto>>> {
        val savedEmployeesDto = saveEmployees(employeesDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployeesDto)
    }

    private fun saveEmployees(employees: Flux<EmployeeDto>): Mono<MutableList<EmployeeDto>> {
        return employeeService.saveEmployees(employees)
            .map { employeeMapper.toDto(it)!! }
            .collectList()
    }
}