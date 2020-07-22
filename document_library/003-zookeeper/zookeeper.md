# zookeeper集群安装



1. 选择合适的版本解压安装，因为有时候会用自己的集群作为开发环境使用；故选择和公司版本一直 3.4.13

   

2. 配置zookeeper文件存储路径，zkData选择喜欢的位置

3. 在zkData目录下创建myid文件

4. 在myid文件填下对应的编号； 分别是1~3  对应hadoop001~hadoop003

5. 配置zoo.cfg文件；

   5.1 拷贝zoo_sample.cfg 修改名字为zoo.cfg

   5.2 修改数据存储路径为zkData

   5.3 增加server,server后面跟的编号是myid中的数字

   > server.1=hadoop001:2888:3888
   > server.2=hadoop002:2888:3888
   > server.3=hadoop003:2888:3888

   

   6. 分别启动Zookeeper

      ```shell
      # 分别启动zookeeper
      zkServer.sh start
      # 查看状态
      zkServer.sh status
      ```

常见客户端shell操作

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





Kafka集群安装：









































