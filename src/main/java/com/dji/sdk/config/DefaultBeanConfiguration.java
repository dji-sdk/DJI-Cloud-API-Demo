/*************************************************
 * @copyright 2017 Flision Corporation Inc.
 * @author: Vincent Chan @ Canton
 * @date: 2023年09月22日
 * @version: 1.0.0
 * @description:
 **************************************************/
package com.dji.sdk.config;

import com.dji.sdk.common.JDKLockBarrierImpl;
import com.dji.sdk.common.PublishBarrier;
import com.dji.sdk.mqtt.ChanBarrierAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultBeanConfiguration {

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
