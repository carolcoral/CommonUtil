package site.cnkj.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 * @version 1.0 created by LXW on 2018/11/8 16:11
 */
public class DateUtil {

    public static class DATEFORMATE{
        //基本单位 年
        public static final String BASETIME_yyyy = "yyyy";
        //基本单位 月
        public static final String BASETIME_MM = "MM";
        //基本单位 日
        public static final String BASETIME_dd = "dd";
        //基本单位 时
        public static final String BASETIME_HH = "HH";
        //基本单位 分
        public static final String BASETIME_mm = "mm";
        //基本单位 秒
        public static final String BASETIME_ss = "ss";
        //基本单位 毫秒
        public static final String BASETIME_SSS = "SSS";
        //年-月-日
        public static final String FULLTIMEBY_yMd = "yyyy-MM-dd";
        //年-月-日 时
        public static final String FULLTIMEBY_yMdH = "yyyy-MM-dd HH";
        //年-月-日 时:分
        public static final String FULLTIMEBY_yMdHm = "yyyy-MM-dd HH:mm";
        //年-月
        public static final String FULLTIMEBY_yM = "yyyy-MM";
        //月-日
        public static final String FULLTIMEBY_Md = "MM-dd";
        //时:分:秒
        public static final String FULLTIMEBY_Hms = "HH:mm:ss";
        //时:分
        public static final String FULLTIMEBY_Hm = "HH:mm";
        //分:秒
        public static final String FULLTIMEBY_ms = "mm:ss";
        //时:分:秒.毫秒
        public static final String FULLTIMEBY_HmsS = "HH:mm:ss.SSS";
        //年月日 无分割
        public static final String NOSEGMENTATION_yM = "yyyyMM";

        public static final String NOSEGMENTATION_yMd = "yyyyMMdd";
        //时分 无分割
        public static final String NOSEGMENTATION_Hm = "HHmm";
        //时分秒 无分割
        public static final String NOSEGMENTATION_Hms = "HHmmss";
        //时分秒毫秒 无分割
        public static final String NOSEGMENTATION_HmsS = "HHmmssSSS";
        //年月日时分 无分割
        public static final String NOSEGMENTATION_yMdHm = "yyyyMMddHHmm";
        //年月日时分秒 无分割
        public static final String NOSEGMENTATION_yMdHms = "yyyyMMddHHmmss";
        //年月日时分秒毫秒 无分割
        public static final String NOSEGMENTATION_yMdHmsS = "yyyyMMddHHmmssSSS";
        //年月日 点分割
        public static final String POINT_yMd = "yyyy.MM.dd";
        //时分秒 点分割
        public static final String POINT_Hms = "HH.mm.ss";
        //年-月-日 时:分:秒
        public static final String FULLTIMEBY_yMdHms = "yyyy-MM-dd HH:mm:ss";
        //年-月-日 时:分:秒.毫秒
        public static final String FULLTIMEBY_yMdHmsS = "yyyy-MM-dd HH:mm:ss.SSS";
        //格式化 年-月-日
        public static final String FULLTIMEFORMAT_yMd = "yyyy年MM月dd日";
        //格式化 时:分:秒
        public static final String FULLTIMEFORMAT_Hms = "HH时mm分ss秒";
        //格式化 年-月-日 时:分:秒
        public static final String FULLTIMEFORMAT_yMdHms = "yyyy年MM月dd日 HH时mm分ss秒";
        //格式化 年-月-日 时:分:秒.毫秒
        public static final String FULLTIMEFORMAT_yMdHmsS = "yyyy年MM月dd日 HH时mm分ss秒SSS毫秒";
    }

    /**
     * 日期字符串转日期格式
     * @param date 日期字符串
     * @param timeFormat 转换后的日期格式
     * @return
     */
    public static Date dateStringToDate(String date, String timeFormat){
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String timeStamp2fulltime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMATE.FULLTIMEBY_yMdHms);
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
        long current=translateDatetoTimestamp(time, timeFormat);//当前时间毫秒数
        long zero=current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset();//前一天的零点零分零秒的毫秒数
        long startTime = zero + 24*60*60*1000;
        return startTime;
    }


    /**
     * 获取指定日期的晚上24点的时间戳
     *
     * @param time 时间
     * @param timeFormat 时间对应格式
     * @return 当天24点的时间戳(23:59:59)
     */
    public static long getTodayLaterMorning(String time, String timeFormat){
        long current=translateDatetoTimestamp(time, timeFormat);//当前时间毫秒数
        long zero=current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset();//前一天的零点零分零秒的毫秒数
        long endTime = zero + 24*60*60*1000*2 - 1;
        return endTime;
    }


    /**
     * 获取当前日期的过去指定天数长度的零点时时间戳，包含当天
     * @param maxDays 3 (2018/10/11 0:0:0)
     * @return 2018/10/09 0:0:0 的时间戳
     */
    public static long getLastDaysTimestamp(int maxDays){
        //当前时间毫秒数
        long current=System.currentTimeMillis();
        //今天零点零分零秒的毫秒数
        long zero=current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset();
        long lastThirty = zero - 24*60*60*1000*(maxDays);
        return lastThirty;
    }

    /**
     * 获取过去指定时间的时间戳
     * @param pastTime 指定的时间数量 5
     * @param type 时间的类型 d日 H时 m分 s秒 S毫秒 不写默认单位秒
     * @param timeFormat 转换后的时间戳格式化的标准 不写默认格式 年-月-日 时:分:秒
     * @return
     */
    public static String getThePastTime(int pastTime, String type, String timeFormat){
        if (type == null){
            type = "s";
        }
        if (timeFormat == null){
            timeFormat = DATEFORMATE.FULLTIMEBY_yMdHmsS;
        }
        long date = System.currentTimeMillis();
        Long now_time = new Long(1);
        if ("S".equals(type)){
            now_time = date - pastTime;
        }else if ("s".equals(type)){
            now_time = date - pastTime*1000;
        }else if ("m".equals(type)){
            now_time = date - pastTime*1000*60;
        }else if ("H".equals(type)){
            now_time = date - pastTime*1000*60*60;
        }else if ("d".equals(type)){
            now_time = date - pastTime*1000*60*60*24;
        }
        String finalTime = translateTimeToDate(now_time, timeFormat);
        return finalTime;
    }

    public static String getThePastTime(int pastTime){
        return getThePastTime(pastTime, null, null);
    }

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
     * @return 时间
     */
    public static String getNowTimeByFormat(String timeFormat){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
        String today = simpleDateFormat.format(date);
        return today;
    }

    /**
     * 转换时间戳位时间格式
     * @param timestamp 时间戳(long)
     * @param timeFormat 转换的时间格式
     * @return 时间
     */
    public static String translateTimeToDate(Long timestamp, String timeFormat){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
        String da = simpleDateFormat.format(timestamp);
        return da;
    }

    /**
     * 转换时间格式
     * @param date 时间(date)
     * @param timeFormat 转换的时间格式
     * @return 时间
     */
    public static String translateDateToString(Date date, String timeFormat){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
        String da = simpleDateFormat.format(date);
        return da;
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
     * @return
     */
    public static Date translateTimestampToDate(Long timestamp, String timeFormat){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
        String time = translateTimeToDate(timestamp, timeFormat);
        Date da = null;
        try {
            da = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return da;
    }

    /**
     * 时间转时间戳
     * @param time 需要转换的时间
     * @param timeFormat 时间对应的格式
     * @return 13位时间戳
     */
    public static Long translateDatetoTimestamp(String time, String timeFormat){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }


    /**
     * 序列化字符串为时间格式
     * @param time 需要转换的字符串 (20181011 10:11:12.013)
     * @param timeFormat 字符串对应的时间格式 (yyyyMMdd HH:mm:ss.SSS)
     * @param timeTranselate 转换后需要的字符串格式 (yyyy年M月dd日 HH时mm分ss秒SSS毫秒)
     * @return 2018年10月11日 10时11分12秒13毫秒
     */
    public static String serializationDate(String time, String timeFormat, String timeTranselate){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
        try {
            Date date = simpleDateFormat.parse(time);
            SimpleDateFormat dateFormat = new SimpleDateFormat(timeTranselate);
            String formatTime = dateFormat.format(date);
            return formatTime;
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
    public static List getDesignationDay(int num, String time, String timeFormat, String timeTranslate){
        List days = new ArrayList();
        Long timestamp_time = translateDatetoTimestamp(time, timeFormat);
        for (int i = 0; i < num; i++) {
            Long new_time = timestamp_time - i*24*60*60*1000;
            String today = translateTimeToDate(new_time, timeTranslate);
            days.add(today);
        }
        return days;

    }

    /**
     * 时间切割机，小时切割
     *
     * @param cutTime 切割小时数 2小时 = 2
     * @param startTime 开始时间戳 10位
     * @param endTime 结束时间戳 10位
     * @return 切割后时间段列表
     */
    public static List<String> splitTimestamp(int cutTime, long startTime, long endTime) {
        List<String> timeList = new ArrayList<>();
        startTime = startTime * 1000;
        endTime = endTime * 1000;
        if (cutTime <= 0){
            timeList.add(String.valueOf(String.valueOf(startTime).concat(",").concat(String.valueOf(endTime))));
        }else if (cutTime > 0 && endTime > startTime){
            long cut = ((endTime - startTime) / 3600000 /cutTime) + ((endTime - startTime) / 3600000 % cutTime);
            long timeInterval = (endTime - startTime) / cut;
            while(true){
                if (startTime < endTime){
                    if ((startTime + timeInterval) > endTime){
                        timeList.add(String.valueOf(String.valueOf(startTime).concat(",").concat(String.valueOf(endTime))));
                    }else {
                        timeList.add(String.valueOf( String.valueOf(startTime).concat(",").concat(String.valueOf(startTime + timeInterval))));
                    }
                }else if (startTime >= endTime){
                    break;
                }
                startTime = startTime + timeInterval;
            }
        }
        return timeList;
    }

    /**
     * 当前时间与周期时间的时间差
     * 最大支持3600秒
     * 时间单位 秒
     *
     * @param executorTime 执行周期
     * @return 等待秒
     */
    public static long waitingTimeBySeconds(int executorTime){
        if (executorTime <= 3600 && executorTime >=0){
            //获取当前时间与周期时间的差
            long nowTime = System.currentTimeMillis();
            int timeDiscrepancy = Integer.parseInt(String.valueOf(nowTime%(executorTime*1000)/1000));
            return executorTime-timeDiscrepancy;
        }else {
            return 0;
        }
    }


    /**
     * 等待开始周期执行的时间
     * 最大限制24h
     * 单位 小时
     *
     * @param executorTime 周期
     * @return 等待秒
     */
    public static long waitingTimeByHours(int executorTime){
        long delayTime = 0;
        if (executorTime <= 24 && executorTime>0){
            int num = 24 / executorTime;
            long tomorrowEarlyMorning = getTodayEarlyMorning(translateTimeToDate(getCurrentTime(), DATEFORMATE.FULLTIMEBY_yMdHmsS), DATEFORMATE.FULLTIMEBY_yMdHmsS);
            long todayEarlyMorning = tomorrowEarlyMorning - 24*60*60*1000;
            long todayHour = num * executorTime * 3600000 + todayEarlyMorning;
            if (todayHour < getCurrentTime() && getCurrentTime() < tomorrowEarlyMorning){
                delayTime = (tomorrowEarlyMorning - getCurrentTime()) / 1000 / 60;
            }else {
                for (int i = 0; i <= num; i++) {
                    long current = i * executorTime * 3600000 + todayEarlyMorning;
                    if (getCurrentTime() == current){
                        delayTime = 0;
                        break;
                    }else if (current > getCurrentTime()){
                        delayTime = (current - getCurrentTime()) / 1000 / 60;
                        break;
                    }
                }
            }
        }else {
            delayTime = 0;
        }
        if (delayTime <= 0){
            return 0;
        }else {
            delayTime = delayTime * 60 + waitingTimeBySeconds(60);
            return delayTime;
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
                time = date.substring(0, 4) + "-" +
                        date.substring(4, 6) + "-" +
                        date.substring(6, 8) + " " +
                        date.substring(8, 10) + ":" +
                        date.substring(10, 12) + ":" +
                        date.substring(12, 14);
            }else if (date.length() == 13){
                //1560390240760
                time = DateUtil.translateTimeToDate(Long.valueOf(date), format);
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
     */
    public static String switchTime(String time) throws ParseException {
        SimpleDateFormat format2 = new SimpleDateFormat(DATEFORMATE.FULLTIMEBY_yMdHms);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");//格式化的表达式
        time = time.replace("Z", " UTC");//是空格+UTC
        Date data = format.parse(time);
        String format1 = format2.format(data);
        return format1;
    }

    /**
     * 转化时间格式
     * @param time  2020-01-15T15:16:23+08:00
     * @return      2020-01-15 15:16:23
     * @throws ParseException
     */
    public static String switchLineTime(String time) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date  date = df.parse(time);
        SimpleDateFormat df1 = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
        Date date1 =  df1.parse(date.toString());
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = df2.format(date1);
        return format;
    }

    /**
     * 根据日期格式自动转换时间
     * 最大精确到秒，不支持毫秒，最小精确到年，不支持世纪
     * @param date_format 日期格式，例如 YYYY-MM-dd
     * @param parseInt 前/后 多少，例如 1 根据date_format格式表达为当天时间的前一天
     * @return 格式化后的时间
     */
    public static String formatDateByReg(String date_format, int parseInt){
        String translateTimeToDate = "";
        Long current_time = System.currentTimeMillis();
        if ((date_format.contains("YYYY")||date_format.contains("yyyy")) &&
                date_format.contains("MM") &&
                date_format.contains("dd") &&
                date_format.contains("HH") &&
                date_format.contains("mm") &&
                date_format.contains("ss")){
            //秒
            long real_time = current_time + parseInt * 1000;
            translateTimeToDate = DateUtil.translateTimeToDate(real_time, date_format);
        }else if ((date_format.contains("YYYY")||date_format.contains("yyyy")) &&
                date_format.contains("MM") &&
                date_format.contains("dd") &&
                date_format.contains("HH") &&
                date_format.contains("mm") &&
                !date_format.contains("ss")){
            //分钟
            long real_time = current_time + parseInt * 60 * 1000;
            translateTimeToDate = DateUtil.translateTimeToDate(real_time, date_format);
        }else if ((date_format.contains("YYYY")||date_format.contains("yyyy")) &&
                date_format.contains("MM") &&
                date_format.contains("dd") &&
                date_format.contains("HH") &&
                !date_format.contains("mm") &&
                !date_format.contains("ss")){
            //小时
            long real_time = current_time + parseInt * 60 * 60 * 1000;
            translateTimeToDate = DateUtil.translateTimeToDate(real_time, date_format);
        }else if ((date_format.contains("YYYY")||date_format.contains("yyyy")) &&
                date_format.contains("MM") &&
                date_format.contains("dd") &&
                !date_format.contains("HH") &&
                !date_format.contains("mm") &&
                !date_format.contains("ss")){
            //天
            long real_time = current_time + parseInt * 24 * 60 * 60 * 1000;
            translateTimeToDate = DateUtil.translateTimeToDate(real_time, date_format);
        }else if ((date_format.contains("YYYY")||date_format.contains("yyyy")) &&
                date_format.contains("MM") &&
                !date_format.contains("dd") &&
                !date_format.contains("HH") &&
                !date_format.contains("mm") &&
                !date_format.contains("ss")){
            //月
            String toDate = DateUtil.translateTimeToDate(current_time, DateUtil.DATEFORMATE.FULLTIMEBY_yM);
            Integer year = Integer.valueOf(toDate.split("-")[0]);
            Integer month = Integer.valueOf(toDate.split("-")[1]);
            int shang = parseInt / 12;
            int yu = parseInt % 12;
            year = year + shang;
            month = month + yu;
            if (month<0){
                year = year - 1;
                month = 12 + month;
            }else if (month>12){
                year = year + 1;
                month = month - 12;
            }
            String i5 = month<10?"0"+String.valueOf(month):String.valueOf(month);
            Long translateDateToTimestamp = DateUtil.translateDatetoTimestamp(String.valueOf(year) + "-" + i5, DateUtil.DATEFORMATE.FULLTIMEBY_yM);
            translateTimeToDate = DateUtil.translateTimeToDate(translateDateToTimestamp, date_format);
        }else if ((date_format.contains("YYYY")||date_format.contains("yyyy")) &&
                !date_format.contains("MM") &&
                !date_format.contains("dd") &&
                !date_format.contains("HH") &&
                !date_format.contains("mm") &&
                !date_format.contains("ss")){
            //年
            String year = DateUtil.translateTimeToDate(current_time, DATEFORMATE.BASETIME_yyyy);
            String value = String.valueOf(Integer.valueOf(year) + parseInt);
            Long aLong = DateUtil.translateDatetoTimestamp(value, DATEFORMATE.BASETIME_yyyy);
            translateTimeToDate = DateUtil.translateTimeToDate(aLong, date_format);
        }else {
            translateTimeToDate = DateUtil.translateTimeToDate(current_time, date_format);
        }
        return translateTimeToDate;
    }

    /**
     * 转换String类型的Date格式数据为指定格式
     * @param time Date 格式日期字符串
     * @param date_format 格式化后类型
     * @return 格式化后字符串
     */
    public static String changeDateStringToFormatString(String time, String date_format) throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        SimpleDateFormat format2 = new SimpleDateFormat(date_format);
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
        c.add(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        return DateUtil.translateDateToString(c.getTime(), format);
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
        c.add(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return DateUtil.translateDateToString(c.getTime(), format);
    }
}
