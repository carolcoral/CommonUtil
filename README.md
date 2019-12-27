# CommonUtil
* <b>Desc: A common utils jar for using redis elasticsearch Rest and file even more these.</b>

## Download
[CommonUtil.jar](https://github.com/carolcoral/CommonUtil/releases/download/1.0.1/CommonUtil-1.0.1.jar)

[CommonUtil-javadoc.jar](https://github.com/carolcoral/CommonUtil/releases/download/1.0.1/CommonUtil-1.0.1-javadoc.jar)

[CommonUtil-sources.jar](https://github.com/carolcoral/CommonUtil/releases/download/1.0.1/CommonUtil-1.0.1-sources.jar)

## [WIKI](https://github.com/carolcoral/CommonUtil/wiki)

## Log

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
