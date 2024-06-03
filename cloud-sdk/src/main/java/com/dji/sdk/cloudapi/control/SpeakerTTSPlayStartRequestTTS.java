/*************************************************
 * @copyright 2017 Flision Corporation Inc.
 * @author: Vincent Chan @ Canton
 * @date: 2024年06月03日
 * @version: 1.0.0
 * @description:
 **************************************************/
package com.dji.sdk.cloudapi.control;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

import java.beans.Encoder;

public class SpeakerTTSPlayStartRequestTTS {
    String md5;

    String name;

    String text;

    public String getMd5() {
        return md5;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.md5 = Hashing.md5().newHasher().putString(text, Charsets.UTF_8)
          .hash().toString();
    }
}
