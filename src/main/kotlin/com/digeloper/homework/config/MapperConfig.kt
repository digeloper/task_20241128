package com.digeloper.homework.config

import com.digeloper.homework.mapper.EmployeeMapper
import org.mapstruct.factory.Mappers
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MapperConfig {
    @Bean
    fun employeeMapper(): EmployeeMapper {
        return Mappers.getMapper(EmployeeMapper::class.java)
    }
}