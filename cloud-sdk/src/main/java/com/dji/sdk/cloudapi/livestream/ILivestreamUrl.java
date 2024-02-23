package com.dji.sdk.cloudapi.livestream;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/13
 */
public interface ILivestreamUrl {

    @JsonValue
    String toString();

    ILivestreamUrl clone();
}
