package com.cloud.lab.zuul.utils;

import com.cloud.lab.zuul.constant.ZuulGatewayConstant;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * HTTP请求相关工具类
 * </p>
 *
 * @author zhangkun
 */
public class RequestUtils {
    public static String getCurrentRequestHeader(String headerName) {
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader(headerName);
    }

    public static String getCurrentServiceGroupHeader() {
        return getCurrentRequestHeader(ZuulGatewayConstant.SERVICE_GROUP_HEADER);
    }
}
