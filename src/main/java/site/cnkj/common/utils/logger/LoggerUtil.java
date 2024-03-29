package site.cnkj.common.utils.logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import site.cnkj.common.utils.bean.ReflectionUtil;
import site.cnkj.common.utils.date.DateUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * 日志打印
 *
 * @author Liu XueWen, 2021-12-06
 * @version EasyUtil v0.3.0
 **/
@Slf4j
public class LoggerUtil {

    enum Level{
        INFO("INFO"),
        WARN("WARN"),
        ERROR("ERROR"),
        DEBUG("DEBUG");

        private final String value;

        Level(String value){
            this.value = value;
        }
    }

    private static final String HOST_NAME = getHostName();

    private static String getHostName(){
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
            return address.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 格式化Exception为String
     * @param e Exception
     * @return e.toString
     */
    private static String formatException(Exception e){
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        String str = sw.toString();
        return StringEscapeUtils.escapeJava(str);
    }

    @Data
    private static class LoggerEntity {
        private String traceId = getMDC();
        private String requestId = MDC.get("requestId");
        private String hostName = HOST_NAME;//机器名
        private String time = DateUtil.translateDateToString(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");//时间
        private Long timestamp = new Date().getTime();
        private String className = new Throwable().getStackTrace()[4].getClassName();
        private String methodName = new Throwable().getStackTrace()[4].getMethodName();
        private String level;//级别
        @Nullable private String info;//信息
        @Nullable private String stackTrace;//异常信息，如果没有则为NULL

        @Override
        public String toString() {
            return JSON.toJSONString(this);
        }
    }

    private static final String TRACE_ID = "TraceID";

    static String getMDC(){
        if (StringUtils.isEmpty(MDC.get(TRACE_ID))){
            String traceID = "TraceID: " + UUID.randomUUID().toString();
            MDC.put(TRACE_ID, traceID);
        }
        return MDC.get(TRACE_ID).split(":")[1].trim();
    }

    static void checkFields(Map<String, Object> fields){
        for (Field field : ReflectionUtil.getAllFieldByClazz(LoggerEntity.class)) {
            if (field.isSynthetic()){
                continue;
            }
            if (fields.containsKey(field.getName())){
                throw new IllegalArgumentException("current field can not be use as key: " + field.getName());
            }
        }
    }

    private static void logger(Level level, String source){
        if (Level.INFO.value.equals(level.value)){
            log.info(source);
        }else if (Level.WARN.value.equals(level.value)){
            log.warn(source);
        }else if (Level.ERROR.value.equals(level.value)){
            log.error(source);
        }else if (Level.DEBUG.value.equals(level.value)){
            log.debug(source);
        }
    }

    private static void message(Level level, String message){
        LoggerEntity loggerEntity = new LoggerEntity();
        loggerEntity.setLevel(level.value);
        loggerEntity.setInfo(message);
        logger(level, loggerEntity.toString());
    }

    private static void map(Level level, String message, Map<String, Object> fields){
        checkFields(fields);
        LoggerEntity loggerEntity = new LoggerEntity();
        loggerEntity.setLevel(level.value);
        loggerEntity.setInfo(message);
        JSONObject jsonSource = JSON.parseObject(loggerEntity.toString());
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            jsonSource.put(entry.getKey(), entry.getValue());
        }
        logger(level, JSON.toJSONString(jsonSource));
    }

    private static void throwable(Level level, String message, Throwable throwable){
        LoggerEntity loggerEntity = new LoggerEntity();
        loggerEntity.setLevel(level.value);
        loggerEntity.setInfo(message);
        loggerEntity.setStackTrace(throwable.getMessage());
        logger(level, loggerEntity.toString());
    }

    private static void exception(Level level, String message, Exception e){
        LoggerEntity loggerEntity = new LoggerEntity();
        loggerEntity.setLevel(level.value);
        loggerEntity.setInfo(message);
        loggerEntity.setStackTrace(formatException(e));
        logger(level, loggerEntity.toString());
    }

    private static void mapThrowable(Level level, String message, Map<String, Object> fields, Throwable throwable){
        checkFields(fields);
        LoggerEntity loggerEntity = new LoggerEntity();
        loggerEntity.setLevel(level.value);
        loggerEntity.setInfo(message);
        loggerEntity.setStackTrace(throwable.getMessage());
        JSONObject jsonSource = JSON.parseObject(loggerEntity.toString());
        jsonSource.putAll(fields);
        logger(level, JSON.toJSONString(jsonSource));
    }

    private static void mapException(Level level, String message, Map<String, Object> fields, Exception e){
        checkFields(fields);
        LoggerEntity loggerEntity = new LoggerEntity();
        loggerEntity.setLevel(level.value);
        loggerEntity.setInfo(message);
        loggerEntity.setStackTrace(formatException(e));
        JSONObject jsonSource = JSON.parseObject(loggerEntity.toString());
        jsonSource.putAll(fields);
        logger(level, JSON.toJSONString(jsonSource));
    }

    private static String formatInputString(String message, Object... args){
        StringBuilder stringBuilder = new StringBuilder();
        String[] messages = message.split("\\{\\}", -1);
        for (int i = 0; i < messages.length; i++) {
            stringBuilder.append(messages[i]);
            if (i < args.length){
                Object arg = args[i];
                stringBuilder.append(String.valueOf(arg));
            }
        }
        return stringBuilder.toString();
    }

    public static void info(String message, Object... args){
        String inputString = formatInputString(message, args);
        message(Level.INFO, inputString);
    }

    public static void info(String message){
        message(Level.INFO, message);
    }

    public static void info(String message, Map<String, Object> fields){
        map(Level.INFO, message, fields);
    }

    public static void info(String message, Throwable throwable){
        throwable(Level.INFO, message, throwable);
    }

    public static void info(String message, Exception e){
        exception(Level.INFO, message, e);
    }

    public static void info(String message, Map<String, Object> fields, Throwable throwable){
        mapThrowable(Level.INFO, message, fields, throwable);
    }

    public static void info(String message, Map<String, Object> fields, Exception e){
        mapException(Level.INFO, message, fields, e);
    }

    public static void warn(String message){
        message(Level.WARN, message);
    }

    public static void warn(String message, Object... args){
        String inputString = formatInputString(message, args);
        message(Level.WARN, inputString);
    }

    public static void warn(String message, Map<String, Object> fields){
        fields.remove("data");
        map(Level.WARN, message, fields);
    }

    public static void warn(String message, Throwable throwable){
        throwable(Level.WARN, message, throwable);
    }

    public static void warn(String message, Exception e){
        exception(Level.WARN, message, e);
    }

    public static void warn(String message, Map<String, Object> fields, Throwable throwable){
        mapThrowable(Level.WARN, message, fields, throwable);
    }

    public static void warn(String message, Map<String, Object> fields, Exception e){
        mapException(Level.WARN, message, fields, e);
    }

    public static void debug(String message){
        message(Level.DEBUG, message);
    }

    public static void debug(String message, Object... args){
        String inputString = formatInputString(message, args);
        message(Level.DEBUG, inputString);
    }

    public static void debug(String message, Map<String, Object> fields){
        map(Level.DEBUG, message, fields);
    }

    public static void debug(String message, Throwable throwable){
        throwable(Level.DEBUG, message, throwable);
    }

    public static void debug(String message, Exception e){
        exception(Level.DEBUG, message, e);
    }

    public static void debug(String message, Map<String, Object> fields, Throwable throwable){
        mapThrowable(Level.DEBUG, message, fields, throwable);
    }

    public static void debug(String message, Map<String, Object> fields, Exception e){
        mapException(Level.DEBUG, message, fields, e);
    }

    public static void error(String message){
        message(Level.ERROR, message);
    }

    public static void error(String message, Object... args){
        String inputString = formatInputString(message, args);
        message(Level.ERROR, inputString);
    }

    public static void error(String message, Map<String, Object> fields){
        map(Level.ERROR, message, fields);
    }

    public static void error(String message, Throwable throwable){
        throwable(Level.ERROR, message, throwable);
    }

    public static void error(String message, Exception e){
        exception(Level.ERROR, message, e);
    }

    public static void error(String message, Map<String, Object> fields, Throwable throwable){
        mapThrowable(Level.ERROR, message, fields, throwable);
    }

    public static void error(String message, Map<String, Object> fields, Exception e){
        mapException(Level.ERROR, message, fields, e);
    }

}
