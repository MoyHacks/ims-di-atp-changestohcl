package com.coppel.models;

import com.coppel.clients.ApiRestClient;
import com.coppel.components.ConsumeServiceRest;
import com.coppel.config.ServiceConfig;
import com.coppel.dto.by.*;
import com.coppel.dto.inventory.InventoryRequest;
import com.coppel.dto.inventory.Item;

import com.coppel.dto.inventory.InventoryResponse;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
public class ApiModel {

    @Autowired
    private ServiceConfig conf;

    @Autowired
    private ApiRestClient apiRestClient;

    @Autowired
    ConsumeServiceRest restService;
    public InventoryRequest createInventoryRequest(Request records) {
        InventoryRequest request = new InventoryRequest();
        AvailabilityByProduct product = records.getRecords().get(0).getValue().getEntity().getAvailabilityByProducts().get(0);
        AvailabilityDetails details = product.getAvailabilityByFulfillmentTypes().get(0).getAvailabilityDetails().get(0);
        if(this.conf.getProductsList().get(0).isEmpty() || this.conf.getProductsList().contains(product.getProductId()))
        {
            log.debug("ProductId Valido: "+product.getProductId()+" en la lista:"+this.conf.getProductsList());
            Item jsonRequest = new Item();
            jsonRequest.setPartNumber(this.setPartNumber(product.getProductId()));
            jsonRequest.setAvailableQuantity( details.getAtp());
            jsonRequest.setStoreLocationId(this.conf.getStoreLocationId());
            List<Item> items = new ArrayList<>();
            items.add(jsonRequest);
            request.setItems(items);
        }
        return request;
    }

    public InventoryResponse sendtoInventoryHCL(Request records)throws RestClientException, CallNotPermittedException {
        InventoryResponse response = null;
        try{
         InventoryRequest jsonRequest = this.createInventoryRequest(records);
        if(jsonRequest.getItems() != null)
            response = this.apiRestClient.inventoryAPI(jsonRequest);
        }catch (Exception e){
            log.error("Error en Response: "+e);
        }
        return response;
    }

    public String setPartNumber(String id) {
        String talla = id.substring(id.length() - 3);
        talla = talla.replaceAll("^0+(?!$)", "");
        return "IR-" + id.substring(0, id.length() - 3) + "2-" + (talla.isEmpty() ? "0" : talla);
    }
}
