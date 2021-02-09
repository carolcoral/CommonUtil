package site.cnkj.common.service;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;
import site.cnkj.common.utils.http.HttpCommonUtil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


@Data
@Component
public class ActuatorInfo implements InfoContributor {


    @Value("${spring.application.name:common}")
    private String NAME="";//当前服务名

    private int PID = HttpCommonUtil.getCurrentPid();//服务当前状态

    private List TOPICS=new LinkedList();//当前监听的topic列表

    private HashMap INFO = new HashMap();//自定义对象集合

    public void removeCustom(String key){
        this.INFO.remove(key);
    }

    public void addCustom(HashMap customInfo){
        this.INFO.putAll(customInfo);
    }


    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("SERVICE_NAME",NAME);
        builder.withDetail("SERVICE_PID", PID);
        if (TOPICS.size() > 0){
            builder.withDetail("TOPIC_LIST", TOPICS);
        }
        if (INFO.size() > 0){
            builder.withDetail("CUSTOM_INFO", INFO);
        }
    }

    public void clean(){
        this.TOPICS.clear();
        this.INFO.clear();
    }

    public Map<String, Object> toJson(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("SERVICE_NAME", getNAME());
        jsonObject.put("SERVICE_PID", getPID());
        jsonObject.put("TOPIC_LIST", getTOPICS());
        jsonObject.put("CUSTOM_INFO", getINFO());
        return jsonObject;
    }

    public Object get(String name){
        return toJson().get(name);
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
