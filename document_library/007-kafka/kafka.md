

### kafka是什么

​	ApacheKafka是*一个分布式流平台*

### 三个关键功能

- 发布和订阅消息，类似于消息队列或企业消息传递系统。
- 具有容错和持久化方式存储消息
- 消息处理

### 两大类应用程序：

- 建立实时流数据管道，以可靠地在系统或应用程序之间获取数据
- 构建实时流应用程序以转换或响应数据流

### 几个概念

- cluster  在一个或多个可以跨越多个数据中心的服务器上作为集群运行

- topic     一个主题可以理解为一个类别的消息，kafka将一个类别消息存储在一个topic中

- record   一个record对应通俗理解的一条消息，由一对k-v 和时间戳组成

### 五个核心API

- Producer API ：允许应用程序向一个或多个Kafka主题发布消息。
- Consumer API：允许应用程序订阅一个或多个主题，并处理为这些主题生成的记录流。
- Streams API：允许应用程序充当流处理器，使用一个或多个主题的输入流并生成一个或多个输出主题的输出流，有效地将输入流转换为输出流。
- Connectors API: 允许构建和运行可重用的生产者或消费者，将Kafka主题连接到现有的应用程序或数据系统。例如，连接到关系数据库的连接器可能捕获对表的每个更改。
- Admin API: 允许管理和检查主题、代理和其他Kafka对象。











## [配置详解](https://kafka.apache.org/21/documentation.html#producerconfigs)





























## 必备的术语

### Exactly Once

### At Least Once

### At Most Once

### idempotence

### offset

### Pagecache

### ISR

LEO

