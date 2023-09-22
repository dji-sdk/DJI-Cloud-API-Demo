/*************************************************
 * @copyright 2017 Flision Corporation Inc.
 * @author: Vincent Chan @ Canton
 * @date: 2023年09月22日
 * @version: 1.0.0
 * @description:
 **************************************************/
package com.dji.sdk.common;

import com.dji.sdk.mqtt.CommonTopicRequest;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PublishConfiguration {

    String bid;
    String tid;

    //默认超时
    int timeout = 3;
    //请求发送前调用
    Consumer<CommonTopicRequest> beforePublishHook = (e)->{};
    //收到请求回信后调用
    BiConsumer<CommonTopicRequest, PublishBarrierResult> afterPublishHook = (req,rsp) ->{};


    public String getBid() {
        return bid;
    }

    public String getTid() {
        return tid;
    }

    public long getTimeout() {
        return timeout * 1000;
    }

    public void setBizId(String bid) {
        this.bid = bid;
    }

    public void setTransactionId(String tid) {
        this.tid = tid;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setBeforePublishHook(Consumer<CommonTopicRequest> callback) {
        beforePublishHook = callback;
    }

    public void setAfterPublishReplyHook(BiConsumer<CommonTopicRequest, PublishBarrierResult> callback) {
        afterPublishHook = callback;
    }

    public void invokeBeforePublishHook(CommonTopicRequest req){
        if(Objects.nonNull(beforePublishHook)){
            try {
                beforePublishHook.accept(req);
            }catch (Throwable ex){
                //do nothing
                //业务层的异常不理会
            }
        }
    }

    public void invokeAfterPublishReplyHook(CommonTopicRequest req, PublishBarrierResult result){
        if(Objects.nonNull(afterPublishHook)){
            try{
                afterPublishHook.accept(req,result);
            }catch (Throwable ex){
                //do nothing
                //业务层的异常不理会
            }
        }
    }
}
