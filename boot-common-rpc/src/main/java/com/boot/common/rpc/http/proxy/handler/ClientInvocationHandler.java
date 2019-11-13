package com.boot.common.rpc.http.proxy.handler;

import com.google.common.reflect.AbstractInvocationHandler;
import org.springframework.core.env.Environment;

import java.lang.reflect.Method;


/**
 * RPC invocation based on HTTP.
 * TODO: to be continued
 */
public class ClientInvocationHandler extends AbstractInvocationHandler {

    private Environment env;

    public ClientInvocationHandler(Environment env) {
        this.env = env;
    }

    @Override
    protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
