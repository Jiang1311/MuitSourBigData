package com.mutisource.bigdata.util.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 *
 * @author Jeremy
 * @create  2020 07 03 20:13
 */
public class Md5Util {

    public static String getMD5(String message) {
        String md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageByte = message.getBytes(StandardCharsets.UTF_8);
            byte[] md5Byte = md.digest(messageByte);
            StringBuilder hexStr = new StringBuilder();
            int num;
            for (byte b : md5Byte) {
                num = b;
                if (num < 0) {
                    num += 256;
                }
                if (num < 16) {
                    hexStr.append("0");
                }
                hexStr.append(Integer.toHexString(num));
            }
            md5 = hexStr.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5;
    }


}
