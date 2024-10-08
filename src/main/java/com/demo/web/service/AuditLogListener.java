package com.demo.web.service;

import java.time.LocalDateTime;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.demo.web.repo.AuditLogRepository;
import com.demo.web.vo.AuditLog;

@Service
public class AuditLogListener {
	
	private static final Logger logger = LogManager.getLogger(AuditLogListener.class);

	@Autowired
	private AuditLogRepository auditLogRepository;

	/*
	 * KafkaListener Method Which Will Look for record in topic - 'user_operations' 
	 */
	@KafkaListener(topics = "user_operations", groupId = "my-group")
	public void handleUserOperations(ConsumerRecord<String, String> record) {
		AuditLog log = new AuditLog();
		String key = record.key();
		String message = record.value().toLowerCase();
		logger.info("Kafka Message Recieved Key:{},Topic:{},Message:{}",key,record.topic(),message);
		if (message.startsWith("create")) {
			log.setAction("CREATE");
			log.setDetails(message);
		} else if (message.startsWith("update")) {
			log.setAction("UPDATE");
			log.setDetails(message);
		} else if (message.startsWith("delete")) {
			log.setAction("DELETE");
			log.setDetails(message);
		}

		log.setTimestamp(LocalDateTime.now());
		auditLogRepository.save(log);
	}

}
