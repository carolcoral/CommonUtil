package site.cnkj.common.utils.io;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

/**
 * DES加密工具类
 * 
 * @author xuyang
 * 
 */
public class DES {

    private static final String S_DEFAULT_KEY = "redcloud";

    private final static String S_DES = "DES";
    private final static String S_HEX = "0123456789abcdef";
    private final static String S_MODE = "DES/ECB/PKCS5Padding";
    public static String S_DES_KEY = "jbXGHTytBAxFqleSbJ";

    /**
     * 加密
     * 
     * @param key
     *            密钥
     * @param input
     *            加密前的字符串
     * @return 加密后的字符串
     * @throws Exception
     */
    public static String encode(String key, String input) throws Exception {
        byte[] data = encrypt(key, input);
        return toHex(data);
    }

    /**
     * 加密
     * 
     * @param key
     *            密钥
     * @param input
     *            加密前的字符串
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(String key, String input) throws Exception {
        return doFinal(key, Cipher.ENCRYPT_MODE, input.getBytes("UTF-8"));
    }

    /**
     * 解密
     * 
     * @param key
     *            密钥
     * @param input
     *            解密前的字符串
     * @return encode方法返回的字符串
     * @throws Exception
     */
    public static String decode(String key, String input) throws Exception {
        byte[] data = decrypt(key, toByte(input));
        return new String(data,"UTF-8");
    }

    /**
     * 解密
     * 
     * @param key
     *            密钥
     * @param input
     *            encrypt方法返回的字节数组
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(String key, byte[] input) throws Exception {
        return doFinal(key, Cipher.DECRYPT_MODE, input);
    }

    /**
     * 执行加密解密操作
     * 
     * @param key
     *            密钥
     * @param opmode
     *            操作类型：Cipher.ENCRYPT_MODE-加密，Cipher.DECRYPT_MODE-解密
     * @param input
     *            加密解密前的字节数组
     * @return
     * @throws Exception
     */
    private static byte[] doFinal(String key, int opmode, byte[] input) throws Exception {
        key = key != null ? key : S_DEFAULT_KEY;
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密匙数据创建一个DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key.getBytes());
        // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成 一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(S_DES);
        SecretKey secureKey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(S_MODE);
        // 用密匙初始化Cipher对象
        // IvParameterSpec param = new IvParameterSpec(IV);
        // cipher.init(Cipher.DECRYPT_MODE, securekey, param, sr);
        cipher.init(opmode, secureKey, sr);
        // 执行加密解密操作
        return cipher.doFinal(input);
    }

    /**
     * 将加密后的bytes转换成string
     * 
     * @param buf
     * @return
     */
    public static String toHex(byte[] buf) {
        if (buf == null) {
            return "";
        }
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (byte aBuf : buf) {
            appendHex(result, aBuf);
        }
        return result.toString();
    }

    /**
     * 将byte中的数据保存到字符串中
     * 
     * @param sb
     * @param b
     */
    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(S_HEX.charAt((b >> 4) & 0x0f)).append(S_HEX.charAt(b & 0x0f));
    }



    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        }
        return result;
    }

}