package com.kafka.producer.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

public class ProducerConfig {

    private static final String SERVERS = "10.255.15.126:9092,10.255.15.125:9092,10.255.15.124:9092";

    private static final Integer RETRIES = 3;

    private static final String ACKS = "1";

    private static final Integer BATCHSIZE = 323840;

    private static final Integer BUFFERMEMORY = 33554432;



    private  static Properties getProducerConfig() {
        List<String> interceptors = new ArrayList<>();
        interceptors.add("com.kafka.producer.config.TimeStampInterceptor");
        Properties properties = new Properties();
        properties.put(BOOTSTRAP_SERVERS_CONFIG, SERVERS);
        properties.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        properties.put(RETRIES_CONFIG, RETRIES);
        properties.put(ACKS_CONFIG, ACKS);
        properties.put(BATCH_SIZE_CONFIG, BATCHSIZE);
        properties.put(BUFFER_MEMORY_CONFIG, BUFFERMEMORY);
        properties.put(INTERCEPTOR_CLASSES_CONFIG, interceptors);
        return properties;
    }

    public static Producer<String, String> getKafkaProducer() {

        return new KafkaProducer<String, String>(getProducerConfig());
    }
}
