package com.mutisource.bigdata.util.common;


import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * <p>
 *      类描述信息：文件下载工具类
 * </p>
 *
 * @author  Created by Jeremy
 * @date    2020/5/7 15:21
 */
public class DownloadUtils {
    public static void download(ByteArrayOutputStream byteArrayOutputStream, HttpServletResponse response, String returnName) throws IOException {
        response.setContentType("application/octet-stream");
        // 保存的文件名,必须和页面编码一致,否则乱码
        response.encodeURL(new String(returnName.getBytes(), StandardCharsets.UTF_8));
        response.addHeader("Content-Disposition","attachment;filename=total.xls");
        response.setContentLength(byteArrayOutputStream.size());
        response.addHeader("Content-Length", "" + byteArrayOutputStream.size());
        //取得输出流
        ServletOutputStream outPutStream = response.getOutputStream();
        //写到输出流
        byteArrayOutputStream.writeTo(outPutStream);
        //关闭
        byteArrayOutputStream.close();
        //刷数据
        outPutStream.flush();
    }


    /**
     * 根据hdfs路径 批量打包下载
     * @param pathList 路径list
     * @param fs fileSystem
     * @param response resp
     * @throws Exception ex
     */
    public static void downloadFileList(List<String> pathList, FileSystem fs, HttpServletResponse response)throws Exception{
        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String downloadName = df.format(new Date()) + ".zip";
        response.reset();
        // 不同类型的文件对应不同的MIME类型
        response.setContentType("application/x-msdownload");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + downloadName );

        OutputStream out = response.getOutputStream();
        ZipOutputStream zos = new ZipOutputStream(out);
        for (String path : pathList) {
            String name  = path.substring(path.lastIndexOf("/") + 1);
            // 获取hdfs 文件流
            Path fsPath = new Path(path);
            InputStream inputStream = fs.open(fsPath);
            byte[] buffer = new byte[1024];
            int len ;
            ZipEntry entry = new ZipEntry(name);
            zos.putNextEntry(entry);
            while ((len = inputStream.read(buffer)) != -1 ) {
                zos.write(buffer,0,len);
            }
            inputStream.close();
            zos.closeEntry();
        }
        zos.close();
    }



}