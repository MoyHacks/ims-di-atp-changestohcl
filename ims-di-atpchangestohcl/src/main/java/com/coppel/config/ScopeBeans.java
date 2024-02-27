package com.coppel.config;

import com.coppel.logs.Logging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
public class ScopeBeans {

    @Bean
    @RequestScope
    Logging getLogging() {
        return new Logging();
    }

    @Bean
    ServiceConfig getServiceConfig()  {
        return new ServiceConfig();
    }
}
