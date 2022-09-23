package com.dji.sample.configuration.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/16
 */
public class GetSnakeArgumentProcessor extends ServletModelAttributeMethodProcessor {

    @Autowired
    private GetSnakeDataBinder snakeDataBinder;

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
