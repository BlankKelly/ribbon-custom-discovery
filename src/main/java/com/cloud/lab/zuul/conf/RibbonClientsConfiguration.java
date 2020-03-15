package com.cloud.lab.zuul.conf;

import com.cloud.lab.zuul.rule.MetadataBasedServiceGroupRule;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangkun
 */
@Configuration
public class RibbonClientsConfiguration {
    @Bean
    public IRule ribbonRule(
            @Autowired(required = false) IClientConfig config) {
        ZoneAvoidanceRule rule = new MetadataBasedServiceGroupRule();
        rule.initWithNiwsConfig(config);
        return rule;
    }
}
