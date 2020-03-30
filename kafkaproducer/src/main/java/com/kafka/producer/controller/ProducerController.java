package com.kafka.producer.controller;

import com.kafka.producer.entity.PersonDTO;
import com.kafka.producer.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/send")
public class ProducerController {

    @Value("${kafka.topic.my-topic}")
    String myTopic;

    @Autowired
    private ProducerService producerService;

    @PostMapping
    public void send(@RequestParam("name")String name) {

        producerService.sendKafkaMesaages(myTopic,  new PersonDTO(name, new AtomicInteger().addAndGet(1)));
        producerService.sendMessages(myTopic, new PersonDTO(name, new AtomicInteger().addAndGet(2)));

    }

}
