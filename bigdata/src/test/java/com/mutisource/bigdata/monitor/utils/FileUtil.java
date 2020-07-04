package com.mutisource.bigdata.monitor.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Jeremy
 * @create 2020 06 08 14:01
 */
public class FileUtil{

    /**
     * 新增文件
     * @throws IOException ex
     */
    public static long createFile(String sourcePath, String targetPath){
        //封装好需要复制的文件原路径\
        try {
            FileInputStream fis=new FileInputStream(sourcePath);
            //将文件复制到指定路径下
            FileOutputStream fos=new FileOutputStream(targetPath);
            byte[] lsy=new byte[fis.available()];
            fis.read(lsy);
            fos.write(lsy);
            fos.close();
            fis.close();
            File file=new File(targetPath);
            if(file.exists()){
                System.out.println("文件成功复制到指定路径下");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        long createTime = System.currentTimeMillis();
        return createTime;
    }
}