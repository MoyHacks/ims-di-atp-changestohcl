package com.coppel.service.impl;

import com.coppel.dto.by.Request;
import com.coppel.dto.inventory.InventoryResponse;
import com.coppel.models.ApiModel;
import com.coppel.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gcp.pubsub.support.BasicAcknowledgeablePubsubMessage;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {
    @Override
    public void processMessage(String jsonString, BasicAcknowledgeablePubsubMessage originalMessage, ApiModel apiModel) {
        //Procesamiento del mensaje
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Request records = objectMapper.readValue(jsonString, Request.class);
            InventoryResponse data = apiModel.sendtoInventoryHCL(records);
            if ((data.getStatus()).equals("success")) {
                log.info("Success Data Confirmed,num: ");
                originalMessage.ack();
            } else {
                log.info("Producto no Aceptado");
                originalMessage.nack();
            }
        } catch (IOException e) {
            log.error("Error en la petición:" + e);
        }
    }
}
