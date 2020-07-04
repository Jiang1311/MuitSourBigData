package com.mutisource.bigdata.monitor.test;

import com.github.drapostolos.rdp4j.DirectoryPoller;
import com.github.drapostolos.rdp4j.spi.PolledDirectory;
import com.mutisource.bigdata.monitor.MyListener;
import com.mutisource.bigdata.monitor.directory.FtpDirectory;

import java.util.concurrent.TimeUnit;

/**
 * @author JJM
 */
public class FtpMonitorTest {
    public static void main(String[] args) {

        String host = "**********";
        String workingDirectory ="";
        String username = "ftp";
        String password = "123456";
        PolledDirectory polledDirectory = new FtpDirectory(host, workingDirectory, username, password);
        MyListener myListener = new MyListener();
        DirectoryPoller dp = DirectoryPoller.newBuilder()
                .addPolledDirectory(polledDirectory)
                .addListener(myListener)
                .enableFileAddedEventsForInitialContent()
                .setPollingInterval(1, TimeUnit.MILLISECONDS)
                .start();

        try {
            TimeUnit.HOURS.sleep(2);

        } catch (Exception e) {
            e.printStackTrace();
        }

        dp.stop();
    }


}
