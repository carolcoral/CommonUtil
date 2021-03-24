package site.cnkj.common.config;


import com.mongodb.*;
import com.mongodb.selector.ServerSelector;
import lombok.Setter;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.cnkj.common.utils.data.MongoClientsInit;

import javax.net.ssl.SSLContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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
    private List<MongoCompressor> compressorList = Collections.emptyList();
    private ReadPreference readPreference = ReadPreference.primary();
    private WriteConcern writeConcern = WriteConcern.ACKNOWLEDGED;
    private boolean retryWrites = true;
    private boolean retryReads = true;
    private ReadConcern readConcern = ReadConcern.DEFAULT;
    private CodecRegistry codecRegistry = MongoClient.getDefaultCodecRegistry();
    private UuidRepresentation uuidRepresentation = UuidRepresentation.UNSPECIFIED;
    private ServerSelector serverSelector;
    private int minConnectionsPerHost;
    private int maxConnectionsPerHost = 100;
    private int serverSelectionTimeout = 1000 * 30;
    private int maxWaitTime = 1000 * 60 * 2;
    private int maxConnectionIdleTime;
    private int maxConnectionLifeTime;
    private int connectTimeout = 1000 * 10;
    private int socketTimeout = 0;
    private boolean sslEnabled = false;
    private boolean sslInvalidHostNameAllowed = false;
    private SSLContext sslContext;

    private int heartbeatFrequency = 10000;
    private int minHeartbeatFrequency = 500;
    private int heartbeatConnectTimeout = 20000;
    private int heartbeatSocketTimeout = 20000;
    private int localThreshold = 15;


    private MongoClientOptions.Builder builder(){
        //配制连接池
        MongoClientOptions.Builder mongoClientBuilder = new MongoClientOptions.Builder();
        mongoClientBuilder.applicationName(applicationName);
        mongoClientBuilder.autoEncryptionSettings(AutoEncryptionSettings.builder().build());
        mongoClientBuilder.codecRegistry(codecRegistry);
        mongoClientBuilder.compressorList(compressorList);
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
        mongoClientBuilder.readConcern(readConcern);
        mongoClientBuilder.readPreference(readPreference);
        mongoClientBuilder.retryReads(retryReads);
        mongoClientBuilder.retryWrites(retryWrites);
        mongoClientBuilder.serverSelectionTimeout(serverSelectionTimeout);
        mongoClientBuilder.serverSelector(serverSelector);
        mongoClientBuilder.socketTimeout(socketTimeout);
        mongoClientBuilder.sslContext(sslContext);
        mongoClientBuilder.sslEnabled(sslEnabled);
        mongoClientBuilder.sslInvalidHostNameAllowed(sslInvalidHostNameAllowed);
        mongoClientBuilder.uuidRepresentation(uuidRepresentation);
        mongoClientBuilder.writeConcern(writeConcern);
        return mongoClientBuilder;
    }

    @Bean(name = "mongoClientsInit")
    public MongoClientsInit mongoClientsInit(){
        return new MongoClientsInit(uri, builder());
    }

}
