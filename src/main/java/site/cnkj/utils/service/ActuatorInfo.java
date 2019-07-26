package site.cnkj.utils.service;

import lombok.Data;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


@Data
@Component
public class ActuatorInfo implements InfoContributor {

    //当前服务名
    private String SERVICE_NAME="";
    //服务当前进程号
    private int SERVICE_PID;
    //自定义属性
    private Object data = new Object();


    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("SERVICE_NAME",SERVICE_NAME);
        builder.withDetail("SERVICE_PID", SERVICE_PID);
        builder.withDetail("data", data);
    }

    public void clearAll(){
        this.SERVICE_NAME = new String();
        this.SERVICE_PID = -1;
        this.data = new Object();
    }

    public Map getAll(){
        Map map = new HashMap();
        map.put("SERVICE_NAME", getSERVICE_NAME());
        map.put("SERVICE_PID", getSERVICE_PID());
        map.put("data", getData());
        return map;
    }


}
