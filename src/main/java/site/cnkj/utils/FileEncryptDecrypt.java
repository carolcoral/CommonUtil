package site.cnkj.utils;

import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Security;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/*
 * @version 1.0 created by LXW on 2019/4/15 15:31
 * @desc 对文件内容RSA加解密
 */
public class FileEncryptDecrypt {

    private static final String RSA_ALGORITHM = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
    private static final Charset UTF8 = Charset.forName("UTF-8");

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    /**
     * 加密文件
     * @param keyPath 公钥路径，DER
     * @param input 输入文件地址
     * @param output 输出文件地址
     */
    public static boolean encrypt(String keyPath, String input, String output){
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        List<String> list = new ArrayList();
        try {
            byte[] buffer = Files.readAllBytes(Paths.get(keyPath));
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            File inputFile = new File(input);
            File outputFile = new File(output);
            fileInputStream = new FileInputStream(inputFile);
            fileOutputStream = new FileOutputStream(outputFile);
            byte[] inputByte = new byte[116];
            int len;
            while((len = fileInputStream.read(inputByte)) != -1){
                list.add(new String(inputByte, 0, len));
            }
            for (String s : list) {
                byte [] encrypted = encrypt(publicKey, s);
                fileOutputStream.write(encrypted);
                fileOutputStream.flush();
            }
            fileOutputStream.close();
            fileInputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static byte[] encrypt(PublicKey publicKey, String message) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM,"BC");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey, new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT));
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.getEncoder().encode(cipher.doFinal(message.getBytes(UTF8)));
    }

    /**
     * 从字符串中加载公钥
     *
     */
    private static RSAPublicKey loadPublicKey(String publicKeyStr) throws Exception {
        try {
            byte[] buffer = new BASE64Decoder().decodeBuffer(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }


    //public static void main(String [] args) throws Exception {
    //    encrypt("F:\\dls\\robot-dls-file\\config\\rsa_public_key.der",
    //            "C:\\Users\\Carol\\Desktop\\file\\868663032830438_migu$user$login$_1554947856915.log",
    //            "C:\\Users\\Carol\\Desktop\\decrypt\\868663032830438_migu$user$login$_1557208800000.log"
    //            );
    //}

}
