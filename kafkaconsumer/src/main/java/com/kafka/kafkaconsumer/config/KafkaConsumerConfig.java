package com.kafka.kafkaconsumer.config;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.BatchMessageListener;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

public class KafkaConsumerConfig {
    private static final String SERVERS = "10.255.15.126:9092,10.255.15.125:9092,10.255.15.124:9092";
    private static final String GROUP_ID = "test_group";
    private static final String ENABLE_AUTO_COMMIT = "true";
    private static final String AUTO_COMMIT_INTERVAL = "2000";
    private static final String SESSION_TIMEOUT = "60000";
    private static final String AUTO_OFFSET_RESET = "earliest";
    private static final String MAX_POLL_RECORDS = "100";

    private static Map<String, Object> consumerConfigs() {
        Map<String, Object> propsMap = new HashMap<>();
        propsMap.put(BOOTSTRAP_SERVERS_CONFIG, SERVERS);
        propsMap.put(GROUP_ID_CONFIG, GROUP_ID);
        propsMap.put(ENABLE_AUTO_COMMIT_CONFIG, ENABLE_AUTO_COMMIT);
        propsMap.put(AUTO_COMMIT_INTERVAL_MS_CONFIG, AUTO_COMMIT_INTERVAL);
        propsMap.put(SESSION_TIMEOUT_MS_CONFIG, SESSION_TIMEOUT);
        propsMap.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(AUTO_OFFSET_RESET_CONFIG, AUTO_OFFSET_RESET);
        propsMap.put(MAX_POLL_RECORDS_CONFIG, MAX_POLL_RECORDS);
        return propsMap;
    }

    public static KafkaMessageListenerContainer<String, String> getContainer(String consumerId, BatchMessageListener batchMessageListener, String... topic) {
        ContainerProperties containerProperties = new ContainerProperties(topic);
        containerProperties.setMessageListener(batchMessageListener);
        if (StringUtils.isEmpty(consumerId)) {
            containerProperties.setGroupId(GROUP_ID);
        } else {
            containerProperties.setGroupId(consumerId);
        }
        DefaultKafkaConsumerFactory<String, String> cf = new DefaultKafkaConsumerFactory<>(consumerConfigs());
        KafkaMessageListenerContainer<String, String> kafkaMessageListenerContainer = new KafkaMessageListenerContainer<>(cf, containerProperties);
        return kafkaMessageListenerContainer;
    }

}
