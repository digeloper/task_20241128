package com.digeloper.homework.mapper

import com.digeloper.homework.model.dto.EmployeeInfoDto
import com.digeloper.homework.model.hash.EmployeeInfo
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface EmployeeInfoMapper {
    fun toDto(employeeInfo: EmployeeInfo?) : EmployeeInfoDto?
    fun toModel(employeeInfoDto: EmployeeInfoDto?) : EmployeeInfo?
}