package com.mutisource.bigdata.monitor.directory;

import com.github.drapostolos.rdp4j.spi.FileElement;
import com.github.drapostolos.rdp4j.spi.PolledDirectory;
import com.mutisource.bigdata.monitor.directory.file.FtpFile;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * @author Jeremy
 * @create 2020 06 08 14:01
 */
public class FtpDirectory  implements PolledDirectory {
    private String host;
    private String workingDirectory;
    private String username;
    private String password;

    public FtpDirectory(String host, String workingDirectory, String username, String password) {
        this.host = host;
        this.workingDirectory = workingDirectory;
        this.username = username;
        this.password = password;
    }

    @Override
    public Set<FileElement> listFiles() throws IOException {
        FTPClient ftp = null;
        try{
            ftp = new FTPClient();
            ftp.connect(host);
            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                ftp.disconnect();
                throw new IOException("Exception when connecting to FTP Server: " + ftp);
            }
            ftp.login(username, password,workingDirectory);

            Set<FileElement> result = new LinkedHashSet<FileElement>();
            // 监听如果是目录则递归添加
            addMonitorDir(ftp,result,workingDirectory);
            return result;
        } catch (Exception e){
            throw new IOException(e);
        } finally {
            try {
                if(ftp.isConnected()) {
                    ftp.disconnect();
                }
            } catch(Throwable ioe) {
                // do nothing
            }
        }
    }

    /**
     * 递归添加目录
     * @param ftp ftp
     * @param result res
     */
    private void addMonitorDir(FTPClient ftp, Set<FileElement> result,String workDir) throws IOException {

        FTPFile[] ftpFiles = ftp.listFiles(workDir);
        for(FTPFile file : ftpFiles){
            String link = file.getLink();
            System.out.println("link ======== " + link);
            result.add(new FtpFile(file));
            if(file.isDirectory()){
                String name = file.getName();
                workDir = workDir + "/" + name;
                this.addMonitorDir(ftp,result,workDir);
            }
        }
    }


}
