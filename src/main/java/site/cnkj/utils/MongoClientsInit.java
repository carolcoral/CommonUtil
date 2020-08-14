package site.cnkj.utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/*
 * @author  Carol
 * @create  2020/8/14 15:50
 * @Description
 */
@Component
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.data.mongodb")
@ConditionalOnProperty(prefix = "spring.data.mongodb", name = "activity", havingValue = "true")
public class MongoClientsInit {

    private HashMap<String, String> uri;

    private HashMap<String, MongoDatabase> mongoClientDatabases = new HashMap<>();

    private String getDatabase(String mongoUri){
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


    public void init() throws Exception{
        for (String name : uri.keySet()) {
            String url = uri.get(name);
            MongoClient mongoClient = MongoClients.create(url);
            MongoDatabase mongoClientDatabase = mongoClient.getDatabase(getDatabase(url));
            mongoClientDatabases.put(name, mongoClientDatabase);
        }
    }


}
