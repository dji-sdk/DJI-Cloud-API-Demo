/*************************************************
 * @copyright 2017 Flision Corporation Inc.
 * @author: Vincent Chan @ Canton
 * @date: 2023年09月22日
 * @version: 1.0.0
 * @description:
 **************************************************/
package com.dji.sdk.common;

import com.dji.sdk.mqtt.CommonTopicResponse;

public class PublishBarrierResult implements PublishResult {

    public static PublishBarrierResult EMPTY = new PublishBarrierResult();

    public static PublishBarrierResult ok(CommonTopicResponse data){
        return new PublishBarrierResult().setData(data);
    }


    boolean timeout = true;

    CommonTopicResponse data;

    private PublishBarrierResult() {
    }

    private PublishBarrierResult setData(CommonTopicResponse data) {
        this.data = data;
        this.timeout = false;
        return this;
    }

    public boolean isTimeout(){
        return timeout;
    }

    public CommonTopicResponse getData(){
        return data;
    }
}
