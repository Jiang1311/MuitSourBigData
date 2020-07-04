package com.mutisource.bigdata.monitor.test;

import com.github.drapostolos.rdp4j.DirectoryPoller;
import com.github.drapostolos.rdp4j.spi.PolledDirectory;
import com.mutisource.bigdata.monitor.MyListener;
import com.mutisource.bigdata.monitor.directory.SftpDirectory;

import java.util.concurrent.TimeUnit;

/**
 * @author JJM
 */
public class SftpMonitorTest {
    public static void main(String[] args) {

        String host = "********";
        String path ="/sftp01";
        String username = "sftp";
        String password = "123456";
        MyListener myListener = new MyListener();
        PolledDirectory polledDirectory = new SftpDirectory(host, path, username, password);

        DirectoryPoller dp = DirectoryPoller.newBuilder()
                .addPolledDirectory(polledDirectory)
                .addListener(myListener)
                .enableFileAddedEventsForInitialContent()
                .setPollingInterval(1, TimeUnit.SECONDS)
                .start();

        try {
            TimeUnit.HOURS.sleep(2);

        } catch (Exception e) {
            e.printStackTrace();
        }

        dp.stop();
    }


}
