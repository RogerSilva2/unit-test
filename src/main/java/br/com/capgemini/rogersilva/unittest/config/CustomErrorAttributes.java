package br.com.capgemini.rogersilva.unittest.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributesDefault = super.getErrorAttributes(webRequest, options);
        Map<String, Object> errorAttributes = new LinkedHashMap<>();

        errorAttributes.put("error", errorAttributesDefault.get("error").toString().toLowerCase().replace(" ", "_"));
        errorAttributes.put("error_description", errorAttributesDefault.get("message"));

        return errorAttributes;
    }
}