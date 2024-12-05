package com.digeloper.homework.service

import com.digeloper.homework.constants.DigeloperError
import com.digeloper.homework.model.document.Employee
import com.digeloper.homework.exception.DigeloperException
import com.digeloper.homework.mapper.EmployeeMapper
import com.digeloper.homework.model.message.EmployeeAddedMessage
import com.digeloper.homework.model.dto.EmployeeDto
import com.digeloper.homework.repository.EmployeeRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers

@Service
class EmployeeServiceImpl(
    @Autowired
    private val kafkaProducer: KafkaProducer,

    @Autowired
    private val employeeMapper: EmployeeMapper,

    @Autowired
    private val employeeRepository: EmployeeRepository,
) : EmployeeService {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun saveEmployees(employeesDto: Flux<EmployeeDto>): Flux<Employee> {
        return employeesDto
            .collectList()
            .flatMapMany { employeeList ->
                val emails = employeeList.map { it.email }
                employeeRepository.existsByEmailIsIn(emails)
                    .flatMapMany { exists ->
                        if (exists) {
                            Flux.error(DigeloperException(DigeloperError.FAILED_SAVE_DUPLICATE_EMPLOYEES))
                        } else {
                            val employees = Flux.fromIterable(employeeList).map { employeeMapper.toModel(it)!! }
                            employeeRepository.saveAll(employees)
                                .onErrorResume {
                                    logger.error("Failed saving employees : {}", it.message)
                                    Flux.error(DigeloperException(DigeloperError.FAILED_SAVE_EMPLOYEES))
                                }
                                .switchIfEmpty(Flux.error(DigeloperException(DigeloperError.FAILED_SAVE_EMPLOYEES)))
                                .publishOn(Schedulers.boundedElastic())
                                .doOnComplete {
                                    val employeeAddedMessage = employees.collectList().block()!!
                                    kafkaProducer.sendMessage(EmployeeAddedMessage(employeeAddedMessage))
                                }
                        }
                    }
            }
    }
}