package site.cnkj.common.object;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.cnkj.common.enumeration.ResponseStatusEnum;

/*
 * @version 1.0 created by LXW on 2019/7/22 11:04
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ResponseDataResult<T> extends BaseResult {

    private T data;

    public ResponseDataResult(ResponseStatusEnum responseStatusEnum) {
        this.setCode(responseStatusEnum.getCode());
        this.setDesc(responseStatusEnum.getDesc());
    }

    public ResponseDataResult(T data) {
        this.data = data;
    }

    public ResponseDataResult() {
    }
}
