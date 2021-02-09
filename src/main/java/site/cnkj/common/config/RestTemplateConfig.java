package site.cnkj.common.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import site.cnkj.common.utils.http.RestTemplateUtil;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by rx on 2018/12/13.
 */
@Configuration
@ConditionalOnClass(value = { RestTemplate.class, HttpClient.class })
public class RestTemplateConfig {

	@Value("${restTemplate.remote.maxTotalConnect:0}")
	private int maxTotalConnect; // 连接池的最大连接数默认为0
	@Value("${restTemplate.remote.maxConnectPerRoute:200}")
	private int maxConnectPerRoute; // 单个主机的最大连接数
	@Value("${restTemplate.remote.connectTimeout:2000}")
	private int connectTimeout; // 连接超时默认2s
	@Value("${restTemplate.remote.readTimeout:30000}")
	private int readTimeout; // 读取超时默认30s

	// 创建HTTP客户端工厂
	private ClientHttpRequestFactory createFactory() {
		if (this.maxTotalConnect <= 0) {
			SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
			factory.setConnectTimeout(this.connectTimeout);
			factory.setReadTimeout(this.readTimeout);
			return factory;
		}
		HttpClient httpClient = HttpClientBuilder.create().setMaxConnTotal(this.maxTotalConnect)
				.setMaxConnPerRoute(this.maxConnectPerRoute).build();
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
		factory.setConnectTimeout(this.connectTimeout);
		factory.setReadTimeout(this.readTimeout);
		return factory;
	}

	// 初始化RestTemplate,并加入spring的Bean工厂，由spring统一管理
	@Bean
	@ConditionalOnMissingBean(RestTemplate.class)
	public RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate(this.createFactory());
		List<HttpMessageConverter<?>> converterList = restTemplate.getMessageConverters();

		// 重新设置StringHttpMessageConverter字符集为UTF-8，解决中文乱码问题
		HttpMessageConverter<?> converterTarget = null;
		for (HttpMessageConverter<?> item : converterList) {
			if (StringHttpMessageConverter.class == item.getClass()) {
				converterTarget = item;
				break;
			}
		}
		if (null != converterTarget) {
			converterList.remove(converterTarget);
		}
		converterList.add(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		return restTemplate;
	}

	@Bean(name = "restTemplateUtil")
	public RestTemplateUtil restTemplateUtil(RestTemplate restTemplate) {
		RestTemplateUtil restTemplateUtil = new RestTemplateUtil();
		restTemplateUtil.setRestTemplate(restTemplate);
		return restTemplateUtil;
	}

}
