package com.coppel.service;

import com.coppel.models.ApiModel;
import org.springframework.cloud.gcp.pubsub.support.BasicAcknowledgeablePubsubMessage;

public interface MessageService {
     void processMessage(String jsonString, BasicAcknowledgeablePubsubMessage originalMessage, ApiModel apiModel);
}
