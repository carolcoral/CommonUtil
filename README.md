# CommonUtil
* <b>Desc: A common utils jar for using redis elasticsearch Rest and file even more these.</b>

## Download
[CommonUtil.jar](https://github.com/carolcoral/CommonUtil/releases/download/1.0.1/CommonUtil-1.0.1.jar)

[CommonUtil-javadoc.jar](https://github.com/carolcoral/CommonUtil/releases/download/1.0.1/CommonUtil-1.0.1-javadoc.jar)

[CommonUtil-sources.jar](https://github.com/carolcoral/CommonUtil/releases/download/1.0.1/CommonUtil-1.0.1-sources.jar)

## Log
> `2019-12-05 14:33:23` New class for remotely connect to the server and execute the script.[SSH2Util]()

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
### [RedisUtil](https://github.com/carolcoral/CommonUtil/wiki/RedisUtil)
### [ElasticSearch](https://github.com/carolcoral/CommonUtil/wiki/ElasticSearch)
### [HttpCommonUtil](https://github.com/carolcoral/CommonUtil/wiki/HttpCommonUtil)
### [MongodbUtil](https://github.com/carolcoral/CommonUtil/wiki/MongodbUtil)
### [Thread Pool](https://github.com/carolcoral/CommonUtil/wiki/Thread-Pool)
### [SSH2Util](https://github.com/carolcoral/CommonUtil/wiki/SSH2Util)