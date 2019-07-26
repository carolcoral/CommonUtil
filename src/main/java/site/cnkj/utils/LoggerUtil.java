package site.cnkj.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

/*
 * @version 1.0 created by LXW on 2019/4/1 9:13
 */
public class LoggerUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerUtil.class);

    public static class Level{
        public static final String INFO = "INFO";
        public static final String WARN = "WARN";
        public static final String ERROR = "ERROR";
        public static final String DEBUG = "DEBUG";
    }

    public static class Status{
        public static final String SUCCESS = "000000";//成功

        public static final String FAILED = "100000";//执行失败
        public static final String FAILED_INSERTMD5 = "100001";//插入流水号失败
        public static final String FAILED_VERIFYSEPARATOR = "100002";//校验分隔符数量失败
        public static final String FAILED_INCLUDDIRTYDATA = "100003";//校验脏数据失败

        public static final String ERROR = "200000";//错误
        public static final String DEBUG = "300000";//调试，开发
        public static final String WARN = "400000";//警告
    }

    public static class Type{
        public static final String ERROR_MESSAGE = "error_message";
        public static final String SCHEDULE = "schedule";
        public static final String BUSINESS = "business";
    }

    @Data
    public static class MonitorEntity {
        private String hostName = HttpCommonUtil.getLocalHostName();//机器名
        private String timestamp = DateUtil.translateTimeToDate(DateUtil.getCurrentTime(), DateUtil.DATEFORMATE.FULLTIMEBY_yMdHmsS);//时间
        private String time = DateUtil.translateTimeToDate(DateUtil.getCurrentTime(), DateUtil.DATEFORMATE.FULLTIMEBY_yMdHmsS);//时间
        private String level;//级别
        @Nullable private String status;//状态枚举值
        @Nullable private String type;//类型
        @Nullable private String info;//信息
        @Nullable private Object customField;//自定义字段
        @Nullable private String stackTrace;//异常信息，如果没有则为NULL

        @Override
        public String toString() {
            return JSONObject.toJSONString(this);
        }
    }

    public static void message(String level, String info, String status, String type, Object customField, Exception stackTrace){
        MonitorEntity monitorEntity = new MonitorEntity();
        monitorEntity.setLevel(level);
        monitorEntity.setInfo(info);
        monitorEntity.setStatus(status);
        monitorEntity.setType(type);
        monitorEntity.setCustomField(customField);
        monitorEntity.setStackTrace(StringUtil.formatException(stackTrace));
        if (level.equals(Level.ERROR)){
            LOGGER.error(monitorEntity.toString());
        }else if (level.equals(Level.INFO)){
            LOGGER.info(monitorEntity.toString());
        }else if (level.equals(Level.WARN)){
            LOGGER.warn(monitorEntity.toString());
        }else if (level.equals(Level.DEBUG)){
            LOGGER.debug(monitorEntity.toString());
        }
    }

    public static void warn(String info, String type, Object customField){
        MonitorEntity monitorEntity = new MonitorEntity();
        monitorEntity.setLevel(Level.WARN);
        monitorEntity.setInfo(info);
        monitorEntity.setStatus(Status.WARN);
        monitorEntity.setType(type);
        monitorEntity.setCustomField(customField);
        LOGGER.warn(monitorEntity.toString());
    }

    public static void warn(String info, String type){
        MonitorEntity monitorEntity = new MonitorEntity();
        monitorEntity.setLevel(Level.WARN);
        monitorEntity.setInfo(info);
        monitorEntity.setStatus(Status.WARN);
        monitorEntity.setType(type);
        LOGGER.warn(monitorEntity.toString());
    }

    public static void warn(String info){
        MonitorEntity monitorEntity = new MonitorEntity();
        monitorEntity.setLevel(Level.WARN);
        monitorEntity.setInfo(info);
        monitorEntity.setStatus(Status.WARN);
        monitorEntity.setType(Type.BUSINESS);
        LOGGER.warn(monitorEntity.toString());
    }

    public static void error(String info, String status, Object customField){
        MonitorEntity monitorEntity = new MonitorEntity();
        monitorEntity.setLevel(Level.ERROR);
        monitorEntity.setInfo(info);
        monitorEntity.setStatus(status);
        monitorEntity.setType(Type.ERROR_MESSAGE);
        monitorEntity.setCustomField(customField);
        LOGGER.error(monitorEntity.toString());
    }

    public static void error(String info){
        MonitorEntity monitorEntity = new MonitorEntity();
        monitorEntity.setLevel(Level.ERROR);
        monitorEntity.setInfo(info);
        monitorEntity.setStatus(Status.ERROR);
        monitorEntity.setType(Type.ERROR_MESSAGE);
        LOGGER.error(monitorEntity.toString());
    }

    public static void error(String info, Exception e, String status, Object customField){
        MonitorEntity monitorEntity = new MonitorEntity();
        monitorEntity.setLevel(Level.ERROR);
        monitorEntity.setInfo(info);
        monitorEntity.setStatus(status);
        monitorEntity.setType(Type.ERROR_MESSAGE);
        monitorEntity.setCustomField(customField);
        monitorEntity.setStackTrace(StringUtil.formatException(e));
        LOGGER.error(monitorEntity.toString());
    }

    public static void error(String info, Exception e, Object customField){
        MonitorEntity monitorEntity = new MonitorEntity();
        monitorEntity.setLevel(Level.ERROR);
        monitorEntity.setInfo(info);
        monitorEntity.setStatus(Status.ERROR);
        monitorEntity.setType(Type.ERROR_MESSAGE);
        monitorEntity.setCustomField(customField);
        monitorEntity.setStackTrace(StringUtil.formatException(e));
        LOGGER.error(monitorEntity.toString());
    }

    public static void error(String info, Exception e, String status){
        MonitorEntity monitorEntity = new MonitorEntity();
        monitorEntity.setLevel(Level.ERROR);
        monitorEntity.setInfo(info);
        monitorEntity.setStatus(status);
        monitorEntity.setType(Type.ERROR_MESSAGE);
        monitorEntity.setStackTrace(StringUtil.formatException(e));
        LOGGER.error(monitorEntity.toString());
    }

    public static void error(String info, Exception e){
        MonitorEntity monitorEntity = new MonitorEntity();
        monitorEntity.setLevel(Level.ERROR);
        monitorEntity.setInfo(info);
        monitorEntity.setStatus(Status.ERROR);
        monitorEntity.setType(Type.ERROR_MESSAGE);
        monitorEntity.setStackTrace(StringUtil.formatException(e));
        LOGGER.error(monitorEntity.toString());
    }

    public static void error(String info, String status){
        MonitorEntity monitorEntity = new MonitorEntity();
        monitorEntity.setLevel(Level.ERROR);
        monitorEntity.setInfo(info);
        monitorEntity.setStatus(status);
        monitorEntity.setType(Type.ERROR_MESSAGE);
        LOGGER.error(monitorEntity.toString());
    }

    public static void info(String info, Object customField){
        MonitorEntity monitorEntity = new MonitorEntity();
        monitorEntity.setLevel(Level.INFO);
        monitorEntity.setInfo(info);
        monitorEntity.setStatus(Status.SUCCESS);
        monitorEntity.setType(Type.BUSINESS);
        monitorEntity.setCustomField(customField);
        LOGGER.info(monitorEntity.toString());
    }

    public static void info(String info){
        MonitorEntity monitorEntity = new MonitorEntity();
        monitorEntity.setLevel(Level.INFO);
        monitorEntity.setInfo(info);
        monitorEntity.setStatus(Status.SUCCESS);
        monitorEntity.setType(Type.BUSINESS);
        LOGGER.info(monitorEntity.toString());
    }

    public static void debug(String info, String status, Object customField){
        MonitorEntity monitorEntity = new MonitorEntity();
        monitorEntity.setLevel(Level.DEBUG);
        monitorEntity.setInfo(info);
        monitorEntity.setStatus(status);
        monitorEntity.setType(Type.BUSINESS);
        monitorEntity.setCustomField(customField);
        LOGGER.debug(monitorEntity.toString());
    }

    public static void debug(String info, String status){
        MonitorEntity monitorEntity = new MonitorEntity();
        monitorEntity.setLevel(Level.DEBUG);
        monitorEntity.setInfo(info);
        monitorEntity.setStatus(status);
        monitorEntity.setType(Type.BUSINESS);
        LOGGER.debug(monitorEntity.toString());
    }

    public static void debug(String info){
        MonitorEntity monitorEntity = new MonitorEntity();
        monitorEntity.setLevel(Level.DEBUG);
        monitorEntity.setInfo(info);
        monitorEntity.setType(Type.BUSINESS);
        LOGGER.debug(monitorEntity.toString());
    }

}
