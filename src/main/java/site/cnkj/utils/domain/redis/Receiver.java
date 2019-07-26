package site.cnkj.utils.domain.redis;

/*
 * @version 1.0 created by LXW on 2019/1/9 16:17
 */
public interface Receiver {

    Object receiver(String message);

}
