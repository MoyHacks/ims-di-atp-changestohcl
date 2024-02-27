package com.coppel.logs;

import java.lang.StackWalker.Option;
import java.lang.StackWalker.StackFrame;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import com.coppel.config.ServiceConfig;
import com.coppel.utils.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import jakarta.annotation.Resource;

import static com.coppel.logs.SpansManager.INPUT;
import static com.coppel.logs.SpansManager.OUTPUT;


public class Logging {
    private TracerJeager tracer;

    @Resource(name = "getServiceConfig")
    private ServiceConfig conf;

    private static final String TAG_CLASS_METHOD_VALUE_LOG = "%s: %s";
    private static final String TAG_CLASS_METHOD_KEY_VALUE_LOG = "%s: %s -> %s";

    private Deque<Logger> consoleDeque = new ArrayDeque<>();
    private Deque<SpansManager> spanManagerDeque = new ArrayDeque<>();

    public void init(String operation, Object... inputParams) {
        StackFrame stackFrame = getStackFrameWalker();
        boolean input = inputParams.length > 0;
        initLogging(stackFrame.getClassName(), stackFrame.getMethodName(), input, operation, inputParams);
    }

    private StackFrame getStackFrameWalker() {
        return StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE).walk(frames -> frames.filter(value ->
                value.getClassName().contains("com.coppel")).toList()).get(this.conf.getIndexLogsStackWalker());
    }

    public void addTag(String key, Object value) {
        String str = String.format(TAG_CLASS_METHOD_KEY_VALUE_LOG, this.consoleDeque.getLast().getName(), key, value);
        this.consoleDeque.getLast().debug(str);

        if (this.tracer.isFlagJaeger()) {
            this.spanManagerDeque.getLast().addTag(key, String.valueOf(value));
        }
    }

    public void info(Object value) {
        String str = Utilities.unescapeCharacters(String.format(TAG_CLASS_METHOD_VALUE_LOG, this.consoleDeque.getLast().getName(), value));
        this.consoleDeque.getLast().info(str);

        if (this.tracer.isFlagJaeger()) {
            this.spanManagerDeque.getLast().writeLog(SpansManager.STATE, value);
        }
    }

    public void debug(String key, Object value) {
        String str = Utilities.unescapeCharacters(String.format(TAG_CLASS_METHOD_KEY_VALUE_LOG, this.consoleDeque.getLast().getName(), key, value));
        this.consoleDeque.getLast().debug(str);

        if (this.tracer.isFlagJaeger()) {
            this.spanManagerDeque.getLast().writeLog(key, value);
        }
    }

    public void warn(Object value) {
        String str = Utilities.unescapeCharacters(String.format(TAG_CLASS_METHOD_VALUE_LOG, this.consoleDeque.getLast().getName(), value));
        this.consoleDeque.getLast().warn(str);

        if (this.tracer.isFlagJaeger()) {
            this.spanManagerDeque.getLast().writeLog(SpansManager.WARN, value);
        }
    }

    public void error(Object value, Throwable throwable) {
        String str = Utilities.unescapeCharacters(String.format(TAG_CLASS_METHOD_KEY_VALUE_LOG, this.consoleDeque.getLast().getName(), value, throwable.getMessage()));
        this.consoleDeque.getLast().error(str);

        if (this.tracer.isFlagJaeger()) {
            this.spanManagerDeque.getLast().addError(throwable);
        }
    }

    public void finish(Object response) {
        finishLog4j(response);
        finishJeager(response);
    }

    public void finish() {
        finishLog4j(null);
        finishJeager(null);
    }

    private void initLogging(String className, String methodName, boolean input, String operation, Object... paramsRequest) {
        String request = input ? Arrays.toString(paramsRequest) : "{}";
        String classMethod = createClassMethodLogging(className, methodName);

        this.consoleDeque.add(LoggerFactory.getLogger(classMethod));

        if (this.tracer.isFlagJaeger()) {
            this.spanManagerDeque.add(new SpansManager(this.tracer, classMethod));
        }

        info(String.format("Inicia - %s", operation));

        if (input) {
            debug(INPUT, request);
        }
    }

    private String createClassMethodLogging(String className, String methodName) {
        return String.format("%s - %s", className, methodName);
    }

    private void finishLog4j(Object response) {
        if(!this.consoleDeque.isEmpty()) {
            if (response != null) {
                String str = String.format(TAG_CLASS_METHOD_VALUE_LOG, this.consoleDeque.getLast().getName(), response);
                this.consoleDeque.getLast().debug(str);
            }

            String str = String.format("%s: Finaliza", this.consoleDeque.getLast().getName());
            this.consoleDeque.getLast().info(str);
            this.consoleDeque.removeLast();
        }
    }

    private void finishJeager(Object response) {
        if(this.tracer.isFlagJaeger() && !this.spanManagerDeque.isEmpty()) {
            if (response != null) {
                this.spanManagerDeque.getLast().writeLog(OUTPUT, response);
            }

            this.spanManagerDeque.getLast().writeLog(SpansManager.STATE, "Finaliza");
            this.spanManagerDeque.getLast().finish();
            this.spanManagerDeque.removeLast();
        }
    }

}
