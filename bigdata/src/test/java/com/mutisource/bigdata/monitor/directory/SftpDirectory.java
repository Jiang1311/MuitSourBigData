package com.mutisource.bigdata.monitor.directory;

import com.github.drapostolos.rdp4j.spi.FileElement;
import com.github.drapostolos.rdp4j.spi.PolledDirectory;
import com.jcraft.jsch.*;
import com.mutisource.bigdata.monitor.directory.file.SftpFile;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Vector;

/**
 * @author Jeremy
 * @create 2020 06 08 16:09
 */
public class SftpDirectory implements PolledDirectory {
    private String host;
    private String workingDirectory;
    private String username;
    private String password;

    public SftpDirectory(String host, String workingDirectory, String username, String password) {
        this.host = host;
        this.workingDirectory = workingDirectory;
        this.username = username;
        this.password = password;
    }

    @Override
    public Set<FileElement> listFiles() throws IOException {
        Set<FileElement> result = new LinkedHashSet<FileElement>();

        JSch jsch = new JSch();
        Session session = null;
        try {
            session = jsch.getSession(username, host, 23);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;
//           如果监听目录为文件夹则递归调用
            addMonitorDir(sftpChannel, result, workingDirectory);

            sftpChannel.exit();
            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
            throw new IOException(e);
        } catch (SftpException e) {
            e.printStackTrace();
            throw new IOException(e);
        }

        return result;
    }

    /**
     * @param sftpChannel ChannelSftp
     * @param result      result
     * @param workDir     目录
     */
    private void addMonitorDir(ChannelSftp sftpChannel, Set<FileElement> result, String workDir) throws SftpException {
        Vector<ChannelSftp.LsEntry> filesList = sftpChannel.ls(workDir);
        for (ChannelSftp.LsEntry file : filesList) {
            SftpFile sftpFile = new SftpFile(file);
            result.add(sftpFile);
            String name = sftpFile.getName();
            if (!name.equals(".") && !"..".equals(name) && sftpFile.isDirectory()) {
                addMonitorDir(sftpChannel, result, workDir + "/" + name);

            }
        }
    }
}