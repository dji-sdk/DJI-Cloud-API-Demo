/*************************************************
 * @copyright 2017 Flision Corporation Inc.
 * @author: Vincent Chan @ Canton
 * @date: 2023年09月22日
 * @version: 1.0.0
 * @description:
 **************************************************/
package com.dji.sdk.common;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PublishConfiguration implements ReadonlyPublishConfiguration {

    String bid;
    String tid;

    //默认超时
    int timeout = 3;
    //请求发送前调用
    Consumer<PublishRequest> beforePublishHook = null;
    //收到请求回信后调用
    BiConsumer<PublishRequest, PublishBarrierResult> afterPublishHook = null;


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

    public void setBeforePublishHook(Consumer<PublishRequest> callback) {
        beforePublishHook = callback;
    }

    public void setAfterPublishReplyHook(BiConsumer<PublishRequest, PublishBarrierResult> callback) {
        afterPublishHook = callback;
    }

    public void invokeBeforePublishHook(PublishRequest req){
        if(Objects.nonNull(beforePublishHook)){
            try {
                beforePublishHook.accept(req);
            }catch (Throwable ex){
                //do nothing
                //业务层的异常不理会
            }
        }
    }

    public void invokeAfterPublishReplyHook(PublishRequest req, PublishBarrierResult result){
        if(Objects.nonNull(afterPublishHook)){
            try{
                afterPublishHook.accept(req,result);
            }catch (Throwable ex){
                //do nothing
                //业务层的异常不理会
            }
        }
    }

    public boolean noneBeforePublishHook() {
        return Objects.isNull(beforePublishHook);
    }

    public boolean noneAfterPublishHook() {
        return Objects.isNull(afterPublishHook);
    }
}
