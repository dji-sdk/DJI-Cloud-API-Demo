/*************************************************
 * @copyright 2017 Flision Corporation Inc.
 * @author: Vincent Chan @ Canton
 * @date: 2023年09月22日
 * @version: 1.0.0
 * @description:
 **************************************************/
package com.dji.sdk.common;

import com.dji.sdk.mqtt.CommonTopicRequest;
import com.dji.sdk.mqtt.CommonTopicResponse;

public interface PublishBarrier {

    //构建栅栏标识方法
    String generateIdentity(CommonTopicRequest requestData);
    String generateIdentity(CommonTopicResponse receiveData);

    void put(String identity, CommonTopicResponse receiveData);

    void registerRequest(String identity, CommonTopicRequest requestData);

    PublishBarrierResult await(String identity,long timeout);

    boolean hasIdentity(String identity);
}
