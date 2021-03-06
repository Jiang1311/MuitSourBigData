

# Zookeeper

### zookeeper概述

####  分布式

分布式应用可以在给定时间（同时）在网络中的多个系统上运行，通过协调它们以快速有效的方式完成特定任务。通常来说，对于复杂而耗时的任务，非分布式应用（运行在单个系统中）需要几个小时才能完成，而分布式应用通过使用所有系统涉及的计算能力可以在几分钟内完成。

通过将分布式应用配置为在更多系统上运行，可以进一步减少完成任务的时间。分布式应用正在运行的一组系统称为集群，而在集群中运行的每台机器被称为节点。

分布式应用有两部分， Server（服务器） 和 Client（客户端） 应用程序。服务器应用程序实际上是分布式的，并具有通用接口，以便客户端可以连接到集群中的任何服务器并获得相同的结果。 客户端应用程序是与分布式应用进行交互的工具。

#### 分布式应用的优点

- **可靠性** - 单个或几个系统的故障不会使整个系统出现故障。
- **可扩展性** - 可以在需要时增加性能，通过添加更多机器，在应用程序配置中进行微小的更改，而不会有停机时间。
- **透明性** - 隐藏系统的复杂性，并将其显示为单个实体/应用程序。

#### 分布式应用的挑战

- **竞争条件** - 两个或多个机器尝试执行特定任务，实际上只需在任意给定时间由单个机器完成。例如，共享资源只能在任意给定时间由单个机器修改。
- **死锁** - 两个或多个操作等待彼此无限期完成。
- **不一致** - 数据的部分失败。

#### CAP

CAP原则又称CAP定理，指的是在一个分布式系统中，[一致性](https://baike.baidu.com/item/一致性/9840083)（Consistency）、[可用性](https://baike.baidu.com/item/可用性/109628)（Availability）、分区容错性（Partition tolerance）。CAP 原则指的是，这三个要素最多只能同时实现两点，不可能三者兼顾。

一致性（C）：在[分布式系统](https://baike.baidu.com/item/分布式系统/4905336)中的所有数据备份，在同一时刻是否同样的值。（等同于所有节点访问同一份最新的数据副本）

可用性（A）：在集群中一部分节点故障后，[集群](https://baike.baidu.com/item/集群/5486962)整体是否还能响应[客户端](https://baike.baidu.com/item/客户端/101081)的读写请求。（对数据更新具备高可用性）

分区容忍性（P）：以实际效果而言，分区相当于对通信的时限要求。系统如果不能在时限内达成数据一致性，就意味着发生了分区的情况，必须就当前操作在C和A之间做出选择。



## Zookeeper基础

### ZooKeeper架构的组件



| Client（客户端） | 客户端，我们的分布式应用集群中的一个节点，从服务器访问信息。对于特定的时间间隔，每个客户端向服务器发送消息以使服务器知道客户端是活跃的。类似地，当客户端连接时，服务器发送确认码。如果连接的服务器没有响应，客户端会自动将消息重定向到另一个服务器。 |
| ---------------- | ------------------------------------------------------------ |
| Server（服务器） | 服务器，我们的ZooKeeper总体中的一个节点，为客户端提供所有的服务。向客户端发送确认码以告知服务器是活跃的。 |
| Ensemble         | ZooKeeper服务器组。形成ensemble所需的最小节点数为3。         |
| Leader           | 服务器节点，如果任何连接的节点失败，则执行自动恢复。Leader在服务启动时被选举。 |
| Follower         | 跟随leader指令的服务器节点。                                 |

### ZooKeeper数据模型

ZooKeeper数据模型中的每个znode都维护着一个 stat 结构。一个stat仅提供一个znode的元数据。它由版本号，操作控制列表(ACL)，时间戳和数据长度组成。

- **版本号** - 每个znode都有版本号，这意味着每当与znode相关联的数据发生变化时，其对应的版本号也会增加。当多个zookeeper客户端尝试在同一znode上执行操作时，版本号的使用就很重要。
- **操作控制列表(ACL)** - ACL基本上是访问znode的认证机制。它管理所有znode读取和写入操作。
- **时间戳** - 时间戳表示创建和修改znode所经过的时间。它通常以毫秒为单位。ZooKeeper从“事务ID"(zxid)标识znode的每个更改。**Zxid** 是唯一的，并且为每个事务保留时间，以便你可以轻松地确定从一个请求到另一个请求所经过的时间。
- **数据长度** - 存储在znode中的数据总量是数据长度。你最多可以存储1MB的数据。

### Znode的类型

Znode被分为持久（persistent）节点，顺序（sequential）节点和临时（ephemeral）节点。

- **持久节点** - 即使在创建该特定znode的客户端断开连接后，持久节点仍然存在。默认情况下，除非另有说明，否则所有znode都是持久的。
- **临时节点** - 客户端活跃时，临时节点就是有效的。当客户端与ZooKeeper集合断开连接时，临时节点会自动删除。因此，只有临时节点不允许有子节点。如果临时节点被删除，则下一个合适的节点将填充其位置。临时节点在leader选举中起着重要作用。
- **顺序节点** - 顺序节点可以是持久的或临时的。当一个新的znode被创建为一个顺序节点时，ZooKeeper通过将10位的序列号附加到原始名称来设置znode的路径。例如，如果将具有路径 **/myapp** 的znode创建为顺序节点，则ZooKeeper会将路径更改为 **/myapp0000000001** ，并将下一个序列号设置为0000000002。如果两个顺序节点是同时创建的，那么ZooKeeper不会对每个znode使用相同的数字。顺序节点在锁定和同步中起重要作用。

### Sessions（会话）

会话对于ZooKeeper的操作非常重要。会话中的请求按FIFO顺序执行。一旦客户端连接到服务器，将建立会话并向客户端分配**会话ID** 。

客户端以特定的时间间隔发送**心跳**以保持会话有效。如果ZooKeeper集合在超过服务器开启时指定的期间（会话超时）都没有从客户端接收到心跳，则它会判定客户端死机。

会话超时通常以毫秒为单位。当会话由于任何原因结束时，在该会话期间创建的临时节点也会被删除。

### Watches（监视）

监视是一种简单的机制，使客户端收到关于ZooKeeper集合中的更改的通知。客户端可以在读取特定znode时设置Watches。Watches会向注册的客户端发送任何znode（客户端注册表）更改的通知。

Znode更改是与znode相关的数据的修改或znode的子项中的更改。只触发一次watches。如果客户端想要再次通知，则必须通过另一个读取操作来完成。当连接会话过期时，客户端将与服务器断开连接，相关的watches也将被删除。



# Zookeeper 工作流





| 组件                              | 描述                                                         |
| --------------------------------- | ------------------------------------------------------------ |
| 写入（write）                     | 写入过程由leader节点处理。leader将写入请求转发到所有znode，并等待znode的回复。如果一半的znode回复，则写入过程完成。 |
| 读取（read）                      | 读取由特定连接的znode在内部执行，因此不需要与集群进行交互。  |
| 复制数据库（replicated database） | 它用于在zookeeper中存储数据。每个znode都有自己的数据库，每个znode在一致性的帮助下每次都有相同的数据。 |
| Leader                            | Leader是负责处理写入请求的Znode。                            |
| Follower                          | follower从客户端接收写入请求，并将它们转发到leader znode。   |
| 请求处理器（request processor）   | 只存在于leader节点。它管理来自follower节点的写入请求。       |
| 原子广播（atomic broadcasts）     | 负责广播从leader节点到follower节点的变化。                   |



# Zookeeper leader选举



















# zookeeper集群安装



> 1. 选择合适的版本解压安装，因为有时候会用自己的集群作为开发环境使用；故选择和公司版本一直 3.4.13
>
>    
>
> 2. 配置zookeeper文件存储路径，zkData选择喜欢的位置
>
> 3. 在zkData目录下创建myid文件
>
> 4. 在myid文件填下对应的编号； 分别是1~3  对应hadoop001~hadoop003
>
> 5. 配置zoo.cfg文件；
>
>    5.1 拷贝zoo_sample.cfg 修改名字为zoo.cfg
>
>    5.2 修改数据存储路径为zkData
>
>    5.3 增加server,server后面跟的编号是myid中的数字
>
>    > server.1=hadoop001:2888:3888
>    > server.2=hadoop002:2888:3888
>    > server.3=hadoop003:2888:3888
>
>    
>
>    6. 分别启动Zookeeper
>
>       ```shell
>       # 分别启动zookeeper
>       zkServer.sh start
>       # 查看状态
>       zkServer.sh status
>       ```
>
> 常见客户端shell操作
>

| 命令             | 含义                                             | 范例         | 注意                                                      |
| ---------------- | ------------------------------------------------ | ------------ | --------------------------------------------------------- |
| help             | 显示所有操作命令                                 |              | 所有客户端命令需要在使用 zkCli.sh；进入客户终端窗口后输入 |
| ls path [watch]  | 使用 ls 命令来查看当前znode中所包含的内容        | ls/weather   |                                                           |
| ls2 path [watch] | 查看当前节点数据并能看到更新次数等数据           | ls2 /weather |                                                           |
| create           | 普通创建-s  含有序列-e  临时（重启或者超时消失） |              | create节点是，后面需要跟相关信息否则创建失败              |
| get path [watch] | 获得节点的值                                     |              |                                                           |
| set              | 设置节点的具体值                                 |              |                                                           |
| stat             | 查看节点状态                                     |              |                                                           |
| delete           | 删除节点                                         |              |                                                           |
| rmr              | 递归删除节点                                     |              |                                                           |











































