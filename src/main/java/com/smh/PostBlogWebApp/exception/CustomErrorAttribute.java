package com.smh.PostBlogWebApp.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Component
public class CustomErrorAttribute extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String,Object> errorAttributesMap= super.getErrorAttributes(webRequest, options);
        errorAttributesMap.put("asd","asd");
        return errorAttributesMap;
    }

}
