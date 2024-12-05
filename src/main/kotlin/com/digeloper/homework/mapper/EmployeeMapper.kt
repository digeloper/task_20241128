package com.digeloper.homework.mapper

import com.digeloper.homework.model.document.Employee
import com.digeloper.homework.model.dto.EmployeeDto
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface EmployeeMapper {
    fun toDto(employee: Employee?) : EmployeeDto?
    fun toModel(employeeDto: EmployeeDto?) : Employee?
}