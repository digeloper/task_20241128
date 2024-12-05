package com.digeloper.homework.repository

import com.digeloper.homework.model.document.Employee
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@Repository
interface EmployeeRepository : ReactiveMongoRepository<Employee, String> {
    fun existsByEmailIsIn(emails: List<String>): Mono<Boolean>
}