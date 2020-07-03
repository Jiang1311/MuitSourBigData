package com.mutisource.bigdata.util.security;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author Jeremy
 * @create 2020-07-03  20:13
 */
public class RsaUtils {
    /**
     * 密钥长度 于原文长度对应 以及越长速度越慢
     */
    private final static int KEY_SIZE = 1024;
    /**
     * 用于封装随机产生的公钥与私钥
     */

    /** 用来指定保存密钥对的文件名和存储的名称 */
    private static final String PUBLIC_KEY_NAME = "publicKey";
    private static final String PRIVATE_KEY_NAME = "privateKey";
    private static final String PUBLIC_FILENAME = "publicKey.properties";
    private static final String PRIVATE_FILENAME = "privateKey.properties";

    /**
     * 随机生成密钥对
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器
        keyPairGen.initialize(KEY_SIZE, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        // 得到私钥字符串
        String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        // 将公钥和私钥保存到Properties中
        storeKey(publicKeyString,PUBLIC_KEY_NAME,PUBLIC_FILENAME);
        storeKey(privateKeyString,PRIVATE_KEY_NAME,PRIVATE_FILENAME);

    }

    /**
     * 将指定的密钥字符串保存到文件中,如果找不到文件，就创建
     * @param keyString 密钥的Base64编码字符串（值）
     * @param keyName  保存在文件中的名称（键）
     * @param fileName 目标文件名
     */
    private static void storeKey(String keyString,String keyName,String fileName){
        Properties properties = new Properties();
        //存放密钥的绝对地址
        String path = null;
        try{
            path = RsaUtils.class.getClassLoader().getResource(fileName).toString();
            path = path.substring(path.indexOf(":") + 1);
        }catch (NullPointerException e){
            //如果不存#fileName#就创建
            String classPath = RsaUtils.class.getClassLoader().getResource("").toString();
            String prefix = classPath.substring(classPath.indexOf(":") + 1);
            String suffix = fileName;
            File file = new File(prefix + suffix);
            try {
                file.createNewFile();
                path = file.getAbsolutePath();
            } catch (IOException e1) {

            }
        }
        try(OutputStream out = new FileOutputStream(path)){
            properties.setProperty(keyName,keyString);
            properties.store(out,"There is " + keyName);
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }
    /**
     * RSA公钥加密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(String str, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.getDecoder().decode(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }
    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.getDecoder().decode(str);
        //base64编码的私钥
        byte[] decoded = Base64.getDecoder().decode(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        RsaUtils.genKeyPair();
    }

}