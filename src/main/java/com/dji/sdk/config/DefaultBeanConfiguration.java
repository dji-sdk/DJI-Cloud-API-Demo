/*************************************************
 * @copyright 2017 Flision Corporation Inc.
 * @author: Vincent Chan @ Canton
 * @date: 2023年09月22日
 * @version: 1.0.0
 * @description:
 **************************************************/
package com.dji.sdk.config;

import com.dji.sdk.common.*;
import com.dji.sdk.mqtt.ChanBarrierAdapter;
import com.dji.sdk.mqtt.CommonTopicRequest;
import com.dji.sdk.mqtt.GlobalPublishOption;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Configuration
public class DefaultBeanConfiguration {

    /**
     * 全局发送默认设置
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(GlobalPublishOption.class)
    public GlobalPublishOption defaultPublishOption(){
        return new GlobalPublishOption() {
            @Override
            public Supplier<String> defaultTransactionId() {
                return ()-> UUID.randomUUID().toString();
            }

            @Override
            public Supplier<String> defaultBizId() {
                return ()-> UUID.randomUUID().toString();
            }

            @Override
            public Consumer<CommonTopicRequest> defaultBeforePublishHook() {
                return null;
            }

            @Override
            public BiConsumer<CommonTopicRequest, PublishBarrierResult> defaultAfterPublishHook() {
                return null;
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean(SDKManager.class)
    public SDKManager localCacheSDKManager(){
        return new LocalCacheSDKManager();
    }

    /**
     * 使用者可以自定义PublishBarrier的实现，默认采用Chan实现
     */
    @Bean
    @ConditionalOnMissingBean(PublishBarrier.class)
    public PublishBarrier chanBarrier(){
        /** 原Chan实现 */
        return new ChanBarrierAdapter();
    }

    /**
     * PublishBarrier 另一个实现, 采用同步锁
     */
//    @Bean
//    @ConditionalOnMissingBean(PublishBarrier.class)
//    public PublishBarrier jdkBarrier(){
//        return new JDKLockBarrierImpl();
//    }
}
