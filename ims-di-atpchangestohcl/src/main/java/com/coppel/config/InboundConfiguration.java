package com.coppel.config;



import com.coppel.models.ApiModel;
import com.coppel.utils.Utilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.integration.AckMode;
import org.springframework.cloud.gcp.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import org.springframework.cloud.gcp.pubsub.support.BasicAcknowledgeablePubsubMessage;
import org.springframework.cloud.gcp.pubsub.support.GcpPubSubHeaders;
import org.springframework.context.annotation.Bean;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import com.coppel.service.MessageService;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
@RequiredArgsConstructor

public class InboundConfiguration {

    @Autowired
    private  ServiceConfig pubSubConfiguration;
    @Autowired
    private MessageService messageService;
    public int cont;

    @Bean
    public PubSubInboundChannelAdapter messageChannelAdapter(
            @Qualifier("pubsubInputChannel") MessageChannel inputChannel,
            PubSubTemplate pubSubTemplate) {
        PubSubInboundChannelAdapter adapter =
                new PubSubInboundChannelAdapter(pubSubTemplate, pubSubConfiguration.getSubscription());
        adapter.setOutputChannel(inputChannel);
        adapter.setAckMode(AckMode.MANUAL);
        return adapter;
    }
    @Bean
    public MessageChannel pubsubInputChannel() {
        return new DirectChannel();
    }
    //limitando a 2 las ejecuciones simultaneas para tener un seguimiento en el log... fines practicos despues se eliminara
    ExecutorService executorService = Executors.newFixedThreadPool(2);
    @Bean
    @ServiceActivator(inputChannel = "pubsubInputChannel")
    public MessageHandler messageReceiver(ApiModel apiModel) {
        return message -> executorService.submit(() -> {
        String json = new String((byte[]) message.getPayload());
        String jsonString = Utilities.removeBackslashesFromJson(json);
        log.info("Inicia Mensaje:" +jsonString);
            BasicAcknowledgeablePubsubMessage originalMessage =
                    message.getHeaders().get(GcpPubSubHeaders.ORIGINAL_MESSAGE,
                            BasicAcknowledgeablePubsubMessage.class);
            messageService.processMessage(jsonString, originalMessage, apiModel);
            //Con fines cientificos para ver duplicidad al tener mas de un servicio a un mismo suscriptor
            cont++;
            log.info("mensaje num: "+cont);
        });
    }


}
