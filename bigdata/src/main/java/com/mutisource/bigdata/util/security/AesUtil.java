package com.mutisource.bigdata.util.security;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jeremy
 * @create  2020 07 03 20:13
 */
public class AesUtil {

    private static final String KEY_ALGORITHM = "AES";


    /**
     * AES 加密操作
     *
     * @param content 待加密内容
     * @param pass 加密密码
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String pass) {
        try {
            Cipher cipher = Cipher.getInstance("AES");

            byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);

            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(pass));

            byte[] result = cipher.doFinal(byteContent);

            return Base64.encodeBase64String(result);
        } catch (Exception ex) {
            Logger.getLogger(AesUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * AES 解密操作
     *
     * @param content 解密内容
     * @param password 秘钥
     * @return 加密后字符串
     */
    public static String decrypt(String content, String password) {

        try {
            //实例化
            Cipher cipher = Cipher.getInstance("AES");

            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));

            //执行操作
            byte[] result = cipher.doFinal(Base64.decodeBase64(content));

            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            Logger.getLogger(AesUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    /**
     * 生成加密秘钥
     *
     * @return 生成加密秘钥
     */
    private static SecretKeySpec getSecretKey(final String password) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg;

        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);

            //AES 要求密钥长度为 128
            kg.init(128, new SecureRandom(password.getBytes()));

            //生成一个密钥
            SecretKey secretKey = kg.generateKey();

            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AesUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static void main(String[] args) {
        String s = "test";

        System.out.println("s:" + s);
        // 秘钥
        String secretKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCfvC4Cur2yh0KMwVXbU6NrAyICaHtWM7Yot5a27JFkeJg7UYbF2FGJRuhGFGC9Qmn8eCLYY7FOi8BSe+uUbdK6p3J56y0wGsjRW2YFkQ4TpIf1isL29SBUG9UMoTTzgoO2kkaIbNvh1JLWwoD47s83OqjJRwacylLAmTXmZNIIAw";

        String s1 = AesUtil.encrypt(s, secretKey);
        System.out.println("s1:" + s1);

        System.out.println("s2:"+AesUtil.decrypt(s1, secretKey));
        

    }

}