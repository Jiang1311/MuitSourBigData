package com.mutisource.bigdata.monitor.test;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

import java.io.*;

/**
 * @author Jeremy
 * @create 2020 06 09 13:21
 */
public class SmbLinkTest {
    public static void main(String[] args) throws IOException {

        String ip = "*******";
        String name = "smb";
        String pwd = "123456";
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, name, pwd);
        //通过smb拷贝文件到局域网文件夹
        String smbUrl = "smb://" + ip + "/smb/";
        SmbFile smb = new SmbFile(smbUrl, auth);
        smb.connect();
        SmbFile[] smbFiles = smb.listFiles();
        int length = smbFiles.length;
        System.out.println(length);

    }
}
