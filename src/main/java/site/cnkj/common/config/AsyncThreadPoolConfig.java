package site.cnkj.common.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by rx on 2018/4/19.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.async.pool")
@ConditionalOnProperty(prefix="spring.async.pool", name="corePoolSize", matchIfMissing=false)
public class AsyncThreadPoolConfig {
    private int corePoolSize;

    private int maxPoolSize;

    private int keepAliveSeconds;

    private int queueCapacity;
}
