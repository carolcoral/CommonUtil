package site.cnkj.utils;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/*
 * @version 1.0 created by LXW on 2019/3/14 13:51
 */
public class StringUtil {

    private static final List EscapeStringList = Arrays.asList(".","|","\\","^");
    private static final List ESCAPE_TABS = Arrays.asList("\r","\n","\t");

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
     * Ascii转换为字符串
     * @param value
     * @return
     */
    public static String asciiToString(String value) {
        StringBuffer sbu = new StringBuffer();
        String[] chars = value.split(",");
        for (int i = 0; i < chars.length; i++) {
            sbu.append((char) Integer.parseInt(chars[i]));
        }
        return sbu.toString();
    }

    /**
     * 将字符串转化为int数组
     *
     * @param str 带解析的字符串
     * @return 转化而成的int数组
     */
    public static int[] parse(String str) {
        int length = str.length();
        int[] result = new int[length];
        // 依次取得字符串中的每一个字符，并将其转化为数字，放进int数组中
        for (int i = 0; i < length; i++) {
            char c = str.charAt(i);
            result[i] = Character.getNumericValue(c);
        }
        return result;
    }

    /**
     * 根据指定字节串拆分字节
     * @param delimiter 分割字节
     * @param array 待分割字节
     * @return 分割后字节串数组
     */
    public static List<byte[]> split(byte[] array, byte[] delimiter) {
        List<byte[]> byteArrays = new LinkedList<>();
        if (delimiter.length == 0) {
            return byteArrays;
        }
        int begin = 0;

        outer:
        for (int i = 0; i < array.length - delimiter.length + 1; i++) {
            for (int j = 0; j < delimiter.length; j++) {
                if (array[i + j] != delimiter[j]) {
                    continue outer;
                }
            }
            byteArrays.add(Arrays.copyOfRange(array, begin, i));
            begin = i + delimiter.length;
        }
        byteArrays.add(Arrays.copyOfRange(array, begin, array.length));
        return byteArrays;
    }

}
