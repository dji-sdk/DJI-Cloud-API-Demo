package com.dji.sdk.config;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/16
 */
public class GetSnakeArgumentProcessor extends ServletModelAttributeMethodProcessor {

    /**
     * Class constructor.
     *
     * @param annotationNotRequired if "true", non-simple method arguments and
     *                              return values are considered model attributes with or without a
     *                              {@code @ModelAttribute} annotation
     */
    public GetSnakeArgumentProcessor(boolean annotationNotRequired) {
        super(annotationNotRequired);
    }

    @Override
    protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
        super.bindRequestParameters(new GetSnakeDataBinder(binder.getTarget(), binder.getObjectName()), request);
    }
}
