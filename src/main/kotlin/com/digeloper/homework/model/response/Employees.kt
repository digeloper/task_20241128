package com.digeloper.homework.model.response

import com.digeloper.homework.model.dto.EmployeeInfoDto

data class Employees(
    val totalCount: Long,
    val page: Int,
    val pageSize: Int,
    val employees: List<EmployeeInfoDto>
)