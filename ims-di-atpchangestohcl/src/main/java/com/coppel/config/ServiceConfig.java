package com.coppel.config;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Getter
@NoArgsConstructor
public class ServiceConfig {
    @Value("${app.hclInventoryUrl}")
    private String inventoryUrl;

    @Value("${app.storeLocationId}")
    private int storeLocationId;
    @Value("${app.timeOut}")
    private int timeoutInventoryUrl;

    @Value("${app.productsList}")
    private List<String> productsList;

    @Value("${app.authorization}")
    private String authorization;

    @Value("${app.topicName}")
    String topic;

    @Value("${app.subscriptionName}")
    public String subscription;

    @Value("${app.projectId}")
    private String projectId;

    @Value("${app.secretId}")
    private String secretId;

    @Value("${app.numThreads}")
    public int numthreads;

    @Value("${app.maxAttempt}")
    private  int indexLogsStackWalker ;
}
