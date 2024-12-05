package com.digeloper.homework.service

import com.digeloper.homework.model.hash.EmployeeInfo
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
interface EmployeeInfoService {
    fun getEmployeeInfo(name: String): Mono<EmployeeInfo>
    fun setEmployeesInfos(employeesInfos: Flux<EmployeeInfo>): Flux<EmployeeInfo>
    fun getEmployeesInfos(pageable: Pageable): Flux<EmployeeInfo>
    fun getTotal(): Long
}