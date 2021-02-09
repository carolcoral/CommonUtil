package site.cnkj.common.config;


import com.mongodb.MongoClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import site.cnkj.common.utils.data.MongodbUtil;

/*
 * @version 1.0 created by LXW on 2019/11/22 16:50
 */
@Configuration
@ConditionalOnProperty(prefix="spring.data.mongodb", name = "uri")
public class MongodbConfig {

    private String host;
    private int port;
    private String database;

    /**
     * 默认集合名称前缀
     */
    private String mongoName = "";

    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(host, port), database);
    }

    @Bean
    public MongoTemplate getMongoTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory());
    }

    @Bean(name = "mongodbUtil")
    public MongodbUtil mongodbUtil(MongoTemplate mongoTemplate) {
        return new MongodbUtil(mongoTemplate, mongoName);
    }

}
