package site.cnkj.common.object.servlet;

/*
 * @version 1.0 created by LXW on 2019/3/12 14:17
 */
@Deprecated
public class CommonInfo {

    /**
     * @Desc ResponseCode for servlet request callback
     * @Version delete from 0.3.5 version and replace by <url>cn.migu.log.common.object.servlet.ResponseBodyCode</url>
     */
    @Deprecated
    public static class ResponseCode{
        public static final String SUCCESS = "000000";//成功
        public static final String FAILED = "100000";//结果异常
        public static final String WARNING = "200000";//警告
        public static final String ERROR = "300000";//程序故障
        public static final String TIMEOUT = "999999"; //登录过期

        public class Desc{
            public static final String SUCCESS = "SUCCESS";//成功
            public static final String FAILED = "FAILED";//结果异常
            public static final String WARNING = "WARNING";//警告
            public static final String ERROR = "ERROR";//程序故障
            public static final String NULL_URL = "请求URL不存在";
            public static final String EXIST_ERROR = "该条目已存在";
            public static final String NOT_EXIST_ERROR = "该条目不存在";
            public static final String NOT_NUMERIC = "分隔符数量不是正整数";
            public static final String OUT_RANGE_ERROR = "分隔符数量过长";
            public static final String NOT_EQUAL_ERROR = "分隔符数量不一致";
            public static final String NULL_PARAM = "必填字段不存在";
            public static final String PARAM_ERROR = "字段值错误";
            public static final String ITEM_REFERENCE = "该条目已被引用";
            public static final String NOT_REFRESH_ITEM = "存在未刷新的条目";
            public static final String OFFLINE_MACHINE = "机器已下线";
            public static final String NOT_OFFLINE_ITEM = "该条目未下线";
            public static final String RUNNING_REFRESH = "上次刷新未完成";
            public static final String REFRESH_TIMEOUT = "刷新超时";
            public static final String SCRIPT_ERROR = "请求脚本执行失败";
            public static final String ERROR_CRON = "cron表达式错误";
        }
    }

}
