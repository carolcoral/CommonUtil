package site.cnkj.common.object;

import java.util.HashMap;

/*
 * @author  LXW
 * @create  2020/4/21 11:16
 * @Description 异常原因枚举值
 */
public class ReasonEnumeration {

    public static HashMap<String, String> reasonEnumeration = new HashMap<>();

    public static final class CODE{

        public static final class Header{
            public static final String IMEI = "100001";
            public static final String IMSI = "100002";
            public static final String IDFA = "100003";
            public static final String OAID = "100004";
            public static final String HWID = "100005";
            public static final String UID = "100006";
        }

        /**
         * params
         */
        public static final class Params{
            public static final String contentId = "200001";
            public static final String marketingId = "200002";
            public static final String songListId = "200003";
            public static final String songId = "200004";
            public static final String songName = "200005";
            public static final String contentIdRange = "200006";
            public static final String rateLevelRange = "200007";
            public static final String isOnlineRange = "200008";
            public static final String beginTime = "200009";
            public static final String logId = "200010";
            public static final String videoIdMvIdConcertId = "200011";
            public static final String rateLevel = "200012";
            public static final String vmsParamsE = "200013";
            public static final String vmsParamsERange = "200014";
            public static final String codeRange = "200015";
            public static final String requestTimeRange = "200016";
            public static final String requestTime = "200017";
            public static final String resourceTypeRange = "200018";
        }

        /**
         * RequestParams
         */
        public static final class RequestParams{}

        /**
         * ResponseBody
         */
        public static final class ResponseBody{}
    }

    static {
        reasonEnumeration.put(CODE.Header.IMEI, "IMEI的值不符合规范，数据丢弃");
        reasonEnumeration.put(CODE.Header.IMSI, "IMSI的值不符合规范，数据丢弃");
        reasonEnumeration.put(CODE.Header.IDFA, "IDFA的值长度不符合规范，数据丢弃");
        reasonEnumeration.put(CODE.Header.OAID, "OAID的值不符合规范，数据丢弃");
        reasonEnumeration.put(CODE.Header.HWID, "HWID的值不符合规范，数据丢弃");
        reasonEnumeration.put(CODE.Header.UID, "UID的值等于15586008224920247234882,UID被强制转换成空值");
        reasonEnumeration.put(CODE.Params.contentId, "params.contentId字段不存在或值为空，数据丢弃");
        reasonEnumeration.put(CODE.Params.marketingId, "params.marketingId字段不存在，数据丢弃");
        reasonEnumeration.put(CODE.Params.songListId, "params.songListId字段不存在，设置默认值为空值");
        reasonEnumeration.put(CODE.Params.songId, "params.songId字段不存在，设置默认值为空值");
        reasonEnumeration.put(CODE.Params.songName, "params.songName字段不存在，数据丢弃");
        reasonEnumeration.put(CODE.Params.contentIdRange, "params.contentId字段值不在范围内，数据丢弃");
        reasonEnumeration.put(CODE.Params.rateLevel, "params.rateLevel字段不存在或值为空");
        reasonEnumeration.put(CODE.Params.rateLevelRange, "params.rateLevel字段不在范围内");
        reasonEnumeration.put(CODE.Params.isOnlineRange, "params.isOnline字段不存在或不在范围内，params.isOnline设置默认值");
        reasonEnumeration.put(CODE.Params.beginTime, "params.beginTime字段不存在或值为空，数据丢弃");
        reasonEnumeration.put(CODE.Params.logId, "params.logId字段不存在或值为空，数据丢弃");
        reasonEnumeration.put(CODE.Params.videoIdMvIdConcertId, "params中不存在videoId、mvId、concertId或对应的值为空，数据丢弃");
        reasonEnumeration.put(CODE.Params.vmsParamsE, "params.E字段参位值数量不正确");
        reasonEnumeration.put(CODE.Params.vmsParamsERange, "params.E字段参位值不在范围内");
        reasonEnumeration.put(CODE.Params.codeRange, "params.code字段不存在或值为空");
        reasonEnumeration.put(CODE.Params.requestTimeRange, "params.requestTime字段不符合时间戳格式");
        reasonEnumeration.put(CODE.Params.requestTime, "params.requestTime字段不存在或值为空");
        reasonEnumeration.put(CODE.Params.resourceTypeRange, "params.resourceType字段值不在范围内");
    }

}
