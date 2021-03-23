package site.cnkj.common.object.servlet;

/*
 * @author  LXW
 * @create  2021/2/8 15:04
 * @Description
 */
public enum  ResponseBodyCode {

    SUCCESS("000000"), FAILED("100000"), WARNING("200000"), ERROR("300000");


    private String code;

    ResponseBodyCode(String code){
        this.code = code;
    }

    public String getCode(){
        return this.code;
    }

}
