package com.cloud.lab.zuul.constant;

/**
 * <p>
 * 常量
 * </p>
 *
 * @author zhangkun
 */
public class ZuulGatewayConstant {

    /**
     * 实例版本元信息key
     */
    public static final String METADATA_SERVICE_GROUP_KEY = "service.group";

    /**
     * 正常版本默认版本号
     */
    public static final String DEFAULT_STABLE_SERVICE_GROUP = "stable";

    /**
     * 承载期望消费实例版本号的http header key
     */
    public static final String SERVICE_GROUP_HEADER = "X-Service-Group";

}
