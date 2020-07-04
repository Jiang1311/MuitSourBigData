package com.mutisource.bigdata.monitor.directory.file;

import com.github.drapostolos.rdp4j.spi.FileElement;
import lombok.EqualsAndHashCode;
import org.apache.commons.net.ftp.FTPFile;


/**
 * @author Jeremy
 * @create 2020 06 08 14:01*

 */
@EqualsAndHashCode
public class FtpFile implements FileElement {

    private final FTPFile file;
    private final String name;
    private final boolean isDirectory;

    public FtpFile(FTPFile file) {
        this.file = file;
        this.name = file.getName();
        this.isDirectory = file.isDirectory();
    }

    @Override
    public long lastModified()  {
        return file.getTimestamp().getTimeInMillis();
    }

    @Override
    public boolean isDirectory() {
        return isDirectory;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return file.toString();
    }
}
