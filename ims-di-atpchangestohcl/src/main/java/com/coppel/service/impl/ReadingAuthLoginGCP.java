package com.coppel.service.impl;

import com.google.cloud.secretmanager.v1.*;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReadingAuthLoginGCP {
    @Value("${app.projectId}")
    private String projectId;
    @Value("${app.secretId}")
    private String secretId;

    public void authLogicsecretgcp()  {
        try(SecretManagerServiceClient client =  SecretManagerServiceClient.create()){
            ProjectName projectName = ProjectName.of(projectId);
            Secret secret = Secret.newBuilder().setReplication(
                    Replication.newBuilder().setAutomatic(Replication.Automatic.newBuilder().build()).build()).build();
            Secret createdSecret= client.createSecret(projectName,secretId,secret);
            SecretPayload payload = SecretPayload.newBuilder().setData(ByteString.copyFromUtf8("Hello World!")).build();
            SecretVersion addedVersion= client.addSecretVersion(createdSecret.getName(),payload);
            AccessSecretVersionResponse response = client.accessSecretVersion(addedVersion.getName());
            String data = response.getPayload().getData().toStringUtf8();
            log.info(data);
        }catch(Exception e){
            log.error("Error al consumir Secret");
        }

    }
}
