### 1.共享目录搭建

参考： https://blog.csdn.net/FightingOning/article/details/80471265

####  1.1 创建用户

1. 在开始菜单点击“右键”.

2. 选中弹出的“计算机管理”.

3. 进入“系统工具”->“本地用户和组”->“用户”

4. 空白处右键，“新用户”

5. 填写“用户名”，“密码”，“确认密码”，去掉“用户下次登陆时需去掉密码”

6. 勾选“用户不能更改密码”，“密码永不过期”， 点击“创建”，“关闭”

   
#### 1.2 公共目录设置



​	右键点击需要共享的文件夹->“属性”

 2. 点击“共享”、“高级共享”
 3. 在高级共享窗口中，勾选共享此文件夹，点击“权限”
 4. 在权限窗口中，点击“添加”，打开的选择用户或组窗口
 5. 在输入对象名称来选择栏中输入我们刚才创建的新用户名称,点击“确定”
 6. 点击新用户名称，在“允许”项下点击“完全控制”打勾，点击“确定”
 7. 确定，关闭。



#### 1.3 共享目录映射

1. 进入资源管理器，点击“计算机”，“映射网络驱动器”
2. 在文件夹输入框输入：选择刚刚新建的文件夹
3. 勾选：登录时重新连接和使用其他凭据连接
4. 保存退出
5. 输入用户名密码



### 2. SFTP搭建

​	win10搭建sftp

參考：https://blog.csdn.net/lsfreeing/article/details/82424115





### 3. FTP搭建

參考：https://blog.csdn.net/qq_34610293/article/details/79210539

1. 创建ftp用户，与上面一样，此处不做赘述

2. 开启ftp服务

   控制面板 ---->



### 4. Bug及解决：

#### 4.1 bug1

![avatar](assert/bug_001.png)

解决：

将

```java
  		String host = "ip";
        String workingDirectory = "\\\\DESKTOP-S8C688T\\net_work";
//        String basePath = "";
        String username = "net_smb";
        String password = "123456";
        PolledDirectory polledDirectory = new SMBDirectory(host, workingDirectory, username, password);
        DirectoryPoller dp = DirectoryPoller.newBuilder()
                .addPolledDirectory(polledDirectory)
                .addListener(new MyListener())
                .enableFileAddedEventsForInitialContent()
                .setPollingInterval(10, TimeUnit.SECONDS)
                .start();
```

修改为：

```java
String basePath = "smb://net_smb:123456@/DESKTOP-S8C688T/net_work";
使用字符串的形式代替
```



#### 4.2. bug2

![avatar](assert/bug_002.png)

解决方案：

第一步:j 检查ip是否用正确，如果是本地，可以先替换成127.0.0.1 试试

第二步：确实是第一步正确的话，查看本地是否支持smb1协议，查看方式如下：

```powershell
# 运行窗口键入 powershell
# 进入 powershell 窗口后
# 键入
Get-SmbServerConfiguration | Select EnableSMB1Protocol, EnableSMB2Protocol
# 查看下图 如果smb1协议 为false 修改命令为
Set-SmbServerConfiguration -EnableSMB1Protocol $true


```

如下图

![avatar](assert/smb_status.png)

经过以上两步基本都可以搞定，但是没有从本质上解决jcifs不支持smb2 以上的协议，所以如果必须支持smb2则需要替换部分jar包，已经提供好相应资料，在附件中




