package com.mutisource.bigdata.config;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

/**
 * @author Jeremy
 * @create 2020 07 24 11:06
 */
public class ZookeeperConfig {
    @Autowired
    private ZookeeperProperties zookeeperProperties;

    /**
     * 生成zookeeper客户端
     *
     * @return
     */
    @Bean
    public ZkClient zkClient() {
        //创建客户端
        return new ZkClient(zookeeperProperties.getServers(), zookeeperProperties.getSessionTimeOut());
    }

}
