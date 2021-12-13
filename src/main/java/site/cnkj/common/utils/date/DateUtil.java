package site.cnkj.common.utils.date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期工具类
 *
 * @author Liu XueWen, 2021/12/13
 */
public class DateUtil {

    /**
     * 日期格式枚举值
     */
    @Getter
    @AllArgsConstructor
    public enum FORMAT{
        BASE_TIME_YEAR("yyyy"),
        BASE_TIME_MONTH("MM"),
        BASE_TIME_DAY("dd"),
        BASE_TIME_HH("HH"),
        BASE_TIME_MINUTES("mm"),
        BASE_TIME_SECONDS("ss"),
        BASE_TIME_MILLIONS_SECONDS("SSS"),
        FULL_TIME_YMD("yyyy-MM-dd"),
        FULL_TIME_YMD_H("yyyy-MM-dd HH"),
        FULL_TIME_YMD_HM("yyyy-MM-dd HH:mm"),
        FULL_TIME_YMD_HMS("yyyy-MM-dd HH:mm:ss"),
        FULL_TIME_YMD_HMS_S("yyyy-MM-dd HH:mm:ss.SSS"),
        FULL_TIME_YM("yyyy-MM"),
        FULL_TIME_MD("MM-dd"),
        FULL_TIME_HMS("HH:mm:ss"),
        FULL_TIME_HM("HH:mm"),
        FULL_TIME_MS("mm:ss"),
        FULL_TIME_HMS_S("HH:mm:ss.SSS"),
        NO_SEGMENTATION_YM("yyyyMM"),
        NO_SEGMENTATION_YMD("yyyyMMdd"),
        NO_SEGMENTATION_HM("HHmm"),
        NO_SEGMENTATION_HMS("HHmmss"),
        NO_SEGMENTATION_HMS_S("HHmmssSSS"),
        NO_SEGMENTATION_YMD_HM("yyyyMMddHHmm"),
        NO_SEGMENTATION_YMD_HMS("yyyyMMddHHmmss"),
        NO_SEGMENTATION_YMD_HMS_S("yyyyMMddHHmmssSSS"),
        POINT_YMD("yyyy.MM.dd"),
        POINT_HMS("HH.mm.ss"),
        FULL_TIME_FORMAT_YMD("yyyy年MM月dd日"),
        FULL_TIME_FORMAT_HMS("HH时mm分ss秒"),
        FULL_TIME_FORMAT_YMD_HMS("yyyy年MM月dd日 HH时mm分ss秒"),
        FULL_TIME_FORMAT_YMD_HMS_S("yyyy年MM月dd日 HH时mm分ss秒SSS毫秒");

        private final String value;
    }

    /**
     * 日期字符串转日期格式
     * @param date 日期字符串
     * @param timeFormat 转换后的日期格式
     * @return date
     */
    public static Date dateStringToDate(String date, String timeFormat){
        try {
            if (StringUtils.isNotEmpty(date)){
                SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
                return sdf.parse(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 按照yyyy-MM-dd HH:mm:ss 格式格式化日期对象
     *
     * @param date 日期对象
     * @return 日期字符串
     */
    public static String dateToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT.FULL_TIME_YMD_HMS.getValue());
        return sdf.format(date);
    }

    /**
     * 获取指定日期的零点时间戳
     *
     * @param time 时间
     * @param timeFormat 时间对应格式
     * @return 零点时间戳(0:0:0)
     */
    public static long getTodayEarlyMorning(String time, String timeFormat){
        //当前时间毫秒数
        Long current = translateDateToTimestamp(time, timeFormat);
        if (null != current){
            //前一天的零点零分零秒的毫秒数
            long zero=current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset();
            return zero + 24*60*60*1000;
        }
        return 0L;
    }


    /**
     * 获取指定日期的晚上24点的时间戳
     *
     * @param time 时间
     * @param timeFormat 时间对应格式
     * @return 当天24点的时间戳(23:59:59)
     */
    public static long getTodayLaterMorning(String time, String timeFormat){
        //当前时间毫秒数
        Long current=translateDateToTimestamp(time, timeFormat);
        if (null != current){
            //前一天的零点零分零秒的毫秒数
            long zero=current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset();
            return zero + 24*60*60*1000*2 - 1;
        }
        return 0L;
    }


    /**
     * 获取当前日期的过去指定天数长度的零点时时间戳，包含当天
     * @param maxDays 3 (2018/10/11 0:0:0)
     * @return 2018/10/09 0:0:0 的时间戳
     */
    public static long getLastDaysTimestamp(int maxDays){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long time = calendar.getTime().getTime();
        return time - 24L * 60 * 60 * 1000 * maxDays;
    }

    /**
     * 获取过去指定时间的时间戳
     * @param pastTime 指定的时间数量 5
     * @param type 时间的类型 d日 H时 m分 s秒 S毫秒 不写默认单位秒
     * @param timeFormat 转换后的时间戳格式化的标准 不写默认格式 年-月-日 时:分:秒
     * @return 过去指定时间的时间戳
     */
    public static String getThePastTime(long pastTime, String type, String timeFormat){
        if (type == null){
            type = "s";
        }
        if (timeFormat == null){
            timeFormat = FORMAT.FULL_TIME_YMD_HMS_S.getValue();
        }
        long date = System.currentTimeMillis();
        long nowTime = 1L;
        if ("S".equals(type)){
            nowTime = date - pastTime;
        }else if ("s".equals(type)){
            nowTime = date - pastTime* 1000L;
        }else if ("m".equals(type)){
            nowTime = date - pastTime *1000*60;
        }else if ("H".equals(type)){
            nowTime = date - pastTime *1000*60*60;
        }else if ("d".equals(type)){
            nowTime = date - pastTime *1000*60*60*24;
        }
        return translateTimeToDate(nowTime, timeFormat);
    }

    /**
     * 获取过去指定时间的时间戳
     * 默认日期格式: com.acca.opra.mch.utils.DateUtils.FORMAT.FULL_TIME_YMD_HMS_S
     * 默认日期级别: 秒 s
     *
     * @param pastTime 指定的时间数量 5
     * @return 过去指定时间的时间戳
     */
    public static String getThePastTime(int pastTime){
        return getThePastTime(pastTime, null, null);
    }

    /**
     * 获取过去指定时间的时间戳
     * 默认日期格式: com.acca.opra.mch.utils.DateUtils.FORMAT.FULL_TIME_YMD_HMS_S
     *
     * @param pastTime 指定的时间数量 5
     * @param type 时间的类型 d日 H时 m分 s秒 S毫秒 不写默认单位秒
     * @return 过去指定时间的时间戳
     */
    public static String getThePastTime(int pastTime, String type){
        return getThePastTime(pastTime, type, null);
    }

    /**
     * 获取当前系统时间的时间戳
     * @return 时间戳
     */
    public static Long getCurrentTime(){
        return System.currentTimeMillis();
    }


    /**
     * 获取指定格式的当前时间
     *
     * @param timeFormat 日期格式
     * @return 当前时间
     */
    public static String getNowTimeByFormat(String timeFormat){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
        return simpleDateFormat.format(date);
    }

    /**
     * 转换时间戳位时间格式
     * @param timestamp 时间戳(long)
     * @param timeFormat 转换的时间格式
     * @return 时间
     */
    public static String translateTimeToDate(Long timestamp, String timeFormat){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
        return simpleDateFormat.format(timestamp);
    }

    /**
     * 转换时间格式
     * @param date 时间(date)
     * @param timeFormat 转换的时间格式
     * @return 时间
     */
    public static String translateDateToString(Date date, String timeFormat){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
        return simpleDateFormat.format(date);
    }

    /**
     * 字符串转成时间格式
     * @param time 字符串
     * @param timeFormat 转换格式
     * @return 时间格式
     */
    public static Date translateStringToDate(String time, String timeFormat){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
        Date da = null;
        try {
            da = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return da;
    }

    /**
     * 13位时间戳转date格式
     * @param timestamp 时间戳
     * @param timeFormat 时间格式
     * @return .
     */
    public static Date translateTimestampToDate(Long timestamp, String timeFormat){
        try {
            String date = translateTimeToDate(timestamp, timeFormat);
            if (StringUtils.isNotEmpty(date)){
                SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
                return sdf.parse(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 时间转时间戳
     * @param time 需要转换的时间
     * @param timeFormat 时间对应的格式
     * @return 13位时间戳
     */
    public static Long translateDateToTimestamp(String time, String timeFormat){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 序列化字符串为时间格式
     * @param time 需要转换的字符串 (20181011 10:11:12.013)
     * @param timeFormat 字符串对应的时间格式 (yyyyMMdd HH:mm:ss.SSS)
     * @param timeTranslate 转换后需要的字符串格式 (yyyy年M月dd日 HH时mm分ss秒SSS毫秒)
     * @return 2018年10月11日 10时11分12秒13毫秒
     */
    public static String serializationDate(String time, String timeFormat, String timeTranslate){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
        try {
            Date date = simpleDateFormat.parse(time);
            SimpleDateFormat dateFormat = new SimpleDateFormat(timeTranslate);
            return dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定天数前的日期集合，包含当天
     * @param num 天数
     * @param time 时间
     * @param timeFormat 时间格式
     * @param timeTranslate 输出格式
     * @return 日期集合
     */
    public static List<String> getDesignationDay(int num, String time, String timeFormat, String timeTranslate){
        List<String> days = new ArrayList<>();
        Long timestampTime = translateDateToTimestamp(time, timeFormat);
        if (null != timestampTime){
            for (int i = 0; i < num; i++) {
                long newTime = timestampTime - (long) i *24*60*60*1000;
                String today = translateTimeToDate(newTime, timeTranslate);
                days.add(today);
            }
        }
        return days;

    }

    /**
     * 当前时间与周期时间的时间差
     * 最大支持3600秒
     * 时间单位 秒
     *
     * @param executorTime 执行周期
     * @return 等待秒
     */
    public static long waitingTimeBySeconds(long executorTime){
        if (executorTime <= 3600 && executorTime >=0){
            //获取当前时间与周期时间的差
            long nowTime = System.currentTimeMillis();
            long timeDiscrepancy = Integer.parseInt(String.valueOf(nowTime%(executorTime*1000)/1000));
            return executorTime-timeDiscrepancy;
        }else {
            return 0;
        }
    }

    /**
     * 判断传入的字符串是时间格式还是时间戳格式
     * 时间戳格式根据设置的format转换成对应的时间格式
     * @param date 校验字符串
     * @param format 时间格式
     * @return 时间
     */
    public static String judgeTimeAndTimestamp(String date, String format){
        try {
            String time = "";
            if (date.length() == 14){
                //20190613171965
                time = date.substring(0, 4)
                        + "-"
                        + date.substring(4, 6)
                        + "-"
                        + date.substring(6, 8)
                        + " "
                        + date.substring(8, 10)
                        + ":"
                        + date.substring(10, 12)
                        + ":"
                        + date.substring(12, 14);
            }else if (date.length() == 13){
                //1560390240760
                time = translateTimeToDate(Long.valueOf(date), format);
            }
            return time;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 时间格式转换
     * @param time  2020-03-24T11:24:31.000Z
     * @return      2020-03-24 19:24:31
     * @throws ParseException e
     */
    public static String switchTime(String time) throws ParseException {
        SimpleDateFormat format2 = new SimpleDateFormat(FORMAT.FULL_TIME_YMD_HMS_S.getValue());
        //格式化的表达式
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        //是空格+UTC
        time = time.replace("Z", " UTC");
        Date data = format.parse(time);
        return format2.format(data);
    }

    /**
     * 转化时间格式
     * @param time  2020-01-15T15:16:23+08:00
     * @return      2020-01-15 15:16:23
     * @throws java.text.ParseException e
     */
    public static String switchLineTime(String time) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = df.parse(time);
        SimpleDateFormat df1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
        Date date1 =  df1.parse(date.toString());
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df2.format(date1);
    }


    /**
     * 转换String类型的Date格式数据为指定格式
     *
     * @param time Date 格式日期字符串
     * @param dateFormat 格式化后类型
     * @return 格式化后字符串
     * @throws ParseException e
     */
    public static String changeDateStringToFormatString(String time, String dateFormat) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        SimpleDateFormat format2 = new SimpleDateFormat(dateFormat);
        Date data = format.parse(time);
        return format2.format(data);
    }

    /**
     * 获取指定月份的第一天的当前时间
     * @param month 指定月（1 当前月加1）
     * @param format 转换后的格式
     * @return 转换后格式的时间
     */
    public static String getFirstDayByMonth(int month, String format){
        //获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, month);
        //设置为1号,当前日期既为本月第一天
        c.set(Calendar.DAY_OF_MONTH, 1);
        return translateDateToString(c.getTime(), format);
    }

    /**
     * 获取指定月份的第一天的当前时间
     * @param year 指定年
     * @param month 指定月（1 当前月加1）
     * @param format 转换后时间格式
     * @return 转换后的时间
     */
    public static String getFirstDayByMonth(int year, int month, String format){
        //获取当前月最后一天
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return translateDateToString(c.getTime(), format);
    }

    /**
     * 获取指定月的最后一天
     * @param month 指定月（1 当前月加1）
     * @param format 转换后时间格式
     * @return 转换后的时间
     */
    public static String getLastDayByMonth(int month, String format){
        //获取当前月最后一天
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return translateDateToString(c.getTime(), format);
    }

    /**
     * 计算月份区间
     *
     * @param startMonth 起始月
     * @param endMonth 结束月
     * @param inputFormat 输入日期格式
     * @param outputFormat 输出日期格式
     * @return 月份区间集合
     */
    public static List<String> computerMonth(String startMonth,
                                             String endMonth,
                                             String inputFormat,
                                             String outputFormat) {
        List<String> months = new ArrayList<>();
        Long startTimestamp = translateDateToTimestamp(startMonth, inputFormat);
        Long endTimestamp = translateDateToTimestamp(endMonth, inputFormat);
        if (null == startTimestamp || null == endTimestamp){
            throw  new NullPointerException("one of startMonth or endMonth parsed resulr is null.");
        }
        Calendar startDate = Calendar.getInstance();
        startDate.setTimeInMillis(startTimestamp);
        int year = startDate.get(Calendar.YEAR);
        int month = startDate.get(Calendar.MONTH) + 1;
        long currentTimestamp;
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        while (true){
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month - 1);
            currentTimestamp = calendar.getTimeInMillis();
            if (currentTimestamp > endTimestamp){
                break;
            }
            months.add(translateTimeToDate(currentTimestamp, outputFormat));
            month = month + 1;
            if (month > 12){
                year = year + 1;
                month = 1;
            }
        }
        return months;
    }

    /**
     * 计算两个日期之间的天数，不包含结束日期当天.
     * 如果结束日期小于开始日期，则返回负数天数.
     *
     * @param startDay 开始日期
     * @param endDay 结束日期
     * @param format 日期格式，开始日期和结束日期格式必须保持一致
     * @return 天数.
     */
    public static long computerDaysBetween(String startDay, String endDay, String format){
        Long startTimestamp = translateDateToTimestamp(startDay, format);
        Long endTimestamp = translateDateToTimestamp(endDay, format);
        if (null != endTimestamp && null != startTimestamp){
            long change = endTimestamp - startTimestamp;
            return change / (3600 * 24 * 1000);
        }
        throw new NullPointerException("one of startDay or endDay is null.");
    }

    /**
     * 计算两个日期之间的天数，不包含结束日期当天.
     * 如果结束日期小于开始日期，则返回负数天数.
     *
     * @param startDay 开始日期
     * @param endDay 结束日期
     * @param format 日期格式，开始日期和结束日期格式必须保持一致
     * @return 天数.
     */
    public static long computerDaysBetween(Date startDay, Date endDay, String format){
        String startString = translateDateToString(startDay, format);
        String endString = translateDateToString(endDay, format);
        return computerDaysBetween(startString, endString, format);
    }

    /**
     * 判断当前时间是否在范围内.
     * 包含开始时间和结束时间的时间点.
     *
     * @param format 时间格式
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param checkTime 待判断时间
     * @return 判断结果，范围内 true 不在范围内 false
     */
    public static boolean checkDateInRange(String format, String startTime, String endTime, String checkTime){
        Long startTimestamp = translateDateToTimestamp(startTime, format);
        Long endTimestamp = translateDateToTimestamp(endTime, format);
        Long checkTimestamp = translateDateToTimestamp(checkTime, format);
        if (null == startTimestamp || null == endTimestamp || null == checkTimestamp){
            throw new NullPointerException();
        }
        return startTimestamp <= checkTimestamp && checkTimestamp <= endTimestamp;
    }

    /**
     * 判断当前时间是否在范围内.
     * 包含开始时间和结束时间的时间点.
     *
     * @param format 时间格式
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param checkTime 待判断时间
     * @return 判断结果，范围内 true 不在范围内 false
     */
    public static boolean checkDateInRange(String format, Date startTime, Date endTime, Date checkTime){
        String startDate = translateDateToString(startTime, format);
        String endDate = translateDateToString(endTime, format);
        String checkDate = translateDateToString(checkTime, format);
        return checkDateInRange(format, startDate, endDate, checkDate);
    }

    /**
     * 判断当前时间是否在范围内.
     * 包含开始时间和结束时间的时间点.
     *      如果checkEnd=true,则rangeTime为结束时间，表示1970-01-01 00:00:00 至 rangeTime的时间
     *      如果checkEnd=false,则rangeTime为开始时间时间，表示rangeTime 至 系统当前时间
     *
     * @param format 时间格式
     * @param rangeTime 开始/结束时间
     * @param checkTime 待判断时间
     * @param checkEnd 当前范围为结束时间还是开始时间
     * @return 判断结果，范围内 true 不在范围内 false
     */
    public static boolean checkDateInRange(String format, Date rangeTime, Date checkTime, boolean checkEnd){
        String rangeDate = translateDateToString(rangeTime, format);
        String checkDate = translateDateToString(checkTime, format);
        if (checkEnd){
            Calendar calendar = Calendar.getInstance();
            calendar.set(1970, Calendar.JANUARY, 1, 0, 0, 0);
            String judgeDate = translateDateToString(calendar.getTime(), format);
            return checkDateInRange(format, judgeDate, rangeDate, checkDate);
        }else {
            Date date = new Date();
            String judgeDate = translateDateToString(date, format);
            return checkDateInRange(format, rangeDate, judgeDate, checkDate);
        }
    }

}

