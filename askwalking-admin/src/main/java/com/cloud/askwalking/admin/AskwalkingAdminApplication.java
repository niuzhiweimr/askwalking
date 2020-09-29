package com.cloud.askwalking.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author niuzhiwei
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.cloud.askwalking.*"})
public class AskwalkingAdminApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AskwalkingAdminApplication.class);
        app.run(args);
    }

}
