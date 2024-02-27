package com.coppel.logs;


import com.coppel.dto.logs.FactoryJeager;
import io.jaegertracing.Configuration.ReporterConfiguration;
import io.jaegertracing.Configuration.SamplerConfiguration;
import io.jaegertracing.Configuration.SenderConfiguration;
import io.jaegertracing.internal.JaegerTracer;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.jaegertracing.Configuration;

import lombok.Getter;

@Getter
public class TracerJeager {
    private JaegerTracer jaegertracer;
    private boolean flagJaeger;

    public void initJeager(FactoryJeager factoryjeager, String appName) {
        this.flagJaeger = factoryjeager.isFlagJaeger();

        if (factoryjeager.isFlagJaeger()) {
            SenderConfiguration senderConfig = SenderConfiguration.fromEnv().withAgentHost(factoryjeager.getJeagerAgentHost());
            SamplerConfiguration samplerConfig = SamplerConfiguration.fromEnv().withType(ConstSampler.TYPE).withParam(1.0);
            ReporterConfiguration reporterConfig = ReporterConfiguration.fromEnv().withLogSpans(factoryjeager.isFlagJeagerLogsSpan()).withSender(senderConfig);
            this.jaegertracer = new Configuration(appName).withSampler(samplerConfig).withReporter(reporterConfig).getTracer();
        }
    }

}