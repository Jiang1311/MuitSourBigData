package com.mutisource.bigdata.monitor.test;

import com.github.drapostolos.rdp4j.DirectoryPoller;
import com.github.drapostolos.rdp4j.spi.PolledDirectory;
import com.mutisource.bigdata.monitor.MyListener;
import com.mutisource.bigdata.monitor.directory.SmbDirectory;

import java.util.concurrent.TimeUnit;

/**
 * @author Jeremy
 * @create 2020 06 09 10:10
 */
public class SmbMonitorTest {
    public static void main(String[] args) {
        //协议是smb，格式按照指定格式 这里不正确报过协议错误
        String host = "*******";
        String username = "smb";
        String password = "123456";

        String workingDirectory = "smb://" + host + "/smb/";
        PolledDirectory polledDirectory = new SmbDirectory(host, workingDirectory, username, password);
        DirectoryPoller dp = DirectoryPoller.newBuilder()
                .addPolledDirectory(polledDirectory)
                .addListener(new MyListener())
                .enableFileAddedEventsForInitialContent()
//                通过该值设定轮询频次，修改监听时效问题
                .setPollingInterval(5, TimeUnit.SECONDS)
                .start();

        try {
            TimeUnit.HOURS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        dp.stop();
    }


}
