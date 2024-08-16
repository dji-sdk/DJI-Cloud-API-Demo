/*************************************************
 * @copyright 2017 Flision Corporation Inc.
 * @author: Vincent Chan @ Canton
 * @date: 2023年09月22日
 * @version: 1.0.0
 * @description:
 **************************************************/
package com.dji.sdk.common;

import com.dji.sdk.mqtt.Chan;
import com.dji.sdk.mqtt.CommonTopicRequest;
import com.dji.sdk.mqtt.CommonTopicResponse;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class JDKLockBarrierImpl implements PublishBarrier{

    /**
     * 在我的实现中是采用一个定期清理的TimedCache储存请求
     */
    private final ConcurrentHashMap<String, JDKHolder> container = new ConcurrentHashMap<>();

    @Override
    public String generateIdentity(CommonTopicRequest requestData) {
        return requestData.getTid();
    }

    @Override
    public String generateIdentity(CommonTopicResponse receiveData) {
        return receiveData.getTid();
    }

    @Override
    public void put(String identity, CommonTopicResponse receiveData) {
        if(hasIdentity(identity)){
            container.get(identity).setData(receiveData);
        }
    }

    @Override
    public void registerRequest(String identity, CommonTopicRequest requestData) {
        container.put(identity,new JDKHolder());
    }

    @Override
    public PublishBarrierResult await(String identity, long timeout) {
        JDKHolder jdkHolder = container.get(identity);
        if(Objects.isNull(jdkHolder)){
            throw new RuntimeException("等待指令返回前未注册指令到栅栏");
        }
        jdkHolder.await(timeout);

        return jdkHolder.getResult();
    }

    @Override
    public boolean hasIdentity(String identity) {
        return container.containsKey(identity);
    }

    public static class JDKHolder{
        volatile Object locker = new Object();
        CommonTopicResponse response = null;

        public void await(long timeout) {
            synchronized (locker){
                try {
                    locker.wait(timeout);
                }catch (InterruptedException e){}
            }
        }

        public void setData(CommonTopicResponse receiveData) {
            this.response = receiveData;
            synchronized (locker){
                locker.notify();
            }
        }

        public PublishBarrierResult getResult() {
            if(Objects.nonNull(response)){
                return PublishBarrierResult.ok(response);
            }else{
                return PublishBarrierResult.EMPTY;
            }
        }
    }
}
