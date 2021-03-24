package site.cnkj.common.utils.data;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import java.util.HashMap;

/*
 * @author  LXW
 * @create  2020/8/17 9:50
 * @Description
 */
public class MongoClientsInit {

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

    public MongoClientsInit(HashMap<String, String> uri, MongoClientOptions.Builder mongoClientBuilder){
        try {
            init(uri, mongoClientBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(HashMap<String, String> uri, MongoClientOptions.Builder mongoClientBuilder) throws Exception{
        if (uri.size() > 0){
            for (String name : uri.keySet()) {
                String url = uri.get(name);
                //支持ssl连接
                MongoClient mongoClient = new MongoClient(new MongoClientURI(url, mongoClientBuilder));
                MongoDatabase mongoClientDatabase = mongoClient.getDatabase(getDatabase(url));
                mongoClientDatabases.put(name, mongoClientDatabase);
            }
        }
    }

}