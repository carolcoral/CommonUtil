package site.cnkj.common.utils.http;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


public class RestTemplateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateUtil.class);

    private RestTemplate restTemplate;

    @Value("${restTemplate.remote.alarm.url.email:''}")
    private String alarmEmailUrl = "";

    @Value("${restTemplate.remote.alarm.url.sms:''}")
    private String alarmSMSUrl = "";

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    //post json对象
    public <T> T postVieObjectReturn(Object object, String url,Class<T> cls){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<Object> requestEntity  = new HttpEntity<Object>(object, headers);
        try {
            ResponseEntity<T> responseEntity = restTemplate.postForEntity(url, requestEntity, cls);
            return responseEntity.getBody();
        }catch (Exception e) {
            LOGGER.error("postVieObjectReturn", e.toString());
        }
        return null;
    }

    //post form对象
    public <T> T postVieFormReturn(MultiValueMap<String, Object> req_payload,String url,Class<T> cls){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, Object>> requestEntity  = new HttpEntity<MultiValueMap<String, Object>>(req_payload, headers);
        try {
            ResponseEntity<T> responseEntity = restTemplate.postForEntity(url, requestEntity, cls);
            return responseEntity.getBody();
        }catch (Exception e) {
            LOGGER.error("postVieFormReturn请求失败", e.toString());
        }
        return null;
    }

    //get 请求带参数
    public <T> T get(String url,Class<T> cls,Map req_payload){
        try {
            ResponseEntity<T> responseEntity = restTemplate.getForEntity(url,cls,req_payload);
            return responseEntity.getBody();
        }catch (Exception e) {
            LOGGER.error("get请求失败", e.toString());
        }
        return null;
    }

    //get 无参数
    public <T> T get(String url,Class<T> cls){
        try {
            ResponseEntity<T> responseEntity = restTemplate.getForEntity(url,cls);
            return responseEntity.getBody();
        }catch (Exception e) {
            LOGGER.error("get请求失败", e.toString());
        }
        return null;
    }

    /**
     * get 请求，拼接地址
     * @param url 地址
     * @param args 参数
     * @param cls 对象
     * @param <T> 泛型
     * @return
     */
    public <T> T getByParams(String url,Class<T> cls, Map<String,Object> args){
        try {
            String params = new String();
            for (String arg : args.keySet()){
                params = params + arg + "=" + args.get(arg) + "&";
            }
            params = params.substring(0, params.length() -1);
            url = url + "?" + params;
            ResponseEntity<T> responseEntity = restTemplate.getForEntity(url,cls);
            return responseEntity.getBody();
        }catch (Exception e) {
            LOGGER.error("get请求失败", e.toString());
        }
        return null;
    }

    /**
     * 发送邮件
     * @param url               请求地址
     * @param subject           邮件主题
     * @param content           邮件内容
     * @param address           收件人地址
     * @param system            发送方系统名
     */
    public void alarmByEmail(String url, String subject, String content, String address, String system) {
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("to", address);
        multiValueMap.add("subject", subject);
        multiValueMap.add("content", content);
        multiValueMap.add("type", "text");
        multiValueMap.add("system", system);
        postVieFormReturn(multiValueMap, StringUtils.isNotEmpty(url)?url:alarmEmailUrl, String.class);
    }

    public void alarmByEmail(String subject, String content, String address, String system) {
        alarmByEmail(null, subject, content, address, system);
    }


    /**
     * 发送短信
     * @param to 联系人，多人以逗号拼接
     * @param title 主题
     * @param content 内容
     * @param system 系统服务名
     */
    public void alarmBySMS(String url, String to, String title, String content, String system) {
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("to", to);
        multiValueMap.add("title", title);
        multiValueMap.add("content", content);
        multiValueMap.add("system", system);
        postVieFormReturn(multiValueMap, StringUtils.isNotEmpty(url)?url:alarmSMSUrl, String.class);
    }

    public void alarmBySMS(String to, String title, String content, String system) {
        alarmBySMS(null, to, title, content, system);
    }

}
