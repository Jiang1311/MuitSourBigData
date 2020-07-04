package com.mutisource.bigdata.monitor.directory.file;

import com.github.drapostolos.rdp4j.spi.FileElement;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import lombok.EqualsAndHashCode;

/**
 * @author Jeremy
 * @create 2020 06 09 13:18
 */
@EqualsAndHashCode
public class MySmbFile implements FileElement {
    private final SmbFile file;
    private final String name;

    public MySmbFile(SmbFile file) {
        this.file = file;
        this.name = file.getName();
    }

    @Override
    public long lastModified() {
        return file.getLastModified();
    }

    @Override
    public boolean isDirectory() {
        try {
            return file.isDirectory();
        } catch (SmbException e) {
            e.printStackTrace();
        }
        return false;
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
