package com.mutisource.bigdata.config;

import com.mutisource.bigdata.config.KafkaConsumerProperties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.Properties;

/**
 * @author Jeremy
 * @create 2020 07 18 0:43
 */
public class kafkaConsumerConfig {
    /**
     *
     */
    @Autowired
    private KafkaConsumerProperties kafkaConsumerProperties;

    /**
     * 设置kafka消费者配置参数
     * @return
     */
    @Bean
    public Properties consumerProperties(){
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaConsumerProperties.getServers());
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,kafkaConsumerProperties.isEnableAutoCommit());
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,kafkaConsumerProperties.getAutoCommitInterval());
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,kafkaConsumerProperties.getSessionTimeout());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,kafkaConsumerProperties.getAutoOffsetReset());
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return properties;
    }
}
