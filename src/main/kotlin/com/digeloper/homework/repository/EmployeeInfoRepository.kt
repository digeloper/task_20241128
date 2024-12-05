package com.digeloper.homework.repository

import com.digeloper.homework.model.hash.EmployeeInfo
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository


@Repository
interface EmployeeInfoRepository : CrudRepository<EmployeeInfo, String> {
    fun findBy(pageable: Pageable): List<EmployeeInfo>
    fun findByName(name: String): EmployeeInfo?
}