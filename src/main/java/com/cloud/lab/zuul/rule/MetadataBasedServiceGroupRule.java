package com.cloud.lab.zuul.rule;

import com.cloud.lab.zuul.utils.RequestUtils;
import com.cloud.lab.zuul.constant.ZuulGatewayConstant;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 基于版本的ribbon负载均衡规则
 * </p>
 *
 * @author zhangkun
 */
public class MetadataBasedServiceGroupRule extends ZoneAvoidanceRule {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetadataBasedServiceGroupRule.class);

    @Override
    public Server choose(Object key) {
        String currentExpectServiceGroup = RequestUtils.getCurrentServiceGroupHeader();
        LOGGER.info("MetadataBasedServiceGroupRule choose key = {}", currentExpectServiceGroup);
        if (StringUtils.isBlank(currentExpectServiceGroup)) {
            return super.choose(key);
        }

        List<Server> eligibleServers = this.getPredicate().getEligibleServers(this.getLoadBalancer().getAllServers(), key);
        List<Server> versionMatchServers = Lists.newArrayList();
        List<Server> baseVersionServers = Lists.newArrayList();

        for (Server eligibleServer : eligibleServers) {
            Map<String, String> metadata = ((DiscoveryEnabledServer) eligibleServer).getInstanceInfo().getMetadata();

            String metaDataVersion = metadata.get(ZuulGatewayConstant.METADATA_SERVICE_GROUP_KEY);
            if (!StringUtils.isBlank(metaDataVersion) || !StringUtils.isBlank(currentExpectServiceGroup)) {
                if (metaDataVersion.equals(currentExpectServiceGroup)) {
                    versionMatchServers.add(eligibleServer);
                }
                if (metaDataVersion.equals(ZuulGatewayConstant.DEFAULT_STABLE_SERVICE_GROUP)) {
                    baseVersionServers.add(eligibleServer);
                }
            }
        }

        if (CollectionUtils.isNotEmpty(versionMatchServers)) {
            LOGGER.info("MetadataBasedServiceGroupRule found service group matched servers {} to choose", versionMatchServers);
            return expect(choose(super.getPredicate(), versionMatchServers, key));
        } else if (CollectionUtils.isNotEmpty(baseVersionServers)){
            LOGGER.info("MetadataBasedServiceGroupRule found stable service group servers {} to choose", versionMatchServers);
            return expect(choose(super.getPredicate(), versionMatchServers, key));
        } else {
            LOGGER.info("MetadataBasedServiceGroupRule default choose");
            return super.choose(key);
        }
    }

    protected Server choose(AbstractServerPredicate serverPredicate, List<Server> servers, Object key) {
        Optional<Server> server = serverPredicate.chooseRoundRobinAfterFiltering(servers, key);
        if (server.isPresent()) {
            return server.get();
        } else {
            return null;
        }
    }

    private Server expect(Server server) {
        if (server != null) {
            LOGGER.info("MetadataBasedServiceGroupRule server chosen: {}", server.getId());
        }
        return server;
    }
}
