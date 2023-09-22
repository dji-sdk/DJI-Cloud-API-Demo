/*************************************************
 * @copyright 2017 Flision Corporation Inc.
 * @author: Vincent Chan @ Canton
 * @date: 2023年09月22日
 * @version: 1.0.0
 * @description:
 **************************************************/
package com.dji.sdk.mqtt;

import com.dji.sdk.common.PublishBarrier;
import com.dji.sdk.common.PublishBarrierResult;

import java.util.Objects;

public class ChanBarrierAdapter implements PublishBarrier {
    @Override
    public void put(String identity, CommonTopicResponse receiveData) {
        Chan instance = Chan.getInstance(identity, false);
        if(Objects.nonNull(instance)){
            instance.put(receiveData);
        }
    }

    @Override
    public void registerRequest(String identity, CommonTopicRequest requestData) {
        Chan.getInstance(identity, true);
    }

    @Override
    public PublishBarrierResult await(String identity,long timeout) {
        Chan instance = Chan.getInstance(identity, false);

        CommonTopicResponse response = instance.get(identity, timeout);

        return Objects.nonNull(response) ? PublishBarrierResult.ok(response) : PublishBarrierResult.EMPTY;
    }

    @Override
    public boolean hasIdentity(String identity) {
        Chan instance = Chan.getInstance(identity, false);
        return Objects.nonNull(instance);
    }
}
