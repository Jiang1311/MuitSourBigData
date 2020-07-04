package com.mutisource.bigdata.monitor.directory;

import com.github.drapostolos.rdp4j.spi.FileElement;
import com.github.drapostolos.rdp4j.spi.PolledDirectory;
import com.mutisource.bigdata.monitor.directory.file.MySmbFile;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * @author Jeremy
 * @create 2020 06 09 13:21
 */
public class SmbDirectory implements PolledDirectory {
    private String host;
    private String workingDirectory = "";
    private String username;
    private String password;


    public SmbDirectory(String host, String workingDirectory, String username, String password) {
        this.host = host;
        this.workingDirectory = workingDirectory;
        this.username = username;
        this.password = password;
    }

    @Override
    public Set<FileElement> listFiles() throws IOException {
        try {
            Set<FileElement> result = new LinkedHashSet<FileElement>();
            //初始化用户权限信息
            NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, username, password);
            // 使用递归的方式，实现多层目录监听
            this.addMonitorDir(workingDirectory,auth,result);

            return result;
              } catch (Exception e) {
            throw new IOException(e);
        }
    }

    /**
     *  @param workDir 监听的目录
     * @param auth 用户权限信息
     * @param result 文件列表
     */
    private void addMonitorDir(String workDir, NtlmPasswordAuthentication auth, Set<FileElement> result) throws IOException {
        //           修改连接方式，注意SmbFile不同构造函数的使用规则，
        SmbFile remoteFile = new SmbFile(workDir, auth);
        //尝试连接
        remoteFile.connect();
        // 获取 目录下所有子文件和文件夹。
        SmbFile[] smbFiles = remoteFile.listFiles();
        // 当前目录添加
        for (SmbFile smbFile : smbFiles) {
            result.add(new MySmbFile(smbFile));
        }
        for (SmbFile file :smbFiles) {
            if (file.isDirectory()){
                String path = file.getPath();
                // 递归调用
                addMonitorDir(path,auth,result);
            }

        }
    }
}
