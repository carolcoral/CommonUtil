package site.cnkj.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/*
 * @version 1.0 created by LXW on 2019/1/31 15:32
 */
public class FileUtil {


    /**
     * 检查文件大小
     * @param filePath 完整文件路径
     * @param num mb 的倍数， 1024 MB = 1 GB
     * @return
     */
    public static Boolean checkFileSize(String filePath, int num) {
        File file = new File(filePath);
        return checkFileSize(file, num);
    }

    public static Boolean checkFileSize(File file, int num) {
        try {
            long size = file.length();
            long mb = 1048576;
            long res = size/(mb*num);
            return res > 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 写入获取的内容到本地指定文件中，相同文件追加写入
     * @param filePath 文件路径
     * @param info 写入内容
     */
    public static void writeFile(String filePath, String info){
        try {
            File file = new File(filePath);
            if (!file.exists()){
                file.createNewFile();
            }
            FileUtils.write(file, info.concat("\n"), "utf-8", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入日志信息到文件中，自动补充写入时间
     * @param filePath
     * @param info
     */
    public static void writeLog(String filePath, String info){
        String time = DateUtil.translateTimeToDate(DateUtil.getCurrentTime(), DateUtil.DATEFORMATE.FULLTIMEBY_yMdHmsS);
        String fullInfo = time.concat(" ").concat(info);
        File file = new File(filePath);
        try {
            //如果路径不存在则自动创建
            if (!file.exists()){
                file.createNewFile();
            }
            FileUtils.write(file, fullInfo.concat("\n"), "utf-8", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取指定路径文件的总行数
     * @param filePath
     * @return
     */
    public static int getFileAllLineNumbers(String filePath) {
        try {
            File file = new File(filePath);
            return getFileAllLineNumbers(file);
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    public static int getFileAllLineNumbers(File file) {
        FileReader fileReader = null;
        LineNumberReader lineNumberReader = null;
        try {
            if (file.isFile()){
                fileReader = new FileReader(file);
                lineNumberReader = new LineNumberReader(fileReader);
                lineNumberReader.skip(Long.MAX_VALUE);
                int totalLineNumber = lineNumberReader.getLineNumber();
                return totalLineNumber +1;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (fileReader != null){
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (lineNumberReader != null){
                try {
                    lineNumberReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }

    /**
     * md5加密字符串
     * @param content 字符串
     * @return 加密后字符串
     */
    public static String getMd5Security(String content){
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

    public static File createFile(String... filePath){
        try {
            if (filePath.length > 0){
                String fullFilePath = StringUtils.join(filePath, File.separator);
                File file = new File(fullFilePath);
                if (!file.exists()){
                    if (!file.getParentFile().exists()){
                        file.getParentFile().mkdirs();
                    }
                    if (!file.exists()){
                        file.createNewFile();
                    }
                }
                return new File(fullFilePath);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
