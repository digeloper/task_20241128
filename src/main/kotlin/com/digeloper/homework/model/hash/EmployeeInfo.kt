package com.digeloper.homework.model.hash

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import java.time.LocalDate

@RedisHash(value = "employee")
data class EmployeeInfo(
    @Indexed
    val name: String,

    @Id
    val email: String,

    val tel: String,

    val joined: LocalDate
)