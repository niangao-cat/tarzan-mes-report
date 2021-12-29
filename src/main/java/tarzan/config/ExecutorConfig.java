package tarzan.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author peng.yuan
 * @ClassName ExecutorConfig
 * @description 线程池初始化
 * @date 2019年11月08日 9:39
 */
@Configuration
@EnableAsync
public class ExecutorConfig {
    @Bean
    public ThreadPoolTaskExecutor asyncServiceExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 配置核心线程数 cpu个数两倍
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors() * 2);
        // 配置最大线程数
        executor.setMaxPoolSize(50);
        // 配置队列大小
        executor.setQueueCapacity(100);
        // 配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("async-service-");

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        // 执行初始化
        executor.initialize();
        return executor;
    }
}