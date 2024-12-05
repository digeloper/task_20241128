package com.digeloper.homework.model.message

import com.digeloper.homework.model.document.Employee
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeName

@JsonTypeName("EmployeeAddedMessage")
data class EmployeeAddedMessage @JsonCreator constructor(
    @JsonProperty("employees") val employees: List<Employee>
) : Message()