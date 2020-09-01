package com.cloud.askwalking.client.starter;

import com.cloud.askwalking.client.listener.SpringBootEnvironmentListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author niuzhiwei
 */
@Slf4j
@Configuration
public class AskwalkingClientStarter {

    @Bean
    @ConditionalOnMissingBean(SpringBootEnvironmentListener.class)
    public SpringBootEnvironmentListener springBootAdminListener() {
        return new SpringBootEnvironmentListener();
    }
}
