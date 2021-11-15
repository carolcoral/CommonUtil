package site.cnkj.common.utils.system;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextClosedEvent;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*
 * @version 1.0 created by LXW on 2019/4/25 16:22
 */
public class GracefulShutdown implements TomcatConnectorCustomizer, ApplicationListener<ContextClosedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GracefulShutdown.class);

    private volatile Connector connector;

    @Override
    public void customize(Connector connector) {
        this.connector = connector;
    }
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        try {
            // 指定执行的方法
            shutdown();
            //手动清理内存
            Runtime.getRuntime().gc();
            LOGGER.warn("清理内存完毕，正在退出服务......");
            if (this.connector == null){
                return;
            }
            this.connector.pause();
            LOGGER.warn("关闭全部连接......");
            Executor executor = this.connector.getProtocolHandler().getExecutor();
            if (executor instanceof ThreadPoolExecutor) {
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
                threadPoolExecutor.shutdown();
                LOGGER.warn("当前服务线程池被关闭");
                if (!threadPoolExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
                    LOGGER.warn("Tomcat thread pool did not shut down gracefully within 30 seconds. Proceeding with forceful shutdown");
                }
            }
            this.connector.stop();
        } catch (LifecycleException e) {
            e.printStackTrace();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    @Bean
    public GracefulShutdown gracefulShutdown() {
        return new GracefulShutdown();
    }
    @Bean
    public ConfigurableServletWebServerFactory webServerFactory(final GracefulShutdown gracefulShutdown) {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(gracefulShutdown);
        return factory;
    }


    /**
     * 执行服务关闭前的一些定制化操作
     * 通常需要确认以下步骤
     * 1.关闭kafka等数据连接
     * 2.flush内存中全部的未处理数据
     * 3.清理服务中全部待处理的数据
     */
    public void shutdown(){
        //do something
    }
}
