package site.cnkj.utils.config;

import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.sniff.SniffOnFailureListener;
import org.elasticsearch.client.sniff.Sniffer;
import org.elasticsearch.client.sniff.SnifferBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Objects;

/*
 * @version 1.0 created by LXW on 2018/11/22 9:43
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "common.elasticsearch")
@ConditionalOnProperty(prefix="common.elasticsearch", name = "clusterNodes")
public class ElasticsearchConfig {

    private String[] clusterNodes;
    private String username;
    private String password;
    private Integer snifferinterval = 60000;
    private Integer maxretrytimeout = 30000; //带超时时间式(毫秒级)
    private Integer failuredelay    = 3000;
    private Integer connectTimeout = 5000;
    private Integer socketTimeout = 60000;
    private Integer maxRetryTimeoutMillis=60000;
    private static final int BUFFER_SIZE = 300 * 1024 * 1024;

    private static final int ADDRESS_LENGTH = 2;

    private static final String HTTP_SCHEME = "http";

    private HttpHost makeHttpHost(String s) {
        assert StringUtils.isNotEmpty(s);
        String[] address = s.split(":");
        if (address.length == ADDRESS_LENGTH) {
            String ip = address[0];
            int port = Integer.parseInt(address[1]);
            return new HttpHost(ip, port, HTTP_SCHEME);
        } else {
            return null;
        }
    }

    @Bean(name = "RestSniffClient")
    public RestClient restEsClient() {
        HttpHost[] hosts = Arrays.stream(clusterNodes)
                .map(this::makeHttpHost)
                .filter(Objects::nonNull)
                .toArray(HttpHost[]::new);
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        //自动扫描网段
        //监听同网段服务
        //Low Level Client init
        RestClientBuilder builder = RestClient.builder(hosts).setHttpClientConfigCallback(
                httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
        ).setRequestConfigCallback((RequestConfig.Builder build) -> {
            build.setConnectTimeout(connectTimeout);
            build.setSocketTimeout(socketTimeout);
            return build;});
        builder.setMaxRetryTimeoutMillis(maxRetryTimeoutMillis);
        SniffOnFailureListener sniffOnFailureListener = new SniffOnFailureListener();
        builder.setFailureListener(sniffOnFailureListener);
        RestClient lowLevelRestClient = builder.build();
        SnifferBuilder snifferBuilder = Sniffer.builder(lowLevelRestClient).setSniffIntervalMillis(snifferinterval);
        if (failuredelay > 0) {
            snifferBuilder.setSniffAfterFailureDelayMillis(failuredelay);
        }
        sniffOnFailureListener.setSniffer(snifferBuilder.build());
        return lowLevelRestClient;
    }

    //具备自动扫描功能的客户端
    @Bean(name = "HighLevelSniffClient")
    public RestHighLevelClient highLevelSniffClient(@Qualifier("RestSniffClient") RestClient restClient) {
        return new RestHighLevelClient(restClient);
    }

    //只扫描指定节点的客户端
    @Bean(name = "RestClient")
    public RestClient restClient() {
        HttpHost[] hosts = Arrays.stream(clusterNodes)
                .map(this::makeHttpHost)
                .filter(Objects::nonNull)
                .toArray(HttpHost[]::new);
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

        RestClientBuilder builder = RestClient.builder(hosts).setHttpClientConfigCallback(
                httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
        ).setRequestConfigCallback((RequestConfig.Builder build) -> {
            build.setConnectTimeout(connectTimeout);
            build.setSocketTimeout(socketTimeout);
            return build;});

        builder.setMaxRetryTimeoutMillis(maxRetryTimeoutMillis);
        RestClient lowLevelRestClient = builder.build();
        return lowLevelRestClient;
    }

    //只扫描指定节点的客户端
    @Bean(name = "HighLevelClient")
    public RestHighLevelClient highLevelClient(@Qualifier("RestClient") RestClient restClient) {
        return new RestHighLevelClient(restClient);
    }



}
