package site.cnkj.common.utils.logger;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import site.cnkj.common.object.ReasonEnumeration;
import site.cnkj.common.utils.date.DateUtil;
import site.cnkj.common.utils.http.HttpCommonUtil;
import site.cnkj.common.utils.io.StringUtil;

/*
 * @version 1.0 created by LXW on 2019/4/1 9:13
 */
@Component
public class LoggerUtil {

    @Value("${spring.application.name}")
    private String serviceName;//服务名

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
        public static final String FAILED_ANALYZESOURCE = "100004";//校验数据失败

        public static final String ERROR = "200000";//错误
        public static final String DEBUG = "300000";//调试，开发
        public static final String WARN = "400000";//警告
        public static final String WARN_SOURCE = "400001";//警告,针对异常数据情况
    }

    public static class Type{
        public static final String ERROR_MESSAGE = "error_message";
        public static final String SCHEDULE = "schedule";
        public static final String BUSINESS = "business";
    }

    /**
     * 数据处理方式枚举值
     */
    public static class HandleEnumeration{
        public static final String discard = "0";//丢弃
        public static final String replicate = "1";//复制
    }

    @Data
    public static class MonitorEntity {
        private String hostName = HttpCommonUtil.getLocalHostName();//机器名
        private String timestamp = DateUtil.translateTimeToDate(DateUtil.getCurrentTime(), DateUtil.FORMAT.FULLTIMEBY_yMdHmsS);//时间
        private String time = DateUtil.translateTimeToDate(DateUtil.getCurrentTime(), DateUtil.FORMAT.FULLTIMEBY_yMdHmsS);//时间
        private String level;//级别
        @Nullable private String status;//状态枚举值
        @Nullable private String type;//类型
        @Nullable private String info;//信息
        @Nullable private String logName;//标识
        @Nullable private String topicName;//topic名
        @Nullable private String stackTrace;//异常信息，如果没有则为NULL
        @Nullable private String filterName;//数据特殊处理条件名称
        @Nullable private String prefix;//数据模版前缀
        @Nullable private String source;//原始数据
        @Nullable private String message;//解析处理后的数据
        @Nullable private String templateName;//模版名称
        @Nullable private String serviceName;//服务名
        @Nullable private String handleEnumeration;//枚举值（0 剪切/1 复制）
        @Nullable private String reasonEnumeration;//异常原因枚举值
        @Nullable private String separatorNum;//分隔符数量
        @Nullable private String exceptionKeyWordName;//异常字段名字
        @Nullable private String exceptionKeyWordValue;//异常字段值
        @Nullable private String exceptionKeyWordTemplateArea;//异常字段在模版中的位数

        private void setReasonEnumeration(String reasonEnumeration) {
            this.reasonEnumeration = reasonEnumeration;
            this.info = ReasonEnumeration.reasonEnumeration.get(getReasonEnumeration());
        }

        @Override
        public String toString() {
            return JSONObject.toJSONString(this);
        }
    }


    /**
     * 处理异常数据情况的告警
     * @param logName id或uri，标识
     * @param topic topic名字
     * @param filterName 过滤条件名字
     * @param prefix 模版前缀
     * @param source 原始数据
     * @param message 解析后数据
     * @param templateName 模版名称
     * @param handleEnumeration 数据处理枚举值
     * @param reasonEnumeration 异常原因枚举值
     * @param exceptionKeyWordName 异常字段名字 同一个字段上可能存在多个名字，以"|"拼接  params.videoId|params.mvId|params.concertId
     * @param exceptionKeyWordValue 异常字段名字 同一个字段上可能存在多个名字，以"|"拼接  params.videoId|params.mvId|params.concertId
     * @param exceptionKeyWordTemplateArea 异常字段在模版中的位数
     */
    public static void monitor(String logName,
                               String topic,
                               String filterName,
                               String prefix,
                               String source,
                               String message,
                               String templateName,
                               String handleEnumeration,
                               String reasonEnumeration,
                               String separatorNum,
                               String exceptionKeyWordName,
                               String exceptionKeyWordValue,
                               String exceptionKeyWordTemplateArea){
        MonitorEntity monitorEntity = new MonitorEntity();
        monitorEntity.setLevel(Level.WARN);
        monitorEntity.setStatus(Status.WARN_SOURCE);
        monitorEntity.setType(Type.BUSINESS);
        monitorEntity.setLogName(logName);
        monitorEntity.setTopicName(topic);
        monitorEntity.setFilterName(filterName);
        monitorEntity.setPrefix(prefix);
        monitorEntity.setSource(source);
        monitorEntity.setMessage(message);
        monitorEntity.setTemplateName(templateName);
        monitorEntity.setHandleEnumeration(handleEnumeration);
        monitorEntity.setReasonEnumeration(reasonEnumeration);
        monitorEntity.setSeparatorNum(separatorNum);
        monitorEntity.setExceptionKeyWordName(exceptionKeyWordName);
        monitorEntity.setExceptionKeyWordValue(exceptionKeyWordValue);
        monitorEntity.setExceptionKeyWordTemplateArea(exceptionKeyWordTemplateArea);
        LOGGER.warn(monitorEntity.toString());
    }

    public static void message(String level, String info, String status, String type, String logName, String topicName, Exception e, String id, String uri, String filterName, String prefix, String source, String message, String templateName){
        MonitorEntity monitorEntity = new MonitorEntity();
        monitorEntity.setLevel(level);
        monitorEntity.setInfo(info);
        monitorEntity.setStatus(status);
        monitorEntity.setType(type);
        monitorEntity.setLogName(logName);
        monitorEntity.setTopicName(topicName);
        monitorEntity.setStackTrace(StringUtil.formatException(e));
        monitorEntity.setFilterName(filterName);
        monitorEntity.setPrefix(prefix);
        monitorEntity.setSource(source);
        monitorEntity.setMessage(message);
        monitorEntity.setTemplateName(templateName);
        if ((Level.ERROR).equals(level)){
            LOGGER.error(monitorEntity.toString());
        }else if ((Level.INFO).equals(level)){
            LOGGER.info(monitorEntity.toString());
        }else if ((Level.WARN).equals(level)){
            LOGGER.warn(monitorEntity.toString());
        }else if ((Level.DEBUG).equals(level)){
            LOGGER.debug(monitorEntity.toString());
        }
    }

    public static void message(MonitorEntity monitorEntity){
        String level = monitorEntity.getLevel();
        switch(level){
            case Level.DEBUG:
                LOGGER.debug(monitorEntity.toString());
            case Level.ERROR:
                LOGGER.error(monitorEntity.toString());
            case Level.WARN:
                LOGGER.warn(monitorEntity.toString());
            default:
                LOGGER.info(monitorEntity.toString());
        }
    }

    public static void warn(MonitorEntity monitorEntity){
        monitorEntity.setLevel(Level.WARN);
        monitorEntity.setStatus(Status.WARN);
        LOGGER.warn(monitorEntity.toString());
    }

    public static void warn(String info, String type, String logName, String topicName){
        MonitorEntity monitorEntity = new MonitorEntity();
        monitorEntity.setLevel(Level.WARN);
        monitorEntity.setInfo(info);
        monitorEntity.setStatus(Status.WARN);
        monitorEntity.setType(type);
        monitorEntity.setLogName(logName);
        monitorEntity.setTopicName(topicName);
        LOGGER.warn(monitorEntity.toString());
    }

    public static void warn(String info, String type, String logName){
        MonitorEntity monitorEntity = new MonitorEntity();
        monitorEntity.setLevel(Level.WARN);
        monitorEntity.setInfo(info);
        monitorEntity.setStatus(Status.WARN);
        monitorEntity.setType(type);
        monitorEntity.setLogName(logName);
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

    public static void error(MonitorEntity monitorEntity){
        monitorEntity.setLevel(Level.ERROR);
        monitorEntity.setType(Type.ERROR_MESSAGE);
        LOGGER.error(monitorEntity.toString());
    }

    public static void error(String info, String status, String logName, String topicName){
        MonitorEntity monitorEntity = new MonitorEntity();
        monitorEntity.setLevel(Level.ERROR);
        monitorEntity.setInfo(info);
        monitorEntity.setStatus(status);
        monitorEntity.setType(Type.ERROR_MESSAGE);
        monitorEntity.setLogName(logName);
        monitorEntity.setTopicName(topicName);
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

    public static void error(String info, Exception e, String status, String logName, String topicName){
        MonitorEntity monitorEntity = new MonitorEntity();
        monitorEntity.setLevel(Level.ERROR);
        monitorEntity.setInfo(info);
        monitorEntity.setStatus(status);
        monitorEntity.setType(Type.ERROR_MESSAGE);
        monitorEntity.setLogName(logName);
        monitorEntity.setTopicName(topicName);
        monitorEntity.setStackTrace(StringUtil.formatException(e));
        LOGGER.error(monitorEntity.toString());
    }

    public static void error(String info, Exception e, String logName, String topicName){
        MonitorEntity monitorEntity = new MonitorEntity();
        monitorEntity.setLevel(Level.ERROR);
        monitorEntity.setInfo(info);
        monitorEntity.setStatus(Status.ERROR);
        monitorEntity.setType(Type.ERROR_MESSAGE);
        monitorEntity.setLogName(logName);
        monitorEntity.setTopicName(topicName);
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

    public static void error(String info, Exception e, String source, String result, String topic, String logName){
        MonitorEntity monitorEntity = new MonitorEntity();
        monitorEntity.setLevel(Level.ERROR);
        monitorEntity.setInfo(info);
        monitorEntity.setStatus(Status.ERROR);
        monitorEntity.setType(Type.ERROR_MESSAGE);
        monitorEntity.setStackTrace(StringUtil.formatException(e));
        monitorEntity.setLogName(logName);
        monitorEntity.setMessage(result);
        monitorEntity.setSource(source);
        monitorEntity.setTopicName(topic);
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

    public static void info(MonitorEntity monitorEntity){
        monitorEntity.setLevel(Level.INFO);
        monitorEntity.setStatus(Status.SUCCESS);
        monitorEntity.setType(Type.BUSINESS);
        LOGGER.info(monitorEntity.toString());
    }

    public static void info(String info, String logName, String topicName){
        MonitorEntity monitorEntity = new MonitorEntity();
        monitorEntity.setLevel(Level.INFO);
        monitorEntity.setInfo(info);
        monitorEntity.setStatus(Status.SUCCESS);
        monitorEntity.setType(Type.BUSINESS);
        monitorEntity.setLogName(logName);
        monitorEntity.setTopicName(topicName);
        LOGGER.info(monitorEntity.toString());
    }

    public static void info(String info, String logName){
        MonitorEntity monitorEntity = new MonitorEntity();
        monitorEntity.setLevel(Level.INFO);
        monitorEntity.setInfo(info);
        monitorEntity.setStatus(Status.SUCCESS);
        monitorEntity.setType(Type.BUSINESS);
        monitorEntity.setLogName(logName);
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

    public static void debug(String info, String status, String logName, String topicName){
        MonitorEntity monitorEntity = new MonitorEntity();
        monitorEntity.setLevel(Level.DEBUG);
        monitorEntity.setInfo(info);
        monitorEntity.setStatus(status);
        monitorEntity.setType(Type.BUSINESS);
        monitorEntity.setLogName(logName);
        monitorEntity.setTopicName(topicName);
        LOGGER.debug(monitorEntity.toString());
    }

    public static void debug(String info, String status, String logName){
        MonitorEntity monitorEntity = new MonitorEntity();
        monitorEntity.setLevel(Level.DEBUG);
        monitorEntity.setInfo(info);
        monitorEntity.setStatus(status);
        monitorEntity.setType(Type.BUSINESS);
        monitorEntity.setLogName(logName);
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
