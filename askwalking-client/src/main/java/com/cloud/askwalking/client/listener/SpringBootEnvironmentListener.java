package com.cloud.askwalking.client.listener;

import com.cloud.askwalking.common.domain.SystemManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * SpringBoot 环境监听
 *
 * @author SHOUSHEN LUAN
 */
@Slf4j
public class SpringBootEnvironmentListener implements ApplicationListener<ApplicationPreparedEvent> {

    private static AtomicBoolean isInit = new AtomicBoolean(false);

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
        Environment environment = event.getApplicationContext().getEnvironment();
        if (isInit.compareAndSet(false, true)) {
            setManagerConfig(environment);
        }
    }

    private void setManagerConfig(Environment environment) {
        log.info("-----------------askwalking-client start --------------------");
        SystemManager.initEnvironment(environment);
        log.info("-----------------askwalking-client over--------------------");
    }

}
