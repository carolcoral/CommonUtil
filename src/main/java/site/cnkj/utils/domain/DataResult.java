package site.cnkj.utils.domain;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/*
 * @version 1.0 created by LXW on 2019/6/10 11:11
 */
@Data
public class DataResult extends BaseResult {

    private Object data;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
