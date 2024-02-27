package com.coppel.components;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import lombok.Getter;

@Getter
@Component
public class ConsumeServiceRest {
    private int responseCode;
    private String responseBody;

    public void getRest(String urlService, int timeoutInMillis,  MediaType mediatype) {
        sendRequestGet(urlService, timeoutInMillis, mediatype, new HttpHeaders());
    }

    public void getRest(String urlService, int timeoutInMillis,  MediaType mediatype, HttpHeaders headers) {
        sendRequestGet(urlService, timeoutInMillis, mediatype, headers);
    }

    public void postRest(String urlService, String parametros, int timeoutInMillis, MediaType mediatype, HttpHeaders headers) {
        sendRequestPost(urlService, parametros, timeoutInMillis, mediatype, headers);
    }

    public void postRest(String urlService, String parametros, int timeoutInMillis, MediaType mediatype) {
        sendRequestPost(urlService, parametros, timeoutInMillis, mediatype, new HttpHeaders());
    }

    public JsonObject getJsonResponse() {
        return new Gson().fromJson(this.responseBody, JsonObject.class);
    }

    private void sendRequestGet(String urlService, int timeoutInMillis, MediaType mediatype, HttpHeaders headers) {
        getHeaders(mediatype, headers);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = getRestTemplate(timeoutInMillis);
        ResponseEntity<String> response = restTemplate.getForEntity(urlService, String.class, entity);
        processResponseJson(response);
    }

    private void sendRequestPost(String urlService, String parametros, int timeoutInMillis, MediaType mediatype, HttpHeaders headers) {
        getHeaders(mediatype, headers);
        HttpEntity<String> entity = new HttpEntity<>(parametros, headers);
        RestTemplate restTemplate = getRestTemplate(timeoutInMillis);
        ResponseEntity<String> response = restTemplate.postForEntity(urlService, entity, String.class);
        processResponseJson(response);
    }

    private void getHeaders(MediaType mediatype, HttpHeaders headers) {
        if(mediatype == MediaType.APPLICATION_JSON) {
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        } else if(mediatype == MediaType.TEXT_XML) {
            headers.setContentType(MediaType.TEXT_XML);
            headers.setAccept(Collections.singletonList(MediaType.TEXT_XML));
        }
    }

    private RestTemplate getRestTemplate(int timeoutInMillis) {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(timeoutInMillis);
        httpRequestFactory.setConnectionRequestTimeout(timeoutInMillis);

        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }

    private void processResponseJson(ResponseEntity<String> response) {
        this.responseCode = response.getStatusCode().value();
        this.responseBody = response.getBody();
    }

}