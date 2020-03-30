package com.kafka.producer.service;

import com.kafka.producer.config.ProducerConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.Resource;

@Service
@Slf4j
public class ProducerService {

    @Resource
    private  KafkaTemplate<String, Object> kafkaTemplate;

//    public ProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }

    public void sendMessages(String topic, Object o) {
        ProducerRecord<String, String> producerRecord = new ProducerRecord(topic, 0, System.currentTimeMillis(), "order", o);
        Producer<String, String> producer = ProducerConfig.getKafkaProducer();
        producer.send(producerRecord, (RecordMetadata metadata , Exception exception) -> {
            if (exception == null) {
                log.info("发送成功,topic:{},partition:{}", metadata.topic(), metadata.partition());
            } else {
                log.info("发送失败, error:{}", exception.getMessage());
            }
        });
        producer.close();

    }

    public void sendKafkaMesaages(String topic, Object o) {
        //异步发送
        ProducerRecord<String, Object> producerRecord = new ProducerRecord(topic, 0, System.currentTimeMillis(), "order", o);
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(producerRecord);
        future.addCallback(result -> log.info("生产者成功发送消息到topic:{},partition:{}的消息,offset:{}", result.getRecordMetadata().topic(), result.getRecordMetadata().partition()),
                falseResult -> log.info("生产者发送失败原因:{}", falseResult.getMessage()));
    }

}
