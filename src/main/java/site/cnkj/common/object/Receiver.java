package site.cnkj.common.object;

/*
 * @version 1.0 created by LXW on 2019/1/9 16:17
 */
public interface Receiver {

    abstract Object receiver(String message);
    abstract Object receiver2(String message);

}
