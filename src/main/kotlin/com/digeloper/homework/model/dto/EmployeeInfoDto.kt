package com.digeloper.homework.model.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class EmployeeInfoDto @JsonCreator constructor(
    @JsonProperty("name") val name: String,
    @JsonProperty("email") val email: String,
    @JsonProperty("tel") val tel: String,
    @JsonProperty("joined") val joined: LocalDate
)