package com.mutisource.bigdata.kafka;

import com.mutisource.bigdata.config.KafkaConsumerProperties;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author Jeremy
 * @create 2020 07 18 0:52
 */
@SpringBootTest
public class CustomConsumerTest {

    //消费者配置实体类
    @Autowired
    private KafkaConsumerProperties kafkaConsumerProperties;

    //kafka消费者配置参数
    @Resource
    private Properties props;

    @Test
    public void consumerTest(){
        props.put("bootstrap.servers", "39.107.107.120:9092");
        props.put("group.id", "test");//消费者组，只要group.id相同，就属于同一个消费者组
        props.put("enable.auto.commit", "false");//关闭自动提交offset
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("aFileDay"));
        ConsumerRecords<String, String> consumerRecords = consumer.poll(kafkaConsumerProperties.getPollTimeOut());
        int count = consumerRecords.count();
        System.out.println(count);

    }

}
