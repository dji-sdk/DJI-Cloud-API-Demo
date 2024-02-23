/*************************************************
 * @copyright 2017 Flision Corporation Inc.
 * @author: Vincent Chan @ Canton
 * @date: 2023年09月22日
 * @version: 1.0.0
 * @description:
 **************************************************/
package com.dji.sdk.common;

import com.google.common.base.Strings;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PublishOption {

    public static Consumer<PublishOption> DEFAULT = (cfg)->{};

    final PublishConfiguration configuration;

    public PublishOption(PublishConfiguration configuration) {
        this.configuration = configuration;
    }

    public PublishOption withBizId(String bid){
        if(!Strings.isNullOrEmpty(bid)){
            configuration.setBizId(bid);
        }
        return this;
    }

    public PublishOption withTransactionId(String tid){
        if(!Strings.isNullOrEmpty(tid)){
            configuration.setTransactionId(tid);
        }
        return this;
    }

    public PublishOption timeout(int second){
        configuration.setTimeout(second);
        return this;
    }

    public PublishOption beforePublish(Consumer<PublishRequest> callback){
        if(Objects.nonNull(callback)){
            configuration.setBeforePublishHook(callback);
        }
        return this;
    }
    public PublishOption afterPublishReply(BiConsumer<PublishRequest, PublishBarrierResult> callback){
        if(Objects.nonNull(callback)){
            configuration.setAfterPublishReplyHook(callback);
        }
        return this;
    }
}
