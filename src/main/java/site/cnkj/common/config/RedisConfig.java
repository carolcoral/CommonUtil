package site.cnkj.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import site.cnkj.common.object.redis.Receiver;
import site.cnkj.common.utils.data.RedisUtil;

/**
 * Created by rx on 2018/8/22.
 */
@Configuration
public class RedisConfig {


    @Value("${spring.application.name:''}")
    private String redisName = "";

    @Value("${spring.redis.needRedisName:true}")
    private boolean needRedisName = true;

    @Bean
    public RedisTemplate<String, Object> functionDomainRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        initDomainRedisTemplate(redisTemplate, redisConnectionFactory);
        return redisTemplate;
    }

    private void initDomainRedisTemplate(RedisTemplate<String, Object> redisTemplate, RedisConnectionFactory factory) {
        //如果不配置Serializer，那么存储的时候缺省使用String，如果用User类型存储，那么会提示错误User can't cast to String！
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        // 开启事务
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.setConnectionFactory(factory);
    }

    @Bean(name = "redisUtil")
    public RedisUtil redisUtil(RedisTemplate<String, Object> redisTemplate) {
        if (!needRedisName){
            redisName = "";
        }
        return new RedisUtil(redisTemplate, redisName);
    }

    @Bean(name = "adminRedisUtil")
    public RedisUtil adminRedisUtil(RedisTemplate<String, Object> redisTemplate) {
        redisTemplate.setEnableTransactionSupport(false);
        if (!needRedisName){
            redisName = "";
        }
        return new RedisUtil(redisTemplate, redisName);
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.redis.channel", name = "subDescription")
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerAdapter,
                                            MessageListenerAdapter listenerAdapter2,
                                            @Value("${spring.redis.channel.subDescription}") String channel,
                                            @Value("${spring.redis.channel.subDescription2:''}") String channel2) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic(channel));
        container.addMessageListener(listenerAdapter2, new PatternTopic(channel2));
        return container;
    }

    /**
     * 消息监听器适配器，绑定消息处理器，利用反射技术调用消息处理器的业务方法
     * 需要做多少个消息通信就新建多少个bean并且绑定不同的defaultListenerMethod，可以使用同一个component
     * @param receiver
     * @return
     */
    @Bean
    @ConditionalOnBean(Receiver.class)
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiver");
    }

    @Bean
    @ConditionalOnBean(Receiver.class)
    MessageListenerAdapter listenerAdapter2(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiver2");
    }

    /**redis 读取内容的template */
    @Bean
    StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }




}
