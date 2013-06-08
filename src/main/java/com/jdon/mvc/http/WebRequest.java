package com.jdon.mvc.http;

import com.jdon.mvc.core.FrameWorkContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Enumeration;
import java.util.Map;

/**
 * 原生的request是不可变的，所以框架内部装饰一下
 */
public class WebRequest extends HttpServletRequestWrapper {

    public static final String TRIM_FORM = "trim?";

    private final RequestParameters parameters;

    private HttpServletRequest request;


    public WebRequest(HttpServletRequest request, FrameWorkContext fc) {
        super(request);
        this.request = request;
        this.parameters = new RequestParameters(request.getParameterMap());

        if (fc.getConfigItem(TRIM_FORM) != null && Boolean.valueOf(fc.getConfigItem(TRIM_FORM))) {
            parameters.trimAllParamValue();
        }
    }

    public void setParameter(String name, String value) {
        this.parameters.put(name, value);
    }

    public void addParameterValue(String name, String value) {
        parameters.addValueToArray(name, value);
    }

    public void addFormFile(String name, FormFile formFile) {
        parameters.addFormFile(name, formFile);
    }

    @Override
    public String getParameter(String name) {
        return parameters.getParameter(name);
    }

    @Override
    public Map getParameterMap() {
        return parameters.getParameterMap();
    }

    @Override
    public Enumeration getParameterNames() {
        return parameters.getParameterNames();
    }

    @Override
    public String[] getParameterValues(String name) {
        return parameters.getParameterValues(name);
    }

    public HttpServletRequest getRequest() {
        return request;
    }
}