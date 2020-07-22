package com.mutisource.bigdata.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Jeremy
 * @create 2020 07 18 0:47
 */
@Component
@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties("kafka.consumer")
public class KafkaConsumerProperties {
    private String servers;
    private boolean enableAutoCommit;
    private String sessionTimeout;
    private String autoCommitInterval;
    private String autoOffsetReset;
    private long pollTimeOut;

}
