package site.cnkj.common.utils.file;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import site.cnkj.common.utils.date.DateUtil;
import site.cnkj.common.utils.http.HttpCommonUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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
        Boolean flag = false;
        try {
            long size = file.length();
            long mb = 1048576;
            long res = size/(mb*num);
            if (res > 0){
                flag = true;
            }else {
                flag = false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 写入获取的内容到本地指定文件中，相同文件追加写入
     * @param file 文件
     * @param info 写入内容
     */
    public static void writeFile(File file, String info){
        try {
            FileUtils.writeStringToFile(file, info+"\n", "UTF-8", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入获取的内容到本地指定文件中，相同文件追加写入
     * @param file 文件
     * @param info 写入内容
     */
    public static void writeFile(File file, List<String> info){
        try {
            if (info.size() > 0){
                for (String s : info) {
                    FileUtils.writeStringToFile(file, s+"\n", "UTF-8", true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取指定路径文件的总行数
     * @param filePath
     * @return
     */
    public static int getFileAllLines(String filePath) {
        try {
            File file = new File(filePath);
            return getFileAllLineNumbers(file);
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    public static int getFileAllLineNumbers(File file) {
        try {
            if (file.isFile()){
                FileReader fileReader = new FileReader(file);
                LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
                lineNumberReader.skip(Long.MAX_VALUE);
                int totalLineNumber = lineNumberReader.getLineNumber();
                lineNumberReader.close();
                return totalLineNumber +1;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    public static List getGeneratorListFromFile(File file, int lineNumber){
        List generatorList = new ArrayList();
        String lineStr = new String();
        try {
            JSONObject jsonObject;
            FileReader fileReader = new FileReader(file);
            LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
            int lineNum = 0;
            int totalLineNumber = getFileAllLineNumbers(file);
            if (lineNumber <= 0 || lineNumber > totalLineNumber){
                lineNumberReader.close();
                return generatorList;
            }else {
                while (true){
                    lineStr = lineNumberReader.readLine();
                    lineNum = lineNum + 1;
                    if (lineNum == lineNumber){
                        break;
                    }
                }
                lineNumberReader.close();
                jsonObject = JSONObject.parseObject(lineStr);
                jsonObject.forEach((k,v)->{
                    generatorList.add(k);
                });
                return generatorList;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return generatorList;
    }

    /**
     * 自动获取windows系统下当前应用的properties文件
     * @return File
     */
    @Deprecated
    public static File getPropertiesFileForWin(){
        String propertiesPath = new String();
        String propertiesPathPrefix = new String();
        try {
            StackTraceElement stackTraceElement = new Throwable().getStackTrace()[1];
            Class localClass = stackTraceElement.getClassName().getClass();
            String filePathName = localClass.getResource("/").getPath();
            File file = new File(filePathName);
            for (File file1 : file.listFiles()) {
                String[] names = file1.getPath().split("\\\\");
                if ("application.properties".equals(names[names.length-1])){
                    for (String line : FileUtils.readLines(file1, "UTF-8")) {
                        System.out.println(line);
                        if ("spring.profiles.active".equals(line.split("=")[0].trim())){
                            propertiesPathPrefix = "-"+line.split("=")[1].trim();
                            break;
                        }
                    }
                }else if (names[names.length-1].contains(".properties") && names[names.length-1] != "application.properties"){
                    propertiesPath = names[names.length-1];
                }
            }
            if (propertiesPathPrefix == null && "".equals(propertiesPathPrefix)){
                return new File(localClass.getResource("/"+propertiesPath).getPath());
            }else {
                return new File(localClass.getResource("/application"+propertiesPathPrefix+".properties").getPath());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取linux系统下项目路径下相对指定路径的配置文件
     * @param pathName 指定项目下配置文件的路径，可选，默认指定 ./config/application.properties
     *                 扫描优先级
     *                  指定的pathName地址
     *                  指定的pathName地址下的符合*.properties的文件
     *                   如果存在多个则根据application.properties中指定的文件
     *                   否则按照 pro > dev > test 原则获取
     *                   如果上述三种类型的properties均不存在，则返回空
     * @return
     */
    @Deprecated
    public static File getPropertiesFileForLinux(String... pathName){
        File realFile = null;
        try {
            String userPath = System.getProperty("user.dir");
            if (pathName.length > 0){
                File userPathFile = new File(Paths.get(userPath, pathName).toString());
                if (userPathFile.isFile()){
                    if (FileUtils.readLines(userPathFile, "UTF-8").contains("spring.profiles.active")){
                        for (String userFileLine : FileUtils.readLines(userPathFile, "UTF-8")) {
                            if ("spring.profiles.active".equals(userFileLine.split("=")[0].trim())){
                                String namePath = Paths.get(userPathFile.getParentFile().toString(), "application-"+userFileLine.split("=")[1].trim()+".properties").toString();
                                realFile = new File(namePath);
                                break;
                            }
                        }
                    }else {
                        realFile = userPathFile;
                    }
                }
            }else if (pathName.length == 0 || pathName == null){
                pathName[0] = "config";
                pathName[1] = "application.properties";
                File pathFile = new File(Paths.get(userPath, pathName).toString());
                if (pathFile.exists()){
                    if (FileUtils.readLines(pathFile, "UTF-8").contains("spring.profiles.active")) {
                        for (String lin : FileUtils.readLines(pathFile, "UTF-8")) {
                            if ("spring.profiles.active".equals(lin.split("=")[0].trim())){
                                String propertiesPrefix = lin.split("=")[1].trim();
                                pathName[1] = "application-"+propertiesPrefix+".properties";
                                realFile =  new File(Paths.get(userPath, pathName).toString());
                                break;
                            }
                        }
                    }else {
                        realFile = pathFile;
                    }
                }else if (!pathFile.exists()){
                    pathName[1] = "application-pro.properties";
                    File proPathFile = new File(Paths.get(userPath, pathName).toString());
                    if (proPathFile.exists()){
                        realFile = new File(Paths.get(userPath, pathName).toString());
                    }else {
                        pathName[1] = "application-dev.properties";
                        File devPathFile = new File(Paths.get(userPath, pathName).toString());
                        if (devPathFile.exists()){
                            realFile = new File(Paths.get(userPath, pathName).toString());
                        }else {
                            pathName[1] = "application-test.properties";
                            File testPathFile = new File(Paths.get(userPath, pathName).toString());
                            if (testPathFile.exists()){
                                realFile = new File(Paths.get(userPath, pathName).toString());
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return realFile;
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

    public static File getRecoverFileName(String templateName, String paramsNum, String path) throws Exception{
        String translateTimeToDate = DateUtil.translateTimeToDate(DateUtil.getLastDaysTimestamp(1), DateUtil.FORMAT_ENUM.NOSEGMENTATION_yMd.getValue());
        List<String> fileNames = Arrays.asList(
                templateName,
                translateTimeToDate,
                "00",
                DateUtil.translateTimeToDate(System.currentTimeMillis(), DateUtil.FORMAT_ENUM.NOSEGMENTATION_yMdHms.getValue()),
                HttpCommonUtil.hostName,
                paramsNum
        );
        String fileName = StringUtils.join(fileNames, "_") + ".dat.l";
        File file = FileUtils.getFile(path, translateTimeToDate, "recover", fileName);
        if (!file.getParentFile().exists()){
            FileUtils.forceMkdirParent(file);
        }
        return file;
    }

    public static String getLogFileName(String templateName, String paramsNum, String date_format, int date_num) throws Exception{
        if (StringUtils.isEmpty(date_format)){
            date_format = DateUtil.FORMAT_ENUM.NOSEGMENTATION_yMdHms.getValue();
        }
        String translateTimeToDate = DateUtil.translateTimeToDate(DateUtil.getLastDaysTimestamp(date_num), date_format);
        List<String> fileNames = Arrays.asList(
                templateName,
                translateTimeToDate,
                "00",
                DateUtil.translateTimeToDate(System.currentTimeMillis(), DateUtil.FORMAT_ENUM.NOSEGMENTATION_yMdHmsS.getValue()),
                HttpCommonUtil.hostName,
                paramsNum
        );
        return StringUtils.join(fileNames, "_") + ".dat.l";
    }

}
