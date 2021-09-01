package site.cnkj.common.utils.io;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA非对称数据加解密
 * @version 1.0 created by LXW on 2019/4/15 15:31
 */
public class RSAEncrypt {

    public static final String DEFAULT_PUBLIC_KEY=
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQChDzcjw/rWgFwnxunbKp7/4e8w" + "\r" +
                    "/UmXx2jk6qEEn69t6N2R1i/LmcyDT1xr/T2AHGOiXNQ5V8W4iCaaeNawi7aJaRht" + "\r" +
                    "Vx1uOH/2U378fscEESEG8XDqll0GCfB1/TjKI2aitVSzXOtRs8kYgGU78f7VmDNg" + "\r" +
                    "XIlk3gdhnzh+uoEQywIDAQAB" + "\r";

    public static final String DEFAULT_PRIVATE_KEY=
            "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKEPNyPD+taAXCfG" + "\r" +
                    "6dsqnv/h7zD9SZfHaOTqoQSfr23o3ZHWL8uZzINPXGv9PYAcY6Jc1DlXxbiIJpp4" + "\r" +
                    "1rCLtolpGG1XHW44f/ZTfvx+xwQRIQbxcOqWXQYJ8HX9OMojZqK1VLNc61GzyRiA" + "\r" +
                    "ZTvx/tWYM2BciWTeB2GfOH66gRDLAgMBAAECgYBp4qTvoJKynuT3SbDJY/XwaEtm" + "\r" +
                    "u768SF9P0GlXrtwYuDWjAVue0VhBI9WxMWZTaVafkcP8hxX4QZqPh84td0zjcq3j" + "\r" +
                    "DLOegAFJkIorGzq5FyK7ydBoU1TLjFV459c8dTZMTu+LgsOTD11/V/Jr4NJxIudo" + "\r" +
                    "MBQ3c4cHmOoYv4uzkQJBANR+7Fc3e6oZgqTOesqPSPqljbsdF9E4x4eDFuOecCkJ" + "\r" +
                    "DvVLOOoAzvtHfAiUp+H3fk4hXRpALiNBEHiIdhIuX2UCQQDCCHiPHFd4gC58yyCM" + "\r" +
                    "6Leqkmoa+6YpfRb3oxykLBXcWx7DtbX+ayKy5OQmnkEG+MW8XB8wAdiUl0/tb6cQ" + "\r" +
                    "FaRvAkBhvP94Hk0DMDinFVHlWYJ3xy4pongSA8vCyMj+aSGtvjzjFnZXK4gIjBjA" + "\r" +
                    "2Z9ekDfIOBBawqp2DLdGuX2VXz8BAkByMuIh+KBSv76cnEDwLhfLQJlKgEnvqTvX" + "\r" +
                    "TB0TUw8avlaBAXW34/5sI+NUB1hmbgyTK/T/IFcEPXpBWLGO+e3pAkAGWLpnH0Zh" + "\r" +
                    "Fae7oAqkMAd3xCNY6ec180tAe57hZ6kS+SYLKwb4gGzYaCxc22vMtYksXHtUeamo" + "\r" +
                    "1NMLzI2ZfUoX" + "\r";



    /**
     * 私钥
     */
    private RSAPrivateKey privateKey;

    /**
     * 公钥
     */
    private RSAPublicKey publicKey;

    /**
     * 字节数据转字符串专用集合
     */
    private static final char[] HEX_CHAR= {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    /**
     * 获取私钥
     * @return 当前的私钥对象
     */
    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    /**
     * 获取公钥
     * @return 当前的公钥对象
     */
    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * 随机生成密钥对
     */
    public void genKeyPair(){
        KeyPairGenerator keyPairGen= null;
        try {
            keyPairGen= KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (keyPairGen != null){
            keyPairGen.initialize(2048, new SecureRandom());
            KeyPair keyPair= keyPairGen.generateKeyPair();
            this.privateKey= (RSAPrivateKey) keyPair.getPrivate();
            this.publicKey= (RSAPublicKey) keyPair.getPublic();
        }
    }

    private StringBuilder loadKey(InputStream in) throws IOException {
        BufferedReader br= new BufferedReader(new InputStreamReader(in));
        String readLine= null;
        StringBuilder sb= new StringBuilder();
        while((readLine= br.readLine())!=null){
            if(readLine.charAt(0)!='-'){
                sb.append(readLine);
                sb.append('\r');
            }
        }
        return sb;
    }


    /**
     * 从文件中输入流中加载公钥
     * @param in 公钥输入流
     * @throws IOException e
     * @throws NoSuchAlgorithmException e
     * @throws InvalidKeySpecException e
     */
    public void loadPublicKey(InputStream in) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        loadPublicKey(loadKey(in).toString());
    }


    /**
     * 从字符串中加载公钥
     * @param publicKeyStr 公钥数据字符串
     * @throws NoSuchAlgorithmException e
     * @throws InvalidKeySpecException e
     */
    public void loadPublicKey(String publicKeyStr) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] buffer= Base64.decodeBase64(publicKeyStr);
        KeyFactory keyFactory= KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec= new X509EncodedKeySpec(buffer);
        this.publicKey= (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }


    /**
     * 从文件中加载私钥
     * @param in 输入流
     * @throws NoSuchAlgorithmException e
     * @throws InvalidKeySpecException e
     * @throws IOException e
     */
    public void loadPrivateKey(InputStream in) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException{
        loadPrivateKey(loadKey(in).toString());
    }

    public void loadPrivateKey(String privateKeyStr) throws NoSuchAlgorithmException, InvalidKeySpecException {
        try {
            byte[] buffer= Base64.decodeBase64(privateKeyStr);
            PKCS8EncodedKeySpec keySpec= new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory= KeyFactory.getInstance("RSA");
            this.privateKey= (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new InvalidKeySpecException("私钥非法");
        } catch (NullPointerException e) {
            throw new NullPointerException("私钥数据为空");
        }
    }


    /**
     * 加密过程
     * @param publicKey 公钥
     * @param plainTextData 明文数据
     * @return 加密后字节
     * @throws NoSuchAlgorithmException e
     * @throws InvalidKeyException e
     * @throws IllegalBlockSizeException e
     * @throws BadPaddingException e
     * @throws NoSuchPaddingException e
     */
    public byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
        if(publicKey== null){
            throw new NullPointerException("加密公钥为空, 请设置");
        }
        Cipher cipher= null;
        try {
            cipher= Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding", new BouncyCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(plainTextData);
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException("无此加密算法");
        } catch (InvalidKeyException e) {
            throw new InvalidKeyException("加密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new IllegalBlockSizeException("明文长度非法");
        } catch (BadPaddingException e) {
            throw new BadPaddingException("明文数据已损坏");
        } catch (NoSuchPaddingException e) {
            throw new NoSuchPaddingException("无此类填充");
        }
    }


    /**
     * 解密过程
     * @param privateKey 私钥
     * @param cipherData 密文数据
     * @return 明文
     * @throws NoSuchAlgorithmException e
     * @throws NoSuchPaddingException e
     * @throws InvalidKeyException e
     * @throws IllegalBlockSizeException e
     * @throws BadPaddingException e
     */
    public byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        if (privateKey== null){
            throw new NullPointerException("解密私钥为空, 请设置");
        }
        Cipher cipher= null;
        try {
            cipher= Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding", new BouncyCastleProvider());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(cipherData);
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException("无此解密算法");
        } catch (NoSuchPaddingException e) {
            throw new NoSuchPaddingException("无此类填充");
        }catch (InvalidKeyException e) {
            throw new InvalidKeyException("解密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new IllegalBlockSizeException("密文长度非法");
        } catch (BadPaddingException e) {
            throw new BadPaddingException("密文数据已损坏");
        }
    }


    /**
     * 字节数据转十六进制字符串
     * @param data 输入数据
     * @return 十六进制内容
     */
    public static String byteArrayToString(byte[] data){
        StringBuilder stringBuilder= new StringBuilder();
        for (int i=0; i<data.length; i++){
            //取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
            stringBuilder.append(HEX_CHAR[(data[i] & 0xf0)>>> 4]);
            //取出字节的低四位 作为索引得到相应的十六进制标识符
            stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
            if (i<data.length-1){
                stringBuilder.append(' ');
            }
        }
        return stringBuilder.toString();
    }

}