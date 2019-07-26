package site.cnkj.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 * @version 1.0 created by LXW on 2018/11/8 16:11
 */
public class DateUtil {

    public static class DATEFORMATE {
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
     *
     * @param date       日期字符串
     * @param timeFormat 转换后的日期格式
     * @return
     */
    public static Date dateStringToDate(String date, String timeFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String timeStamp2fulltime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMATE.FULLTIMEBY_yMdHms);
        return sdf.format(date);
    }

    /**
     * 获取指定日期的零点时间戳
     *
     * @param time       时间
     * @param timeFormat 时间对应格式
     * @return 零点时间戳(0 : 0 : 0)
     */
    public static long getTodayEarlyMorning(String time, String timeFormat) {
        long current = translateDatetoTimestamp(time, timeFormat);//当前时间毫秒数
        long zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();//前一天的零点零分零秒的毫秒数
        long startTime = zero + 24 * 60 * 60 * 1000;
        return startTime;
    }


    /**
     * 获取指定日期的晚上24点的时间戳
     *
     * @param time       时间
     * @param timeFormat 时间对应格式
     * @return 当天24点的时间戳(23 : 59 : 59)
     */
    public static long getTodayLaterMorning(String time, String timeFormat) {
        long current = translateDatetoTimestamp(time, timeFormat);//当前时间毫秒数
        long zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();//前一天的零点零分零秒的毫秒数
        long endTime = zero + 24 * 60 * 60 * 1000 * 2 - 1;
        return endTime;
    }


    /**
     * 获取当前日期的过去指定天数长度的零点时时间戳，包含当天
     *
     * @param maxDays 3 (2018/10/11 0:0:0)
     * @return 2018/10/09 0:0:0 的时间戳
     */
    public static long getLastDaysTimestamp(int maxDays) {
        //当前时间毫秒数
        long current = System.currentTimeMillis();
        //今天零点零分零秒的毫秒数
        long zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();
        long lastThirty = zero - 24 * 60 * 60 * 1000 * (maxDays);
        return lastThirty;
    }

    /**
     * 获取过去指定时间的时间戳
     *
     * @param pastTime   指定的时间数量 5
     * @param type       时间的类型 d日 H时 m分 s秒 S毫秒 不写默认单位秒
     * @param timeFormat 转换后的时间戳格式化的标准 不写默认格式 年-月-日 时:分:秒
     * @return
     */
    public static String getThePastTime(int pastTime, String type, String timeFormat) {
        if (type == null) {
            type = "s";
        }
        if (timeFormat == null) {
            timeFormat = DATEFORMATE.FULLTIMEBY_yMdHmsS;
        }
        long date = System.currentTimeMillis();
        Long now_time = new Long(1);
        if ("S".equals(type)) {
            now_time = date - pastTime;
        } else if ("s".equals(type)) {
            now_time = date - pastTime * 1000;
        } else if ("m".equals(type)) {
            now_time = date - pastTime * 1000 * 60;
        } else if ("H".equals(type)) {
            now_time = date - pastTime * 1000 * 60 * 60;
        } else if ("d".equals(type)) {
            now_time = date - pastTime * 1000 * 60 * 60 * 24;
        }
        String finalTime = translateTimeToDate(now_time, timeFormat);
        return finalTime;
    }

    public static String getThePastTime(int pastTime) {
        return getThePastTime(pastTime, null, null);
    }

    public static String getThePastTime(int pastTime, String type) {
        return getThePastTime(pastTime, type, null);
    }

    /**
     * 获取当前系统时间的时间戳
     *
     * @return 时间戳
     */
    public static Long getCurrentTime() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间规整以5分钟为单位的时间
     *
     * @param currentTime 时间搓
     * @param min         多少分钟的倍数（如当前时间：16:38 以为5为倍数，规整后的时间为16：35）
     * @return 时间 （201906211635）
     */
    public static String getCurrentTimePutInOrder(long currentTime, long min) {
        try {
            long needTime = 0;
            //北京时间和零时区差八个小时所以代码中如果大于八个小时取余就有问题
            if (min >= 8 * 60) {
                return new SimpleDateFormat(DATEFORMATE.NOSEGMENTATION_yMdHm).format(getMinDate(currentTime));
            } else {
                needTime = currentTime - currentTime % (min * 60L * 1000L);
            }
            return new SimpleDateFormat(DATEFORMATE.NOSEGMENTATION_yMdHm).format(new Date(needTime));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当天的日期
     *
     * @param time 时间搓
     * @return
     * @throws ParseException
     */
    public static Date getMinDate(Long time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMATE.FULLTIMEBY_yMd);
        Date newDate = sdf.parse(sdf.format(time));
        return newDate;
    }

    /**
     * 获取指定格式的当前时间
     *
     * @return 时间
     */
    public static String getNowTimeByFormat(String timeFormat) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
        String today = simpleDateFormat.format(date);
        return today;
    }

    /**
     * 转换时间戳位时间格式
     *
     * @param timestamp  时间戳(long)
     * @param timeFormat 转换的时间格式
     * @return 时间
     */
    public static String translateTimeToDate(Long timestamp, String timeFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
        String da = simpleDateFormat.format(timestamp);
        return da;
    }

    /**
     * 字符串转成时间格式
     *
     * @param time       字符串
     * @param timeFormat 转换格式
     * @return 时间格式
     */
    public static Date translateStringToDate(String time, String timeFormat) {
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
     *
     * @param timestamp  时间戳
     * @param timeFormat 时间格式
     * @return
     */
    public static Date translateTimestampToDate(Long timestamp, String timeFormat) {
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
     *
     * @param time       需要转换的时间
     * @param timeFormat 时间对应的格式
     * @return 13位时间戳
     */
    public static Long translateDatetoTimestamp(String time, String timeFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return null;
        }
        long res = date.getTime();
        return res;
    }


    /**
     * 序列化字符串为时间格式
     *
     * @param time           需要转换的字符串 (20181011 10:11:12.013)
     * @param timeFormat     字符串对应的时间格式 (yyyyMMdd HH:mm:ss.SSS)
     * @param timeTranselate 转换后需要的字符串格式 (yyyy年M月dd日 HH时mm分ss秒SSS毫秒)
     * @return 2018年10月11日 10时11分12秒13毫秒
     */
    public static String serializationDate(String time, String timeFormat, String timeTranselate) {
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
     *
     * @param num           天数
     * @param time          时间
     * @param timeFormat    时间格式
     * @param timeTranslate 输出格式
     * @return 日期集合
     */
    public static List<String> getDesignationDay(int num, String time, String timeFormat, String timeTranslate) {
        List<String> days = new ArrayList<String>();
        Long timestamp_time = translateDatetoTimestamp(time, timeFormat);
        for (int i = 0; i < num; i++) {
            Long new_time = timestamp_time - i * 24 * 60 * 60 * 1000;
            String today = translateTimeToDate(new_time, timeTranslate);
            days.add(today);
        }
        return days;

    }

    /**
     * 时间切割机，小时切割
     *
     * @param cutTime   切割小时数 2小时 = 2
     * @param startTime 开始时间戳 10位
     * @param endTime   结束时间戳 10位
     * @return Map<序号, 时间段> 切割后时间段列表
     */
    public static Map<Integer, String> splitTimestamp(int cutTime, long startTime, long endTime) {
        List<String> timeList = new ArrayList<>();
        Map timeMap = new LinkedHashMap();
        startTime = startTime * 1000;
        endTime = endTime * 1000;
        if (cutTime <= 0) {
            timeList.add(String.valueOf(String.valueOf(startTime).concat(",").concat(String.valueOf(endTime))));
        } else if (cutTime > 0 && endTime > startTime) {
            long cut = ((endTime - startTime) / 3600000 / cutTime) + ((endTime - startTime) / 3600000 % cutTime);
            long timeInterval = (endTime - startTime) / cut;
            int i = 0;
            while (true) {
                Object put;
                if (startTime < endTime) {
                    if ((startTime + timeInterval) > endTime) {
                        timeList.add(String.valueOf(String.valueOf(startTime).concat(",").concat(String.valueOf(endTime))));
                        put = timeMap.put(i, String.valueOf(String.valueOf(startTime).concat(",").concat(String.valueOf(endTime))));
                    } else {
                        timeList.add(String.valueOf(String.valueOf(startTime).concat(",").concat(String.valueOf(startTime + timeInterval))));
                        put = timeMap.put(i, String.valueOf(String.valueOf(startTime).concat(",").concat(String.valueOf(startTime + timeInterval))));
                    }
                    i++;
                } else if (startTime >= endTime) {
                    break;
                }
                startTime = startTime + timeInterval;
            }
        }
        return timeMap;
    }

    /**
     * 当前时间与周期时间的时间差
     * 时间单位 秒
     *
     * @param executorTime 执行周期
     * @return 周期执行的等待时间
     */
    public static int executorsDelayTime(int executorTime) {
        //获取当前时间与周期时间的差
        long nowTime = System.currentTimeMillis();
        int timeDiscrepancy = Integer.parseInt(String.valueOf(nowTime % (executorTime * 1000) / 1000));
        int delayTime = executorTime - timeDiscrepancy;
        return delayTime;
    }


}
