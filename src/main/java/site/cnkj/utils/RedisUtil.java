package site.cnkj.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.util.*;
import java.util.concurrent.TimeUnit;

/*
 * @version 1.0 created by LXW on 2019/6/26 11:18
 */
public class RedisUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class);

    private RedisTemplate<String, Object> redisTemplate;

    private String redisName;

    public RedisUtil(RedisTemplate<String, Object> redisTemplate, String redisName) {
        this.redisTemplate = redisTemplate;
        if (StringUtils.isNotEmpty(redisName)){
            this.redisName = redisName;
        }else {
            this.redisName = null;
        }
    }

    /**
     * 获取链接的redis的信息，等于Command <info>
     * @return
     */
    public Properties info(){
        try {
            return redisTemplate.getConnectionFactory().getConnection().info();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取链接的redis的信息，等于Command <info>
     * @param section 指定的属性
     * @return
     */
    public Properties info(String section){
        try {
            return redisTemplate.getConnectionFactory().getConnection().info(section);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key,long time){
        try {
            if(time >= 0){
                if (StringUtils.isNotEmpty(redisName)){
                    key = redisName + ":" + key;
                }
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据key 获取过期时间
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public Long getExpire(String key){
        try {
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            return redisTemplate.getExpire(key,TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断key是否存在
     * @param key 键
     * @return true 存在 false不存在
     */
    public Boolean hasKey(String key){
        try {
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    public Boolean delete(String key){
        try {
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            return redisTemplate.delete(key);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    public Object get(String key){
        try {
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            return redisTemplate.opsForValue().get(key);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    public Object getWithNoName(String key){
        try {
            return redisTemplate.opsForValue().get(key);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key,Object value) {
        try {
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key,Object value,long time){
        try {
            if(time >= 0){
                if (StringUtils.isNotEmpty(redisName)){
                    key = redisName + ":" + key;
                }
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 递增
     * @param key 键
     * @param delta 要增加几(大于等于0)
     * @return
     */
    public Long increment(String key, long delta){
        try {
            if(delta >= 0){
                if (StringUtils.isNotEmpty(redisName)){
                    key = redisName + ":" + key;
                }
                return redisTemplate.opsForValue().increment(key, delta);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Long increment(String key, long delta, long timeOut) {
        try {
            if(delta >= 0){
                if (StringUtils.isNotEmpty(redisName)){
                    key = redisName + ":" + key;
                }
                Long increment = redisTemplate.opsForValue().increment(key, delta);
                redisTemplate.expire(key, timeOut, TimeUnit.SECONDS);
                return increment;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 递减
     * @param key 键
     * @param delta 要减少几(小于等于0)
     * @return
     */
    public Long decrement(String key, long delta){
        try {
            if(delta >= 0){
                if (StringUtils.isNotEmpty(redisName)){
                    key = redisName + ":" + key;
                }
                return redisTemplate.opsForValue().increment(key, -delta);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取hashKey对应的所有键值
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object,Object> hashMapGet(String key){
        try {
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            return redisTemplate.opsForHash().entries(key);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * HashSet
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hashMapSet(String key, Map<String, String> map){
        try {
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * HashSet 并设置时间
     * @param key 键
     * @param map 对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hashMapSet(String key, Map<String,Object> map, long time){
        try {
            if (time >= 0){
                if (StringUtils.isNotEmpty(redisName)){
                    key = redisName + ":" + key;
                }
                redisTemplate.opsForHash().putAll(key, map);
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * HashGet
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hashGet(String key,String item){
        try {
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            return redisTemplate.opsForHash().get(key, item);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hashSet(String key,String item,Object value) {
        try {
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @param time 时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hashSet(String key, String item, Object value, long time) {
        try {
            if (time >= 0){
                if (StringUtils.isNotEmpty(redisName)){
                    key = redisName + ":" + key;
                }
                redisTemplate.opsForHash().put(key, item, value);
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除hash表中的值
     * @param key 键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public Long hashDelete(String key, Object... item){
        try {
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            return redisTemplate.opsForHash().delete(key,item);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断hash表中是否有该项的值
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public Boolean hashHasKey(String key, String item){
        try {
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            return redisTemplate.opsForHash().hasKey(key, item);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     * @param key 键
     * @param item 项
     * @param by 要增加几(大于0)
     * @return
     */
    public Double hashIncrement(String key, String item,double by){
        try {
            if (by >= 0 ){
                if (StringUtils.isNotEmpty(redisName)){
                    key = redisName + ":" + key;
                }
                return redisTemplate.opsForHash().increment(key, item, by);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * hash递减
     * @param key 键
     * @param item 项
     * @param by 要减少记(小于0)
     * @return
     */
    public Double hashDecrement(String key, String item, double by){
        try {
            if (by >= 0 ){
                if (StringUtils.isNotEmpty(redisName)){
                    key = redisName + ":" + key;
                }
                return redisTemplate.opsForHash().increment(key, item, -by);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据key获取Set中的所有值
     * @param key 键
     * @return
     */
    public Set<Object> hashGet(String key){
        try {
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前redis上的指定hashKey的全部字段
     * @param key
     * @return
     */
    public Set hashKeys(String key){
        try {
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            return redisTemplate.opsForHash().keys(key);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据value从一个set中查询,是否存在
     * @param key 键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean setHasKey(String key,Object value){
        try {
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将数据放入set缓存
     * @param key 键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public Long setSet(String key, Object...values) {
        try {
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将set数据放入缓存
     * @param key 键
     * @param time 时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public Long setSet(String key, long time, Object...values) {
        try {
            if (time >= 0){
                if (StringUtils.isNotEmpty(redisName)){
                    key = redisName + ":" + key;
                }
                Long count = redisTemplate.opsForSet().add(key, values);
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
                return count;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取set缓存的长度
     * @param key 键
     * @return
     */
    public Long setGetSize(String key){
        try {
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 移除值为value的
     * @param key 键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public Long setRemove(String key, Object ...values) {
        try {
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取list缓存的内容
     * @param key 键
     * @param start 开始
     * @param end 结束  0 到 -1代表所有值
     * @return
     */
    public List<Object> listGetRange(String key,long start, long end){
        try {
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取list缓存的长度
     * @param key 键
     * @return
     */
    public long listGetSize(String key){
        try {
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 通过索引 获取list中的值
     * @param key 键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object listGetByIndex(String key, long index){
        try {
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从右开始放入元素到list中
     * @param key 键
     * @param value 值
     * @return
     */
    public Long listRightPush(String key, Object value) {
        try {
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            return redisTemplate.opsForList().rightPush(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    public Long listRightPush(String key, Object value, long time) {
        try {
            if (time >= 0){
                if (StringUtils.isNotEmpty(redisName)){
                    key = redisName + ":" + key;
                }
                Long rightPush = redisTemplate.opsForList().rightPush(key, value);
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
                return rightPush;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @return
     */
    public Long listRightPushAll(String key, List<Object> value) {
        try {
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            return redisTemplate.opsForList().rightPushAll(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    public Long listRightPushAll(String key, List<Object> value, long time) {
        try {
            if (time >= 0){
                if (StringUtils.isNotEmpty(redisName)){
                    key = redisName + ":" + key;
                }
                Long rightPushAll = redisTemplate.opsForList().rightPushAll(key, value);
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
                return rightPushAll;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据索引修改list中的某条数据
     * @param key 键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean listSetByIndex(String key, long index, Object value) {
        try {
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除列表中指定的value
     * @param key
     * @param count 正数表示从左开始查询，删除查询到的第一个；负数表示从右开始查询，删除查询到的第一个；0表示删除符合条件的全部内容
     * @param value 指定的值
     * @return
     */
    public Long listRemove(String key, long count, Object value) {
        try {
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            return redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询某个前缀的全部key
     *
     * @param key
     * @return
     */
    public Set<String> keys(String key){
        try {
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            return redisTemplate.keys(key.concat("*"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前redis上的全部key
     * @return
     */
    public Set<String> keys(){
        try {
            String key = "";
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName;
            }
            return redisTemplate.keys(key.concat("*"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 消息发布
     * @param channel 通道名
     * @param message 信息
     */
    public void publish(String channel, String message){
        try {
            redisTemplate.convertAndSend(channel, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从左开始加入全部集合数据
     * @param key
     * @param value 集合
     * @return
     */
    public Long listLeftPushAll(String key, Collection value){
        try {
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            return redisTemplate.opsForList().leftPushAll(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从左开始加入一个新元素
     * @param key
     * @param value 元素
     * @return
     */
    public Long listLeftPush(String key, String value){
        try {
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            return redisTemplate.opsForList().leftPush(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从左取出第一个元素，直到超过设置的时间后仍未取出则自动返回
     * @param key
     * @param time
     * @param timeUnit
     * @return
     */
    public Object listLeftPop(String key, long time, TimeUnit timeUnit){
        try {
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            if (redisTemplate.hasKey(key)){
                return redisTemplate.opsForList().leftPop(key, time, timeUnit);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从左取出第一个元素
     * @param key
     * @return
     */
    public Object listLeftPop(String key){
        try {
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            if (redisTemplate.hasKey(key)){
                return redisTemplate.opsForList().leftPop(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从右取出第一个元素
     * @param key
     * @return
     */
    public Object listRightPop(String key){
        try {
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            if (redisTemplate.hasKey(key)){
                return redisTemplate.opsForList().rightPop(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 从右取出第一个元素并把该元素从左加入到另一个队列中
     * @param sourceKey 取出元素的队列
     * @param destinationKey 加入元素的队列
     * @return
     */
    public Object listRightPopAndLeftPush(String sourceKey, String destinationKey){
        try {
            if (StringUtils.isNotEmpty(redisName)){
                sourceKey = redisName + ":" + sourceKey;
                destinationKey = redisName + ":" + destinationKey;
            }
            if (redisTemplate.hasKey(sourceKey)){
                return redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从右取出第一个元素并把该元素从左加入到另一个队列中，直到超过设置的时间后自动返回
     * @param sourceKey 取出元素的队列
     * @param destinationKey 加入元素的队列
     * @param timeout 时间
     * @param unit 时间格式
     * @return
     */
    public Object listRightPopAndLeftPush(String sourceKey, String destinationKey, long timeout, TimeUnit unit){
        try {
            if (StringUtils.isNotEmpty(redisName)){
                sourceKey = redisName + ":" + sourceKey;
                destinationKey = redisName + ":" + destinationKey;
            }
            return redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey, timeout, unit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 扫描当前db上的key，效果等于keys的分页
     * 通常生产环境不允许使用keys等命令，可以使用scan获取keys
     * @param count 数量
     * @param pattern 匹配规则
     * @return keys
     */
    public Set<String> scan(Long count, String pattern){
        Set set = new HashSet();
        try {
            if (count > 0){
                ScanOptions scanOptions = ScanOptions.scanOptions().match(pattern).count(count).build();
                Cursor<byte[]> cursor = redisTemplate.getConnectionFactory().getConnection().scan(scanOptions);
                while (cursor.hasNext()){
                    set.add(new String(cursor.next()));
                }
                return set;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前db上的全部keys
     * @return keys
     */
    public Set<String> scan(){
        Set set = new HashSet();
        try {
            ScanOptions scanOptions = ScanOptions.NONE;
            Cursor<byte[]> cursor = redisTemplate.getConnectionFactory().getConnection().scan(scanOptions);
            while (cursor.hasNext()){
                set.add(new String(cursor.next()));
            }
            return set;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取hash字段里面的keys
     * @param key hash key
     * @param count 数量
     * @param pattern 匹配规则
     * @return keys
     */
    public List<Map.Entry<Object, Object>> hashScan(String key, long count, String pattern){
        List<Map.Entry<Object, Object>> list = new ArrayList<Map.Entry<Object, Object>>();
        try {
            if (count > 0){
                if (StringUtils.isNotEmpty(redisName)){
                    key = redisName + ":" + key;
                }
                ScanOptions scanOptions = ScanOptions.scanOptions().match(pattern).count(count).build();
                Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(key, scanOptions);
                while (cursor.hasNext()){
                    list.add(cursor.next());
                }
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取hash字段里面的全部keys
     * @param key hash key
     * @return keys
     */
    public List<Map.Entry<Object, Object>> hashScan(String key){
        List<Map.Entry<Object, Object>> list = new ArrayList<Map.Entry<Object, Object>>();
        try {
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            ScanOptions scanOptions = ScanOptions.NONE;
            Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(key, scanOptions);
            while (cursor.hasNext()){
                list.add(cursor.next());
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取hash里面的全部key的数量
     * @param key hash key
     * @return 数量
     */
    public Long hashSize(String key){
        try {
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            return redisTemplate.getConnectionFactory().getConnection().hLen(key.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * redis 分布式锁 加锁
     * @param key 锁唯一标志
     * @param timeout 超时时间
     * @return 加锁结果
     */
    public boolean lock(String key, long timeout){
        try {
            String value = String.valueOf(timeout + System.currentTimeMillis());
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            if(redisTemplate.opsForValue().setIfAbsent(key,value)){
                return true;
            }
            //判断锁超时,防止死锁
            String currentValue = (String)redisTemplate.opsForValue().get(key);
            //如果锁过期
            if(!StringUtils.isEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis()){
                //获取上一个锁的时间value
                String oldValue = (String) redisTemplate.opsForValue().getAndSet(key,value);
                if(!StringUtils.isEmpty(oldValue) && oldValue.equals(currentValue) ){
                    //校验是不是上个对应的,也是防止并发
                    return true;
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            LOGGER.error("[Redis分布式锁] 加锁异常，{}",e);
        }
        return false;
    }
    /**
     * redis 分布式锁 解锁
     * @param key 锁标识
     * @param value 锁值
     * @return 解锁结果
     */
    public boolean unLock(String key,String value){
        try {
            if (StringUtils.isNotEmpty(redisName)){
                key = redisName + ":" + key;
            }
            String currentValue =  (String) redisTemplate.opsForValue().get(key);
            if(!StringUtils.isEmpty(currentValue) && currentValue.equals(value) ){
                //删除key
                redisTemplate.opsForValue().getOperations().delete(key);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("[Redis分布式锁] 解锁异常，{}",e);
        }
        return false;
    }
}
