/*************************************************
 * @copyright 2017 Flision Corporation Inc.
 * @author: Vincent Chan @ Canton
 * @date: 2023年10月09日
 * @version: 1.0.0
 * @description:
 **************************************************/
package com.dji.sdk.mqtt;

public class FlowTransformWrapper {

    public final static String DEFAULT_ERROR_MSG = "null";

    public static FlowTransformWrapper error(){
        return new FlowTransformWrapper(DEFAULT_ERROR_MSG);
    }

    public static FlowTransformWrapper ok(CommonTopicRequest request){
        return new FlowTransformWrapper(request);
    }

    CommonTopicRequest request;
    boolean bError;
    String errorMessage;

    private FlowTransformWrapper(CommonTopicRequest request){
        this.request = request;
        this.bError = false;
    }

    private FlowTransformWrapper(String errorMessage){
        this.bError = true;
        this.errorMessage = errorMessage;
    }

    public CommonTopicRequest getRequest() {
        return request;
    }

    public boolean hasError() {
        return bError;
    }

    public boolean continuee(){
        return !hasError();
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
