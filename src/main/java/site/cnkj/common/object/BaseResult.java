package site.cnkj.common.object;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by rx on 2018/9/17.
 */
@Data
public class BaseResult implements Serializable {

    private int code;
    private String desc;

}
