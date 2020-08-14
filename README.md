# CommonUtil
* <b>Desc: A common utils jar for using redis elasticsearch Rest and file even more these.</b>

## [WIKI](https://github.com/carolcoral/CommonUtil/wiki)

## Log
> `2020年8月14日 17:56:01` new class [MongoClientsInit](https://github.com/carolcoral/CommonUtil/blob/master/src/main/java/site/cnkj/utils/MongoClientsInit.java) for create muti mongoClient.More info to see [How to use MongoClientsInit](https://carolcoral.github.io/Article/JAVA/Java%E6%9E%84%E5%BB%BA%E5%A4%9Amongo%E6%95%B0%E6%8D%AE%E6%BA%90%E5%AE%A2%E6%88%B7%E7%AB%AF)

> `2020年7月21日 11:20:14` fix some expire bug from [RedisUtil](https://github.com/carolcoral/CommonUtil/blob/master/src/main/java/site/cnkj/utils/RedisUtil.java).

> `2020年7月20日 14:17:00` New date function `String formatDateByReg(String date_format, int parseInt)` for format date by The date-format [DateUtil](https://github.com/carolcoral/CommonUtil/blob/master/src/main/java/site/cnkj/utils/DateUtil.java).For example like this:

```java
public static void main(String[] args) {
    List<String> date_list = Arrays.asList(
            "YYYY",
            "YYYY-MM",
            "YYYY-MM-dd",
            "YYYY-MM-dd HH",
            "YYYY-MM-dd HH:mm",
            "YYYY-MM-dd HH:mm:ss"
    );
    int parseInt = -1;
    for (String date_format : date_list) {
        System.out.println("current date_format is: "+date_format);
        String formatDateByReg = formatDateByReg(date_format, parseInt);
        System.out.println("current format result is:"+formatDateByReg);
    }
}
current date_format is: YYYY
current format result is:2019
current date_format is: YYYY-MM
current format result is:2020-06
current date_format is: YYYY-MM-dd
current format result is:2020-07-19
current date_format is: YYYY-MM-dd HH
current format result is:2020-07-20 13
current date_format is: YYYY-MM-dd HH:mm
current format result is:2020-07-20 14:14
current date_format is: YYYY-MM-dd HH:mm:ss
current format result is:2020-07-20 14:15:08
```

> `2019年12月27日 17:20:34` Demo to show how to operation Protobuf in java.[Java中使用ProtoBuf数据](https://github.com/carolcoral/CommonUtil/wiki/Java中使用ProtoBuf数据)

> `2019-12-05 14:33:23` New class for remotely connect to the server and execute the script.[SSH2Util](https://github.com/carolcoral/CommonUtil/blob/master/src/main/java/site/cnkj/utils/SSH2Util.java)

## How to use
### Import it in your pom
#### Import jar into your maven
```shell
mvn install:install-file -Dfile=<path-to-file> -DgroupId=site.cnkj -DartifactId=CommonUtil -Dversion=1.0 -Dpackaging=jar
```
> Note:<path-to-file> is your local jar of full path.

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

||||||
|:---|:--|:--|:--|:--|
|[RedisUtil](https://github.com/carolcoral/CommonUtil/wiki/RedisUtil)|[ElasticSearch](https://github.com/carolcoral/CommonUtil/wiki/ElasticSearch)|[RestTemplateUtil](https://github.com/carolcoral/CommonUtil/wiki/RestTemplateUtil)|[MongodbUtil](https://github.com/carolcoral/CommonUtil/wiki/MongodbUtil)|[Thread Pool](https://github.com/carolcoral/CommonUtil/wiki/Thread-Pool)|
|[SSH2Util](https://github.com/carolcoral/CommonUtil/wiki/SSH2Util)|[DateUtil](https://github.com/carolcoral/CommonUtil/blob/master/src/main/java/site/cnkj/utils/DateUtil.java)|[FileEncryptDecrypt](https://github.com/carolcoral/CommonUtil/blob/master/src/main/java/site/cnkj/utils/FileEncryptDecrypt.java)|[FileUtil](https://github.com/carolcoral/CommonUtil/blob/master/src/main/java/site/cnkj/utils/FileUtil.java)|
|[GracefulShutdown](https://github.com/carolcoral/CommonUtil/blob/master/src/main/java/site/cnkj/utils/GracefulShutdown.java)|[HttpCommonUtil](https://github.com/carolcoral/CommonUtil/blob/master/src/main/java/site/cnkj/utils/HttpCommonUtil.java)|[KafkaUtil](https://github.com/carolcoral/CommonUtil/blob/master/src/main/java/site/cnkj/utils/KafkaUtil.java)|[LoggerUtil](https://github.com/carolcoral/CommonUtil/blob/master/src/main/java/site/cnkj/utils/LoggerUtil.java)|[StringUtil](https://github.com/carolcoral/CommonUtil/blob/master/src/main/java/site/cnkj/utils/StringUtil.java)|
