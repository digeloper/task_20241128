package com.digeloper.homework.service

import com.digeloper.homework.model.message.EmployeeAddedMessage
import com.digeloper.homework.model.message.Message
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaProducer(
    @Autowired
    private val kafkaTemplate: KafkaTemplate<String, Message>
) {
    fun sendMessage(message: EmployeeAddedMessage) {
        kafkaTemplate.send(TOPIC_ADDED, message)
    }

    companion object {
        const val TOPIC_ADDED: String = "added"
    }
}