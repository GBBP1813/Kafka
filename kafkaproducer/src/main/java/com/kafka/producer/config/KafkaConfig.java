package com.kafka.producer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

/**
 * author gyf
 */

@Configuration
public class KafkaConfig {

    @Value("${kafka.topic.my-topic}")
    private String servers ;

    /**
     * 消息json转化
     * @return
     */
    @Bean
    public RecordMessageConverter jsonConverter() {
        return new StringJsonMessageConverter();
    }

    @Bean
    public NewTopic createTopic() {
        return new  NewTopic(servers, 1, (short)1);
    }
}
