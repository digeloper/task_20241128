package com.digeloper.homework.service

import com.digeloper.homework.constants.DigeloperError
import com.digeloper.homework.exception.DigeloperException
import com.digeloper.homework.model.hash.EmployeeInfo
import com.digeloper.homework.repository.EmployeeInfoRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class EmployeeInfoServiceImpl(
    @Autowired
    private val employeeInfoRepository: EmployeeInfoRepository

) : EmployeeInfoService {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun getEmployeeInfo(name: String): Mono<EmployeeInfo> {
        return Mono.justOrEmpty(employeeInfoRepository.run { findByName(name) })
            .switchIfEmpty(Mono.error(DigeloperException(DigeloperError.NOT_EXIST_EMPLOYEE_INFO)))
    }

    override fun setEmployeesInfos(employeesInfos: Flux<EmployeeInfo>): Flux<EmployeeInfo> {
        return Flux.fromIterable(employeeInfoRepository.run { saveAll(employeesInfos.toIterable()) })
            .switchIfEmpty(Flux.error(DigeloperException(DigeloperError.FAILED_SAVE_EMPLOYEES_INFO)))
    }

    override fun getEmployeesInfos(pageable: Pageable): Flux<EmployeeInfo> {
        return try {
            Flux.fromIterable(employeeInfoRepository.run { findBy(pageable) })  // findBy(pageable)의 반환값은 List<EmployeeInfo>
        } catch (e: Exception) {
            logger.error("Failed loading employeesInfos : {}", e.message)
            Flux.error(DigeloperException(DigeloperError.FAILED_LOAD_EMPLOYEES_INFO))
        }
            .switchIfEmpty(Flux.error(DigeloperException(DigeloperError.NOT_EXIST_EMPLOYEE_INFO)))
    }

    override fun getTotal(): Long {
        return employeeInfoRepository.count()
    }
}