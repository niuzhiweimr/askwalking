package com.cloud.askwalking.gateway.helper;

import com.cloud.askwalking.core.HelperService;
import com.cloud.askwalking.gateway.configuration.GatewayServiceDiscovery;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author niuzhiwei
 */
@Slf4j
@Service
public class HelperServiceImpl implements HelperService {

    @Autowired
    private GatewayServiceDiscovery gatewayServiceDiscovery;

    @Override
    public void addMethodDefinitionHook(String uri) {
        if (log.isDebugEnabled()) {
            log.debug("HelperService#addMethodDefinitionHook({})", uri);
        }
        gatewayServiceDiscovery.addMethodDefinitionHook(uri);
    }

    @Override
    public void updateMethodDefinitionHook(String uri) {
        if (log.isDebugEnabled()) {
            log.debug("HelperService#updateMethodDefinitionHook({})", uri);
        }
        gatewayServiceDiscovery.updateMethodDefinitionHook(uri);
    }

    @Override
    public void updateMetadataMethodDefinitionHook(String uri) {
        if (log.isDebugEnabled()) {
            log.debug("HelperService#updateMetadataMethodDefinitionHook({})", uri);
        }
        gatewayServiceDiscovery.updateMetadataMethodDefinitionHook(uri);
    }
}
