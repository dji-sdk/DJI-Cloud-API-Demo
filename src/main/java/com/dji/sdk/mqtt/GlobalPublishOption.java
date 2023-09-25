/*************************************************
 * @copyright 2017 Flision Corporation Inc.
 * @author: Vincent Chan @ Canton
 * @date: 2023年09月25日
 * @version: 1.0.0
 * @description: 全局发送默认配置
 **************************************************/
package com.dji.sdk.mqtt;

import com.dji.sdk.common.PublishBarrierResult;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface GlobalPublishOption {
    Supplier<String> defaultTransactionId();
    Supplier<String> defaultBizId();

    Consumer<CommonTopicRequest> defaultBeforePublishHook();
    BiConsumer<CommonTopicRequest, PublishBarrierResult> defaultAfterPublishHook();

}
