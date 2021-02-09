package site.cnkj.common.object.servlet;

/*
 * @author  LXW
 * @create  2021/2/8 15:04
 * @Description
 */
public enum  ResponseBodyCode {

    SUCCESS{
        @Override
        public String getCode() {
            return "000000";
        }
    },
    FAILED{
        @Override
        public String getCode() {
            return "100000";
        }
    },
    WARNING{
        @Override
        public String getCode() {
            return "200000";
        }
    },
    ERROR{
        @Override
        public String getCode() {
            return "300000";
        }
    };

    public abstract String getCode();

}
