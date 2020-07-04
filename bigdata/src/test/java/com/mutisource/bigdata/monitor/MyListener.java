package com.mutisource.bigdata.monitor;

import com.github.drapostolos.rdp4j.*;


/**
 * @author Jeremy/
 * @create 2020 06 08 14:01
 */
public class MyListener implements DirectoryListener, IoErrorListener, InitialContentListener {
    long createTime = 0L;
    @Override
    public void fileAdded(FileAddedEvent event) {
        // 时间效率测试
        String msg = "Added: " + event.getFileElement();
        long addTime = System.currentTimeMillis();
        System.out.println("文件新增时间====== " + addTime);
        long result = addTime - createTime;
        System.out.println("文件新增间隔======" + result);
        System.out.println(msg);
    }

    @Override
    public void fileRemoved(FileRemovedEvent event) {
        // 时间效率测试
        long removeTime = System.currentTimeMillis();
        System.out.println("文件刪除时间====== " + removeTime);
        long result = removeTime - createTime;
        System.out.println("文件删除间隔======" + result);
        System.out.println("Removed: " + event.getFileElement());

    }

    @Override
    public void fileModified(FileModifiedEvent event) {
        // 时间效率测试
        long modifyTime = System.currentTimeMillis();
        System.out.println("文件修改时间====== " + modifyTime);
        long result = modifyTime - createTime;
        System.out.println("文件修改间隔======" + result);

        System.out.println("Modified: " + event.getFileElement());
    }

    @Override
    public void ioErrorCeased(IoErrorCeasedEvent event) {
        System.out.println("I/O error ceased.");
    }

    @Override
    public void ioErrorRaised(IoErrorRaisedEvent event) {
        System.out.println("I/O error raised!");
        event.getIoException().printStackTrace();
    }

    @Override
    public void initialContent(InitialContentEvent event) {
        System.out.println("initial Content: ^");
//        createTime = FileUtil.createFile("D:\\test\\new01.txt","/sftp01/sftp002/sftp003/sftp004/sftp005/new01.txt");
//        System.out.println("文件初始化时间 ======== " + createTime);
    }
}
