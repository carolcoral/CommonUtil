# CommonUtil
* <b>Desc: A common utils jar for using redis elasticsearch Rest and file even more these.</b>

## [WIKI](https://github.com/carolcoral/CommonUtil/wiki)

## Download
* [CommonUtil.jar](https://github.com/carolcoral/CommonUtil/releases/download/2.0.2/CommonUtil-2.0.2.jar)
* [CommonUtil-javadoc.jar](https://github.com/carolcoral/CommonUtil/releases/download/2.0.2/CommonUtil-2.0.2-javadoc.jar)
* [CommonUtil-sources.jar](https://github.com/carolcoral/CommonUtil/releases/download/2.0.2/CommonUtil-2.0.2-sources.jar)

## Log
> `2021-10-29 13:51:03` add and fix some methods.Update version to 2.1.0.

> `2021-3-24 11:24:53` fix some bugs and change mongoClient init. [Use MongoClientInit](https://github.com/carolcoral/CommonUtil/wiki/MongoClientInit).

> `2021-2-9 11:09:13` change folder and fix some bug.

> `2020-8-20 16:49:23` new class [MongoClientsInit](https://github.com/carolcoral/CommonUtil/blob/master/src/main/java/site/cnkj/utils/MongoClientsInit.java) for create muti mongoClient.More info to see [How to use MongoClientsInit](https://carolcoral.github.io/Article/JAVA/Java%E6%9E%84%E5%BB%BA%E5%A4%9Amongo%E6%95%B0%E6%8D%AE%E6%BA%90%E5%AE%A2%E6%88%B7%E7%AB%AF)

> `2020-7-21 11:20:14` fix some expire bug from [RedisUtil](https://github.com/carolcoral/CommonUtil/blob/master/src/main/java/site/cnkj/utils/RedisUtil.java).

> `2020-7-20 14:17:00` New date function `String formatDateByReg(String date_format, int parseInt)` for format date by The date-format [DateUtil](https://github.com/carolcoral/CommonUtil/blob/master/src/main/java/site/cnkj/utils/DateUtil.java) .For example like this:

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

> `2019-12-27 17:20:34` Demo to show how to operation Protobuf in java.[Java中使用ProtoBuf数据](https://github.com/carolcoral/CommonUtil/wiki/Java中使用ProtoBuf数据)

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
    <version>2.0.2</version>
</dependency>
```
