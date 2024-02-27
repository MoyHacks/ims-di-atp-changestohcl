package com.coppel.clients;


import com.coppel.components.ConsumeServiceRest;
import com.coppel.config.ServiceConfig;
import com.coppel.dto.inventory.InventoryRequest;
import com.coppel.dto.inventory.InventoryResponse;
import com.coppel.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;


@Component
@Slf4j
public class ApiRestClient {
    @Autowired
    private ServiceConfig conf;



    @Autowired
    private ConsumeServiceRest restService;

    // METODO QUE CONSUME HCL INVENTORY --Pendiente a definir
    @CircuitBreaker(name = Constants.RESILIENCE4J_INVENTORYAPI)
    @Retry(name = Constants.RESILIENCE4J_INVENTORYAPI)
    public InventoryResponse inventoryAPI(InventoryRequest jsonRequest) throws RestClientException, CallNotPermittedException {
        InventoryResponse response = new InventoryResponse();
        try {
            log.info("Request to InventoryHCL: " + jsonRequest.toJson());
            log.info("HCL url:" + this.conf.getInventoryUrl());
            this.restService.postRest(this.conf.getInventoryUrl(), jsonRequest.toJson(), this.conf.getTimeoutInventoryUrl(), MediaType.APPLICATION_JSON);
            JsonObject jsonResponse = this.restService.getJsonResponse();
            log.info("Response HCL Status:" + this.restService.getResponseCode() + " Response:" + this.restService.getResponseBody());
            JsonArray resultsArray = jsonResponse.getAsJsonArray("Results");
            if (resultsArray != null && resultsArray.size() > 0) {
                String jsonString = resultsArray.get(0).getAsString();
                Gson gson = new Gson();
                response = gson.fromJson(jsonString, InventoryResponse.class);

            } else {
                log.error("Error Response Empty");
            }
        } catch (Exception e) {
            log.error("Error" + e);
        }
        return response;
    }





}
