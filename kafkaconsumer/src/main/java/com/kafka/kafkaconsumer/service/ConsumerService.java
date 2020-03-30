package com.kafka.kafkaconsumer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.kafkaconsumer.entrity.PersonDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConsumerService {


    @Value("${kafka.topic.my-topic}")
    private String groupId;

    private final ObjectMapper objectMapper = new ObjectMapper();

    //@KafkaListener(topics = {"${kafka.topic.my-topic}"})
    public void consumeMessage(ConsumerRecord<String, String> consumerRecord) {

        try{
            PersonDTO personDTO = objectMapper.readValue(consumerRecord.value(), PersonDTO.class);
            log.info("消费的topic:{},分区:{}, message:{}", consumerRecord.topic(), consumerRecord.partition(), personDTO.toString());

        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
