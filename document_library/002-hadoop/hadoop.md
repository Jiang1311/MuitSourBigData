

# 阿里云ESC搭建hadoop集群




## 前置

​	购买至少三台服务器，为了节约成本借了两个账号买了三台同一区域的服务器，安装的是 centos7；因此设计到不同账号相同地域之间通讯问题，阿里给了解决方案，详情参考： [云企网](https://help.aliyun.com/document_detail/128508.html?spm=a2c4g.11186623.2.10.798818c0oNp97J)

在目录000-ESC上会整理一版，ESC相关配置和使用的文档，主要是我这边配置的时候用到的坑吧

当然可以通过询问客服，提交工单（比较耗时，大概三个小时左右才会回复）的方式解决

## 集群相关信息

| 节点    | 私网ip               | 公网ip | 用户名-密码 |
| ------- | -------------------- | ------ | ----------- |
| node001 | 1.非注册地址         |        | 普通用户    |
| node002 | 2.为组织机构内部使用 |        | 普通用户    |
| node003 |                      |        | 普通用户    |

**公私网简述**

> 公网IP世界只有一个，私网IP可以重复，但是在一个局域网内不能重复访问互联网是需要IP地址的，IP地址又分为公网IP和私网IP，访问互联网需要公网IP作为身份的标识，而私网IP则用于局域网，在公网上是不能使用私网IP地址来实现互联网访问的。公网IP在全球内是唯一的
>
> 简单点说：
>
> 私网是你购买的服务器之间交互用，例如:配置主机名映射文件 /etc/hosts 使用的是私网ip
>
> 公网用来访问你网络应用的,如访问hdfs的namenode web端时，使用公网ip：50070



## 集群规划

建议先阅读下官网集群搭建模块：[Hadoop Cluster Setup](https://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-common/ClusterSetup.html)

参考官方文档给出的建议和实际购买服务器内存，做如下划分：

主要是 NodeManager ResourceManager SecondaryNameNode 尽量别再一台上

> |      | node1 （2核8G） | node2（2核4G）  | node3（1核4G）    |
> | ---- | --------------- | --------------- | ----------------- |
> | HDFS | NameNode（NN）  |                 | SecondaryNameNode |
> |      | DataNode        | DataNode        | DataNode          |
> | Yarn |                 | ResourceManager |                   |
> |      | NodeManager     | NodeManager     | NodeManager       |

按照上表规划进行

## 集群搭建



### 步骤一：安装Java与Hadoop

Prerequisites

- Install Java. See the [Hadoop Wiki](http://wiki.apache.org/hadoop/HadoopJavaVersions) for known good versions.

- 安装 Java（我使用的是Java8），根据wiki选择适合的版本

- Download a stable version of Hadoop from Apache mirrors.      

- 下载安装Hadoop（我选的是2.7.2 ，我选择的是与公司的一致，没做其他方面考虑）

- 安装很简单    --- 1.解压； 2.设置环境变量  3. sources profile 文件即可

  上述是官网给出去必要条件，当然除了上述以外还需要进行linux的常规配置；

  1. 关闭防火墙，下面命令是针对centos7

  ```shell
  # 查看防火墙状态
  firewall-cmd --state
  # 停止firewall
  systemctl stop firewalld.service
  # 禁止firewall开机启动
  systemctl disable firewalld.service
  ```

  **注意**:如果小伙伴和我一样用的是esc或者其他类似租借的云服务器，看下他们是否有安全组策略； 以我的为例，需要配置端口访问策略； 阿里云esc： 云服务器管理控制台 ---> 实例 ---> 点击需要配置的实例id，进入 ---> 选择右边菜单栏最后一个本实例安全组 ---> 选择配置规则，按需配置，

  常用端口号在下个节点有个列表可以看下，在000-ESC目录下，放了一份我导出的规则，不想一个个配置的话可以下载后，直接导入

  2. 修改主机名，配置域名映射

  ```shell
  # 查看主机名
  hostname 
  # 修改主机名
  hostnamectl set-hostname  hadoop001 
  # 查看主机名
  hostname
  
  # 配置域名映射
  vim /etc/hosts
  # 有个localhost 按照它的格式来，一般第一个为ip， 第二个域名，第三个为别名
  # 重启下服务
  shutdown 
  
  ```

  

### 步骤二：配置相应的文件

*Hadoop’s Java configuration is driven by two types of important configuration files:*

- Read-only default configuration - `core-default.xml`, `hdfs-default.xml`, `yarn-default.xml` and `mapred-default.xml`.      
- default 文件在源码的只读文件，理解为SpringBoot的默认配置即可
- *Site-specific configuration - `etc/hadoop/core-site.xml`, `etc/hadoop/hdfs-site.xml`, `etc/hadoop/yarn-site.xml` and `etc/hadoop/mapred-site.xml`.*
- site文件，为用户提供特定配置使用的文件，具体配置参考下面的文件
-  Additionally, you can control the Hadoop scripts found in the bin/ directory of the distribution, by setting site-specific values via the `etc/hadoop/hadoop-env.sh` and `etc/hadoop/yarn-env.sh`.
- 除了默认配置文件与site文件以外，hadoop还给我提供 env.sh`文件，用来设置bin目录下脚本执行的特定参数；（随便提一嘴：bin目录下所执行的脚本，仔细看下最终都逃不开 exec "$JAVA" ；所以在上述的两个文件中，最低也得配置java的环境变量，不然bin里面的执行到java可就找不到命令啦，其他的配置就各显神通啦）

使用表中所示的配置选项配置各个守护进程:

| NameNode                      | HDFS_NAMENODE_OPTS          | core-site.xml   |
| :---------------------------- | :-------------------------- | --------------- |
| DataNode                      | HDFS_DATANODE_OPTS          | hdfs-site.xml   |
| Secondary NameNode            | HDFS_SECONDARYNAMENODE_OPTS | hdfs-site.xml   |
| ResourceManager               | YARN_RESOURCEMANAGER_OPTS   | yarn-site.xm    |
| NodeManager                   | YARN_NODEMANAGER_OPTS       | yarn-site.xm    |
| WebAppProxy                   | YARN_PROXYSERVER_OPTS       | yarn-site.xm    |
| Map Reduce Job History Server | MAPRED_HISTORYSERVER_OPTS   | mapred-site.xml |
| Daemon                        | Environment Variable        | Resource File   |

#### core-site.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<!--
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License. See accompanying LICENSE file.
-->

<!-- Put site-specific property overrides in this file. -->

<configuration>
  <!-- 指定HDFS中NameNode的地址 -->
  <property>
        <name>fs.defaultFS</name>
       <value>hdfs://hadoop001:9000</value>
  </property>

  <!-- 指定Hadoop运行时产生文件的存储目录 -->
  <property>
        <name>hadoop.tmp.dir</name>
        <value>/opt/module/hadoop-2.7.2/data/tmp</value>
  </property>

</configuration>

```

#### hdfs-site.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<!--
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License. See accompanying LICENSE file.
-->

<!-- Put site-specific property overrides in this file. -->

<configuration>

		<!--配置副本数量，默认为3，如果你的副本数为3也可以不配置 -->
        <property>
           <name>dfs.replication</name>
           <value>3</value>
        </property>

        <!-- 指定Hadoop辅助名称节点主机配置 -->
        <property>
          <name>dfs.namenode.secondary.http-address</name>
          <value>hadoop003:50090</value>
        </property>

</configuration>           
```
#### mapred-site.xml
```xml

<?xml version="1.0"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<!--
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License. See accompanying LICENSE file.
-->

<!-- Put site-specific property overrides in this file. -->

<configuration>
        <!-- 指定MR运行在Yarn上 -->
        <property>
           <name>mapreduce.framework.name</name>
           <value>yarn</value>
        </property>

</configuration>
```
#### yarn-size.xml
```xml
	<?xml version="1.0"?>
<!--
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License. See accompanying LICENSE file.
-->
<configuration>

<!-- Site specific YARN configuration properties -->

        <!-- Reducer获取数据的方式 -->
        <property>
                <name>yarn.nodemanager.aux-services</name>
                <value>mapreduce_shuffle</value>
        </property>

        <!-- 指定YARN的ResourceManager的地址 -->
        <property>
                <name>yarn.resourcemanager.hostname</name>
                <value>hadoop002</value>
        </property>

</configuration>
```

#### env.sh文件

```shell
# The java implementation to use. 放开注解，修改为自己的JAVA_HOME即可
#export JAVA_HOME=${JAVA_HOME}

```

### 步骤三：启动集群

#### 单点启动：

```shell 
# 1 格式化NameNode 注意知识针对第一次启动集群
hadoop namenode -format
# 2. 启动NameNode
bash hadoop-deamon.sh  start namenode
# 2.1 jps 看下NameNode是否启动成功
# 3. 分别启动不同节点下的datanode
bash hadoop-deamon.sh start datanode
# 4. 启动yarn
bash start-yarn.sh

```

ip:50070 查看NameNode信息
如下图显示：

![NameNode WebUI](https://raw.githubusercontent.com/Jiang1311/MuitSourBigData/master/document_library/assert/hadoop/50070.png)

ip:8088 yarn-webui 
如下图：
![yarn](https://raw.githubusercontent.com/Jiang1311/MuitSourBigData/master/document_library/assert/hadoop/8088.png)

一切正常的话，说明已经配置好啦；

#### 配置群起

单点启动既耗时，由容易出错，那有没有什么好的方法了，当然有啦

**ssh免密**

```shell
# 先配置ssh登录，又称非login登录
ssh (目标主机)ip
yes
# 如果没有预装ssh
yum install ssh 
# 无密登录原理和gitHub ssh登录一样，需要设置秘钥对，所以需要你在主节点生成秘钥对
ssh-keygen -t rsa
# 上述命令简单理解为： 使用rsa非对称加密算法，生成一对秘钥，秘钥的位置在/home/用户/.ssh目录下
# .开头的说明是隐藏文件，ll -a 可以查看所有文件， 
ssh-copy-id 目标主机（包括主机当前的主机，为啥呢； 试试ssh 加当前ip就知道啦，主要是两种不同的登录方式）
# 测试
ssh ip （不需要输入密码啦）
```

注意：

还需要在**node2**上采用**普通账号**配置一下无密登录（因为yarn的resourcemanager配置在node2上根据）

**群起**

node1节点

start-hdfs.sh

node2节点

start-yarn.sh


### 常用的端口配置

#### 2.1 HDFS端口

 

| 参数                      | 描述                          | 默认  | 配置文件       | 例子值              |
| ------------------------- | ----------------------------- | ----- | -------------- | ------------------- |
| fs.default.name namenode  | namenode RPC交互端口          | 8020  | core-site.xml  | hdfs://master:8020/ |
| dfs.http.address          | NameNode web管理端口          | 50070 | hdfs- site.xml | 0.0.0.0:50070       |
| dfs.datanode.address      | datanode　控制端口            | 50010 | hdfs -site.xml | 0.0.0.0:50010       |
| dfs.datanode.ipc.address  | datanode的RPC服务器地址和端口 | 50020 | hdfs-site.xml  | 0.0.0.0:50020       |
| dfs.datanode.http.address | datanode的HTTP服务器和端口    | 50075 | hdfs-site.xml  | 0.0.0.0:50075       |

 

#### 2.2 MR端口

| 参数                             | 描述                   | 默认  | 配置文件        | 例子值              |
| -------------------------------- | ---------------------- | ----- | --------------- | ------------------- |
| mapred.job.tracker               | job-tracker交互端口    | 8021  | mapred-site.xml | hdfs://master:8021/ |
| job                              | tracker的web管理端口   | 50030 | mapred-site.xml | 0.0.0.0:50030       |
| mapred.task.tracker.http.address | task-tracker的HTTP端口 | 50060 | mapred-site.xml | 0.0.0.0:50060       |

 

#### 2.3 其它端口

| 参数                       | 描述                           | 默认  | 配置文件      | 例子值        |
| -------------------------- | ------------------------------ | ----- | ------------- | ------------- |
| dfs.secondary.http.address | secondary NameNode web管理端口 | 50090 | hdfs-site.xml | 0.0.0.0:50090 |




