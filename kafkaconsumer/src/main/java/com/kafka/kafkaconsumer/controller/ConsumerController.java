package com.kafka.kafkaconsumer.controller;

import com.kafka.kafkaconsumer.config.KafkaConsumerConfig;
import com.kafka.kafkaconsumer.service.KafkaMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/consumer")
public class ConsumerController {

    @Value("${kafka.topic.my-topic}")
    private String myTopic;

    @Autowired
    private KafkaMessageHandler kafkaMessageHandler;

    @PostMapping()
    public void send(@RequestParam("name")String name) {
        KafkaMessageListenerContainer<String, String> container = KafkaConsumerConfig.getContainer(null, kafkaMessageHandler, myTopic);
        container.start();
    }

}
