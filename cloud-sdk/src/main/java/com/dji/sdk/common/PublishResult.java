/*************************************************
 * @copyright 2017 Flision Corporation Inc.
 * @author: Vincent Chan @ Canton
 * @date: 2023年09月25日
 * @version: 1.0.0
 * @description:
 **************************************************/
package com.dji.sdk.common;

import com.dji.sdk.mqtt.CommonTopicResponse;

public interface PublishResult {

    boolean isTimeout();

    CommonTopicResponse getData();
}
