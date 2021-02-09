package site.cnkj.common.object.servlet;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/*
 * @version 1.0 created by LXW on 2019/7/22 11:04
 */
@Data
public class DataResult extends BaseResult {

    private Object data;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
