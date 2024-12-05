package com.digeloper.homework.model.document

import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import java.time.LocalDateTime

@Document
data class Employee(
    @NotNull
    val name: String,

    @Id
    @Length(min = 3, max = 50)
    val email: String,

    @NotNull
    val tel: String,

    @NotNull
    val joined: LocalDate,

    @CreatedDate
    var createdAt: LocalDateTime? = null,

    @LastModifiedDate
    var modifiedAt: LocalDateTime? = null
)