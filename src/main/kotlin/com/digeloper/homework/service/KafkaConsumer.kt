package com.digeloper.homework.service

import com.digeloper.homework.exception.DigeloperException
import com.digeloper.homework.model.hash.EmployeeInfo
import com.digeloper.homework.model.message.EmployeeAddedMessage
import com.digeloper.homework.service.KafkaProducer.Companion.GROUP_EMPLOYEE
import com.digeloper.homework.service.KafkaProducer.Companion.TOPIC_ADDED
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux


@Service
class KafkaConsumer(
    @Autowired
    private val employeeInfoService: EmployeeInfoService
) {
    @Throws(DigeloperException::class)
    @KafkaListener(topics = [TOPIC_ADDED], groupId = GROUP_EMPLOYEE)
    fun consume(record: ConsumerRecord<String, EmployeeAddedMessage>) {
        employeeInfoService.run { setEmployeesInfos(Flux.fromIterable(record.value().employees).map { EmployeeInfo(it.name, it.email, it.tel, it.joined) }) }
    }
}