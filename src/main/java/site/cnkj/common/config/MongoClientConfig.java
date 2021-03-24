package site.cnkj.common.config;


import com.mongodb.MongoClientOptions;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.cnkj.common.utils.data.MongoClientsInit;

import java.util.HashMap;

/*
 * @author  LXW
 * @create  2021/3/24 10:51
 * @Description
 *  1. 使用本bean的时候需要在启动类上加上下面这行注解，否则会自动链接本地mongo出现异常
 *      <code>@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class})</code>
 *  2. 使用MongoClient的时候不能同时使用SpringMongo，如果存在SpringMongo的maven依赖需要去除
 */
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.data.mongodb")
public class MongoClientConfig {


    private HashMap<String, String> uri = new HashMap<>();
    private String applicationName;
    private Boolean retryWrites = true;
    private Boolean retryReads = true;
    private Integer minConnectionsPerHost;
    private Integer maxConnectionsPerHost = 100;
    private Integer serverSelectionTimeout = 1000 * 30;
    private Integer maxWaitTime = 1000 * 60 * 2;
    private Integer maxConnectionIdleTime;
    private Integer maxConnectionLifeTime;
    private Integer connectTimeout = 1000 * 10;
    private Integer socketTimeout = 0;
    private Boolean sslEnabled = false;
    private Boolean sslInvalidHostNameAllowed = false;

    private Integer heartbeatFrequency = 10000;
    private Integer minHeartbeatFrequency = 500;
    private Integer heartbeatConnectTimeout = 20000;
    private Integer heartbeatSocketTimeout = 20000;
    private Integer localThreshold = 15;


    private MongoClientOptions.Builder builder(){
        //配制连接池
        MongoClientOptions.Builder mongoClientBuilder = new MongoClientOptions.Builder();
        mongoClientBuilder.applicationName(applicationName);
        mongoClientBuilder.connectionsPerHost(maxConnectionsPerHost);
        mongoClientBuilder.connectTimeout(connectTimeout);
        mongoClientBuilder.heartbeatConnectTimeout(heartbeatConnectTimeout);
        mongoClientBuilder.heartbeatFrequency(heartbeatFrequency);
        mongoClientBuilder.heartbeatSocketTimeout(heartbeatSocketTimeout);
        mongoClientBuilder.localThreshold(localThreshold);
        mongoClientBuilder.maxConnectionIdleTime(maxConnectionIdleTime);
        mongoClientBuilder.maxConnectionLifeTime(maxConnectionLifeTime);
        mongoClientBuilder.maxWaitTime(maxWaitTime);
        mongoClientBuilder.minConnectionsPerHost(minConnectionsPerHost);
        mongoClientBuilder.minHeartbeatFrequency(minHeartbeatFrequency);
        mongoClientBuilder.retryReads(retryReads);
        mongoClientBuilder.retryWrites(retryWrites);
        mongoClientBuilder.serverSelectionTimeout(serverSelectionTimeout);
        mongoClientBuilder.socketTimeout(socketTimeout);
        mongoClientBuilder.sslEnabled(sslEnabled);
        mongoClientBuilder.sslInvalidHostNameAllowed(sslInvalidHostNameAllowed);
        return mongoClientBuilder;
    }

    @Bean(name = "mongoClientsInit")
    public MongoClientsInit mongoClientsInit(){
        return new MongoClientsInit(uri, builder());
    }

}
