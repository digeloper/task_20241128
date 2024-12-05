package com.digeloper.homework.service

import com.digeloper.homework.model.document.Employee
import com.digeloper.homework.model.dto.EmployeeDto
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
interface EmployeeService {
    fun saveEmployees(employees: Flux<EmployeeDto>): Flux<Employee>
}