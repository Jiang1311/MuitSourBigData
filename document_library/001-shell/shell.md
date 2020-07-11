# Linux



```shell
# 查看主机名: 
hoastname

# 修改主机名 
vim /etc/sysconfig/network
# 重启服务器后生效
shutdown -r 
# 方法二  名称
hostnameclt set-hostname name

# 创建用户 配置root权限
useradd admin
passwd admin

# 配置ront权限
vim /etc/sudoers
```



## 安装JDK

查询是否安装Java软件：

```shell
 rpm -qa | grep java

sudo rpm -e 软件包

which java

# 配置环境变量 
# JAVA_HOME
export JAVA_HOME=/opt/module/jdk1.8.0_144
export PATH=$PATH:$JAVA_HOME/bin
# Hadoop_Home
export HADOOP_HOME=/opt/module/hadoop
export	PATH=$PATH:$HADOOP_HOME/bin
export	PATH=$PATH:$HADOOP_HOME/bin

source profile
```



## SSH免密登录

```shell
#  在当前用户的家目录下生成，生成秘钥对 
ssh-keygen -t ras
# 将公钥拷贝到要免密登录的目标机器上
ssh-copy-id (目标主机)
```

**.ssh 文件夹含义**

known_hosts  ---->  			记录 ssh 访问过计算机的公钥(public key)

id_rsa 			-----> 			 生成的私钥

id_rsa.pub 	------> 			生成的公钥

authorized_keys  ------> 	存放授权过得无密登录服务器公钥

	 注意： linux中隐藏文件都是以.开头的，查看隐藏文件的命令为： ll -a 即可







## Shell 脚本

### hadoop等脚本使用命令


```shell
# 用于查找文件。
which

# 获取文件所在的目录
sh dirname filenaem

# 获取当前执行的脚本文件的全路径
${BASH_SOURCE-$0} 

# 与上面命令相等，bash环境
"${BASH_SOURCE}"

# 用于获取路径中的文件名
basename


```



### $ 用户总结

```shell 
# $n n是数字,获取
$0 
$1
```