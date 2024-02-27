package com.coppel.logs;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.tag.Tags;

import java.util.HashMap;
import java.util.Map;

public class SpansManager {
    public static final String INPUT = "input";
    public static final String OUTPUT = "output";
    public static final String WARN = "LEVEL.WARNING";
    public static final String STATE = "status.operacion.state";

    private final Span span;
    private final Scope scope;

    public SpansManager(TracerJeager tracer, String operationName) {
        this.span = tracer.getJaegertracer().buildSpan(operationName).start();
        this.scope = tracer.getJaegertracer().activateSpan(span);
    }

    public void finish() {
        scope.close();
        span.finish();
    }

    public void writeLog(String key, Object value) {
        Map<String, Object> log = new HashMap<>();
        log.put(key, value);
        span.log(log);
    }

    public void addTag(String key, Object value) {
        span.setTag(key, String.valueOf(value));
    }

    public void addError(Throwable throwable) {
        Tags.ERROR.set(span, true);
        Map<String, Object> log = new HashMap<>();
        log.put("event", "error");
        log.put("error.message", throwable.getMessage());
        log.put("error.stack", throwable.getStackTrace());
        span.log(log);
    }

}