package com.dji.sample.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 *
 * @author sean.zhou
 * @date 2021/11/10
 * @version 0.1
 */
@Configuration
public class GlobalThreadPoolConfiguration {

    @Value("${thread.pool.core-pool-size: 10}")
    private int corePoolSize;

    @Value("${thread.pool.maximum-pool-size: 20}")
    private int maximumPoolSize;

    @Value("${thread.pool.keep-alive-time: 60}")
    private long keepAliveTime;

    @Value("${thread.pool.queue.capacity: 1000}")
    private int capacity;

    /**
     * A custom thread pool.
     * @return
     */
    @Bean
    public Executor threadPool() {
        return new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize, keepAliveTime,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(capacity),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.DiscardOldestPolicy());
    }

}
