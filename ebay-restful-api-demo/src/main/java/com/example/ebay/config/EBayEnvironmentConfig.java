package com.example.ebay.config;

import com.ebay.api.client.auth.oauth2.model.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EBayEnvironmentConfig {

    @Value("${ebay.environment}")
    private String ebayEnvironment;

    @Bean(name = "ebayEnvironment")
    public Environment ebayEnvironment() {
        switch (ebayEnvironment) {
            case "SANDBOX":
                return Environment.SANDBOX;
            case "PRODUCTION":
            default:
                return Environment.PRODUCTION;
        }
    }

}
