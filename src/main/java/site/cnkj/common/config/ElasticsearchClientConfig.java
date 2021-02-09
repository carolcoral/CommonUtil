package site.cnkj.common.config;

import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Objects;

/*
 * @author  LXW
 * @create  2020/10/16 11:00
 * @Description support elasticsearch 7.1.1 version
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.customize.elasticsearch")
@ConditionalOnProperty(prefix="spring.customize.elasticsearch", name = "clusterNodes")
public class ElasticsearchClientConfig {

    private String[] clusterNodes;
    private String username;
    private String password;
    private Integer sniffer_interval = 60000;
    private Integer max_retry_timeout = 30000; //带超时时间式(毫秒级)
    private Integer failure_delay = 3000;
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
            try {
                return new HttpHost(InetAddress.getByName(ip), port, HTTP_SCHEME);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private RestClientBuilder restClientBuilder() throws Exception{
        HttpHost[] hosts = Arrays.stream(clusterNodes)
                .map(this::makeHttpHost)
                .filter(Objects::nonNull)
                .toArray(HttpHost[]::new);

        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

        RestClientBuilder restClientBuilder = RestClient.builder(hosts).setHttpClientConfigCallback(
                httpAsyncClientBuilder -> httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
        ).setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
            @Override
            public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder builder) {
                builder.setConnectTimeout(connectTimeout);
                builder.setSocketTimeout(socketTimeout);
                return builder;
            }
        });
        //set header, after es6.0 need change Content-type to application/json
        Header[] defaultHeaders = new Header[]{
                new BasicHeader("Content-type", "application/json")
        };
        restClientBuilder.setDefaultHeaders(defaultHeaders);
        return restClientBuilder;
    }

    @Bean(name = "ElasticsearchRestClient")
    public RestClient restClient(){
        try {
            return restClientBuilder().build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean(name = "ElasticsearchHighLevelClient")
    public RestHighLevelClient restHighLevelClient(){
        try {
            return new RestHighLevelClient(restClientBuilder());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
