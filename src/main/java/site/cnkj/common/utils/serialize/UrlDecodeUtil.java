package site.cnkj.common.utils.serialize;

import com.alibaba.fastjson.JSONObject;

/*
 * @version 1.0 created by LXW on 2019/11/8 15:11
 */
public class UrlDecodeUtil {

    /**
     * 解析请求地址中的字段内容并转成json对象
     * @param url 地址
     * @return 对象
     */
    public static JSONObject getParams(String url){
        JSONObject jsonObject = new JSONObject();
        try {
            String substring = url.substring(url.indexOf("?")+1, url.length());
            String[] split = substring.split("&");
            for (String s : split) {
                String[] strings = s.split("=");
                if (strings.length == 2){
                    jsonObject.put(strings[0], strings[1]);
                }
            }
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

}
