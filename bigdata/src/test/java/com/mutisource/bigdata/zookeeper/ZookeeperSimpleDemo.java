package com.mutisource.bigdata.zookeeper;

import com.mutisource.bigdata.config.ZookeeperConfig;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author Jeremy
 * @create 2020 07 24 11:09
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ZookeeperSimpleDemo {

    /**
     * zookeeper客户端
     */
    @Resource
    private ZookeeperConfig zookeeperConfig;

    @Test
    public void zkCreate(){
        ZkClient zkClient = zookeeperConfig.zkClient();
        String result = zkClient.create("/test/zkClient", "test1", CreateMode.EPHEMERAL);

        System.out.println(result);

    }
}
