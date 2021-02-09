package site.cnkj.common.utils.data;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;

/*
 * @author  LXW
 * @create  2020/8/17 9:50
 * @Description
 */
@Component
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class})
@Configuration
@ConfigurationProperties(prefix = "spring.data.mongodb")
public class MongoClientsInit {

    private HashMap<String, String> uri = new HashMap<>();

    public void setUri(HashMap<String, String> uri) {
        this.uri = uri;
    }

    private HashMap<String, MongoDatabase> mongoClientDatabases = new HashMap<>();

    public HashMap<String, MongoDatabase> getMongoClientDatabases() {
        return mongoClientDatabases;
    }

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
                MongoClient mongoClient = MongoClients.create(url);
                MongoDatabase mongoClientDatabase = mongoClient.getDatabase(getDatabase(url));
                mongoClientDatabases.put(name, mongoClientDatabase);
            }
        }
    }

}
