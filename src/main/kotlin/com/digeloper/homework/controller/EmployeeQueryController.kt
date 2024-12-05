package com.digeloper.homework.controller

import com.digeloper.homework.exception.DigeloperException
import com.digeloper.homework.mapper.EmployeeInfoMapper
import com.digeloper.homework.model.dto.EmployeeInfoDto
import com.digeloper.homework.model.response.Employees
import com.digeloper.homework.service.EmployeeInfoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping(value = ["api"])
class EmployeeQueryController(
    @Autowired
    private val employeeInfoService: EmployeeInfoService,

    @Autowired
    private val employeeInfoMapper: EmployeeInfoMapper
) {
    @GetMapping(value = ["/employee"])
    @Throws(DigeloperException::class)
    fun employees(
        @RequestParam(value = "page", required = true) page: Int,
        @RequestParam(value = "pageSize", required = true) pageSize: Int
    ): Mono<Employees> {
        val pageable = PageRequest.of(page, pageSize)
        return employeeInfoService.getEmployeesInfos(pageable)
            .map { employeeInfoMapper.toDto(it)!! }
            .collectList()
            .map { Employees(employeeInfoService.getTotal(), page, pageSize, it)}
    }

    @GetMapping(value = ["/employee/{name}"])
    @Throws(DigeloperException::class)
    fun employee(@PathVariable name: String): Mono<EmployeeInfoDto> {
        return employeeInfoService.getEmployeeInfo(name)
            .map { employeeInfoMapper.toDto(it)!! }
    }
}