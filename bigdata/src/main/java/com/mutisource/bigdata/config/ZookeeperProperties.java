package com.mutisource.bigdata.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * @author Jeremy
 * @create 2020 07 24 10:54
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@ConfigurationPropertiesScan("zookeeper")
public class ZookeeperProperties {

    /**
     * 连接地址
     */
    private String servers;

    /**
     * session会话超时
     */
    private int sessionTimeOut;

    /**
     * bigData 根节点 --- 整个大数据项目
     */
    private String rootNode;

    /**
     * 项目 节点 -- 流程项目节点 如： 收集、入库、清洗、离线计算、实时计算等
     */
    private String projectNode;

    /**
     * 产品 -- 数据库配置，每一个产品，代表一个业务常见：如 注册、登录、支付等
     */
    private String productNodes;

    /**
     * 状态主节点
     */
    private String masterNode;

    /**
     * 线下节点
     */
    private String onlineNodes;



}
