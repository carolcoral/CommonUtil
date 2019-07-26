package site.cnkj.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


public class RestTemplateUtil {

    public static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateUtil.class);

    private RestTemplate restTemplate;

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * post请求，json格式对象
     * @param object 提交json对象
     * @param url 请求地址
     * @param cls 设置返回对象
     * @param <T> 返回对象
     * @return
     */
    public <T> T postVieObjectReturn(Object object, String url,Class<T> cls){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<Object> requestEntity  = new HttpEntity<Object>(object, headers);
        try {
            ResponseEntity<T> responseEntity = restTemplate.postForEntity(url, requestEntity, cls);
            return responseEntity.getBody();
        }catch (Exception e) {
            LOGGER.error("postVieObjectReturn",e);
        }
        return null;
    }

    /**
     * post请求，form表单提交
     * @param req_payload 表单对象
     * @param url 请求地址
     * @param cls 设置返回对象
     * @param <T> 返回对象
     * @return
     */
    public <T> T postVieFormReturn(MultiValueMap<String, Object> req_payload,String url,Class<T> cls){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, Object>> requestEntity  = new HttpEntity<MultiValueMap<String, Object>>(req_payload, headers);
        try {
            ResponseEntity<T> responseEntity = restTemplate.postForEntity(url, requestEntity, cls);
            return responseEntity.getBody();
        }catch (Exception e) {
            LOGGER.error("postVieFormReturn请求失败",e);
        }
        return null;
    }

    /**
     * get 请求，带参数
     * @param url 请求地址
     * @param cls 设置返回对象
     * @param req_payload 参数对象
     * @param <T> 返回对象
     * @return
     */
    public <T> T get(String url,Class<T> cls,Map req_payload){
        try {
            ResponseEntity<T> responseEntity = restTemplate.getForEntity(url,cls,req_payload);
            return responseEntity.getBody();
        }catch (Exception e) {
            LOGGER.error("get请求失败",e);
        }
        return null;
    }

    /**
     * get请求，不带参数
     * @param url 请求地址
     * @param cls 设置返回对象
     * @param <T> 返回对象
     * @return
     */
    public <T> T get(String url,Class<T> cls){
        try {
            ResponseEntity<T> responseEntity = restTemplate.getForEntity(url,cls);
            return responseEntity.getBody();
        }catch (Exception e) {
            LOGGER.error("get请求失败",e);
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
    public <T> T getWithQ(String url,Class<T> cls, Map<String,Object> args){
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
            LOGGER.error("get请求失败",e);
        }
        return null;
    }

}
