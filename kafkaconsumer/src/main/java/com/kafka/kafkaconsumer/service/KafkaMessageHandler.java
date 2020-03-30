package com.kafka.kafkaconsumer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.kafkaconsumer.entrity.PersonDTO;
import com.kafka.kafkaconsumer.utils.ThreadPoolUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.BatchMessageListener;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 使用batchmessagelistener来监听消息进行消费
 */

@Slf4j
@Service
public class KafkaMessageHandler implements BatchMessageListener<String, String> {


    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onMessage(List<ConsumerRecord<String, String>> consumerRecords) {

        for (ConsumerRecord<String, String> record :consumerRecords) {
            new ThreadPoolUtils().addTask(() -> handler(record));
        }
    }

    public void handler (ConsumerRecord<String, String> record) {
        try{
            PersonDTO personDTO = objectMapper.readValue(record.value(), PersonDTO.class);
            log.info("消费的topic:{},分区:{}, message:{}", record.topic(), record.partition(), personDTO.toString());

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
