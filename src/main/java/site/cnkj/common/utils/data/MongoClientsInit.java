package site.cnkj.common.utils.data;


import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;

/*
 * @author  LXW
 * @create  2020/8/17 9:50
 * @Description
 *  1. 使用本bean的时候需要在启动类上加上下面这行注解，否则会自动链接本地mongo出现异常
 *      <code>@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class})</code>
 *  2. 使用MongoClient的时候不能同时使用SpringMongo，如果存在SpringMongo的maven依赖需要去除
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.data.mongodb")
@ConditionalOnProperty(prefix = "spring.data.mongodb", value = "activity", havingValue = "true")
public class MongoClientsInit {

    private HashMap<String, String> uri = new HashMap<>();

    private HashMap<String, MongoDatabase> mongoClientDatabases = new HashMap<>();

    private String getDatabase(String mongoUri) throws Exception{
        String databaseName = "";
        String substring = mongoUri.substring(10, mongoUri.length());
        String[] split = substring.split("/");
        String database = split[1];
        if (database.contains("?")){
            databaseName = database.split("\\?")[0];
        }else {
            databaseName = database;
        }
        return databaseName;
    }

    @PostConstruct
    public void init() throws Exception{
        if (uri.size() > 0){
            for (String name : uri.keySet()) {
                String url = uri.get(name);
                //配制连接池
                MongoClientOptions.Builder mongoClientBuilder = new MongoClientOptions.Builder();
                mongoClientBuilder.connectionsPerHost(30);
                mongoClientBuilder.connectTimeout(30000);
                mongoClientBuilder.retryWrites(true);
                //支持ssl连接
                MongoClient mongoClient = new MongoClient(new MongoClientURI(url, mongoClientBuilder));
                MongoDatabase mongoClientDatabase = mongoClient.getDatabase(getDatabase(url));
                mongoClientDatabases.put(name, mongoClientDatabase);
            }
        }
    }

}
