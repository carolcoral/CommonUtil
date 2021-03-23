package site.cnkj.common.utils.io;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import site.cnkj.common.utils.logger.LoggerUtil;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

/*
 * @version 1.0 created by LXW on 2019/3/14 13:51
 */
public class StringUtil {

    private static final List EscapeStringList = Arrays.asList(".","|","\\","^");
    private static final List ESCAPE_TABS = Arrays.asList("\r","\n","\t");
    private static final String DES_KEY = "jbXGHTytBAxFqleSbJ";


    /**
     * 自动转义符合条件的字符串
     * @param str
     * @return
     */
    public static String escapeJava(String str) {
        int sz;
        sz = str.length();
        String newStr = new String();
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);
            if (EscapeStringList.contains(String.valueOf(ch))){
                newStr = newStr + "\\"+String.valueOf(ch);
            }else {
                newStr = newStr + String.valueOf(ch);
            }
        }
        return newStr;
    }


    /**
     * 自动转义特殊制表符
     * @param str 字符串
     * @return 转义后字符串
     */
    public static String escapeString(String str){
        for (Object escapeTab : ESCAPE_TABS) {
            str = str.replaceAll(
                    StringEscapeUtils.escapeJava(escapeTab.toString()),
                    "\\\\\\\\".concat(StringEscapeUtils.escapeJava(escapeTab.toString())));
        }
        return str;
    }


    /**
     * 格式化Exception为String
     * @param e Exception
     * @return e.toString
     */
    public static String formatException(Exception e){
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        String str = sw.toString();
        str = StringEscapeUtils.escapeJava(str);
        return str;
    }

    /**
     * 解密imsi和imei数据并校验
     * @param reg 校验规则
     * @param source 元数据
     * @return 结果
     */
    public static String des(String reg, String source){
        try{
            if (StringUtils.isEmpty(source) || "-".equals(source)){
                source = "";
                return source;
            } else {
                //判断是否为加密数据
                if (source.length() == 32){
                    try {
                        source = DES.decode(DES_KEY, source);
                        if (Pattern.matches(reg, source)){
                            return source;
                        }
                    }catch (Exception e){
                        return null;
                    }
                } else {
                    //2020年2月24日 待696版本修复bug后，邮件通知确认后，取消对36位长度的验证
                    if (source.length() == 36){
                        return source;
                    }else {
                        if (Pattern.matches(reg, source)){
                            return source;
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            LoggerUtil.error("解密imei和imsi数据并校验出现异常: reg="+reg+", source="+source, e);
        }
        return null;
    }

    /**
     * 计算字段在数组中的位置，并对计算后的结果去重
     * @param templateKeywords 数组
     * @param exceptionKeyWord 字符串（多个字符串信息用"|"拼接）
     * @return HashSet 位置
     * @throws Exception e
     */
    public static HashSet<String> computeExceptionWordInTemplateArea(String[] templateKeywords, String exceptionKeyWord) throws Exception{
        ArrayList<String> areas = new ArrayList<>();
        for (int i = 0; i < templateKeywords.length; i++) {
            if (exceptionKeyWord.contains("|")){
                String[] exceptionKeywords = exceptionKeyWord.split("\\|");
                for (String key : exceptionKeywords) {
                    if (templateKeywords[i].contains(key)){
                        areas.add(String.valueOf(i+1));
                    }
                }
            }else {
                if (templateKeywords[i].contains(exceptionKeyWord)){
                    areas.add(String.valueOf(i+1));
                }
            }
        }
        return new HashSet<>(areas);
    }

    /**
     * md5加密字符串
     * @param content 字符串
     * @return 加密后字符串
     */
    public static String md5(String content){
        if (content == null || content.length() == 0) {
            throw new IllegalArgumentException("String to encript cannot be null or zero length");
        }
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(content.getBytes());
            byte[] hash = md.digest();
            for (int i = 0; i < hash.length; i++) {
                if ((0xff & hash[i]) < 0x10) {
                    hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
                } else {
                    hexString.append(Integer.toHexString(0xFF & hash[i]));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hexString.toString();
    }

    public static String transferInputStream(InputStream inputStream){
        try {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString(StandardCharsets.UTF_8.name());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String CapitalizeFirstLetter(String input){
        try {
            char[] chars = input.toCharArray();
            chars[0]-=32;
            return String.valueOf(chars);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
