package com.mutisource.bigdata.monitor.directory.file;

import com.github.drapostolos.rdp4j.spi.FileElement;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import lombok.EqualsAndHashCode;



/**
 * @author Jeremy
 * @create 2020 06 08 14:01
 *
 */
@EqualsAndHashCode
public class SftpFile implements FileElement
{
    private final LsEntry file;

    private final boolean isDirectory;

    public SftpFile(LsEntry file) {
        this.file = file;
        this.isDirectory = file.getAttrs().isDir();
    }

    @Override
    public long lastModified()
    {
    	return file.getAttrs().getMTime();
    }

    @Override
    public boolean isDirectory() {
        return isDirectory;
    }

    @Override
    public String getName() {
        return file.getFilename();
    }

  @Override
    public String toString() {
        return getName();
    }
}