[TOC]

# CommonUtil
* <b>Desc: A common utils jar for using redis elasticsearch Rest even and file.</b>

## Download
[CommonUtil-1.0.jar]()
[CommonUtil-1.0-javadoc.jar]()
[CommonUtil-1.0-sources.jar]()

## How to use
### Import it in your pom

#### Import jar into your maven
```shell
mvn install:install-file -Dfile=<path-to-file> -DgroupId=site.cnkj -DartifactId=CommonUtil -Dversion=1.0 -Dpackaging=jar
```
> Be care for:<path-to-file> is your local jar of full path.

> For example:
```shell
mvn install:install-file -Dfile=/User/carol/Desktop/CommonUtil-1.0.jar -DgroupId=site.cnkj -DartifactId=CommonUtil -Dversion=1.0 -Dpackaging=jar
```

#### Import dependency into your pom.xml
```yaml
<dependency>
    <groupId>site.cnkj</groupId>
    <artifactId>CommonUtil</artifactId>
    <version>1.0</version>
</dependency>
```

## Use in project
1.First import it in your application
```java
@SpringBootApplication
@ComponentScan(basePackages = "site.cnkj.*",
        basePackageClasses = {
                RestTemplateConfig.class,
                RedisConfig.class
        })
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    @Resource(name = "restTemplateUtil")
    public RestTemplateUtil restTemplateUtil;
    @Resource(name = "redisUtil")
    public RedisUtil redisUtil;
}
```

2.Then autowired in your class
```java
@Component
public class TestClass {
    @Autowired
    RedisUtil redisUtil;
    
    @Autowired
    private RestTemplateUtil restTemplateUtil;
    
    public void test(){
        redisUtils.scanAll();
        restTemplateUtil.getWithQ("http://127.0.0.1", Object.class, new HashMap<>().put("test", "1"));
    }
}
```


### Redis
#### Redis single
> Add these config info in your application.properties
```yaml
//set prefix name for all key
spring.redis.prefixName=
//set database which you want to use
spring.redis.database=0
//set redis connection host
spring.redis.host=127.0.0.1
//set redis connection port
spring.redis.port=6379
//set redis connection failed callback time
spring.redis.timeout=10s
//set redis subdescripton channel when you wanto to use it
spring.redis.subDescription.channel= ${spring.application.name}:flush
```

#### Redis sentinel
> Add these config info in your application.properties
```yaml
//set prefix name for all key
spring.redis.prefixName=
//set database which you want to use
spring.redis.database=0
//set redis connection failed callback time
spring.redis.timeout=10s
spring.redis.sentinel.nodes = 127.0.0.1:6381,127.0.0.1:6382,127.0.0.1:6383
spring.redis.sentinel.master = sentinel-127.0.0.1-6379
```

#### Redis cluster
> Add these config info in your application.properties
```yaml
//set prefix name for all key
spring.redis.prefixName=
//set database which you want to use
spring.redis.database=0
//set redis connection failed callback time
spring.redis.timeout=10s
spring.redis.cluster.nodes = 127.0.0.1:6379,127.0.0.1:6380,127.0.0.1:6381,127.0.0.1:6382,127.0.0.1:6383
```

#### Redis subdescription/publish
1.Implements Receiver
```java
@Component
@Slf4j
public class SubDescription implements Receiver {
    /**
     * @param message redis published message
     * @return
     */
    @Override
    public Object receiver(String message) {
        //TODO what you want to do by use received message
        return true;
    }
}

```

2.Add this configuration in your application.properties after redis config
```yaml
spring.redis.subDescription.channel= 
```

### ElasticSearch
1.Add configuration in application.properties
```yaml
common.elasticsearch.cluster.name = site_cnkj_test
common.elasticsearch.clusterNodes=127.0.0.1:8000,127.0.0.1:8001
common.elasticsearch.username = elastic
common.elasticsearch.password = changeme
common.elasticsearch.pool = 100
common.elasticsearch.snifferinterval = 180000
common.elasticsearch.socketTimeout = 180000
common.elasticsearch.maxRetryTimeoutMillis = 180000
common.elasticsearch.connectTimeout = 180000
```

2.Autowried client in your class
```java
@Service
public class ElasticSearchService{
    //@Resource(name = "HighLevelClient")
    //RestHighLevelClient client;

    @Resource(name = "HighLevelSniffClient")
    RestHighLevelClient client;
    
    public void search(String startTime, String endTime, String filterKey, String filterValue){
        //Timeout (seconds)
        int elasticsearchTimeout = 1000 * 60;
        //Single request quantity
        int elasticsearchSize = 10000;
        try {
            final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(10L));
            SearchRequest searchRequest = new SearchRequest(index);
            searchRequest.types(type);
            searchRequest.scroll(scroll);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            //Aggregate statement
            searchSourceBuilder.query(QueryBuilders.boolQuery()
                    .must(QueryBuilders.matchPhraseQuery(filterKey, filterValue))
                    .must(QueryBuilders.rangeQuery("@timestamp").gte(startTime).lte(endTime))
            )
                    .timeout(new TimeValue(elasticsearchTimeout,TimeUnit.SECONDS))
                    .size(elasticsearchSize);
            searchRequest.source(searchSourceBuilder);
            // Print the executed DSL statement, which can be used directly in Kibana
            // LOGGER.info("\n"+searchSourceBuilder.toString());
            SearchResponse searchResponse = client.search(searchRequest);
            for (SearchHit hit : searchResponse.getHits().getHits()) {
                // TODO 
                String res = hit.getSourceAsString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### Thread Pool
1.Add config in your application.properties
```yaml
spring.async.pool.corePoolSize = 10
spring.async.pool.maxPoolSize = 100
spring.async.pool.keepAliveSeconds = 60
spring.async.pool.queueCapacity = 25
```

2.Import it in your application
```java
@SpringBootApplication
@ComponentScan(basePackages = "site.cnkj.*",
        basePackageClasses = {
                AsyncThreadPoolConfig.class,
                AsyncExecutePool.class
        })
public class TestApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(TestApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

3.Use in your class
```java
@Service
public class AsyncExecutePool {

    @Async(value = "myTaskAsyncPool")
    public void execute() {
        //TODO
    }
}
```

