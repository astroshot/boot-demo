package com.boot.common.rpc.http.proxy;

import com.boot.common.rpc.http.proxy.handler.ClientInvocationHandler;
import com.google.common.reflect.Reflection;
import org.springframework.core.env.Environment;


public abstract class ClientProxyFactory {

    public static <T> T createServiceProxy(Class<T> clientInterface, Environment env) {
        return Reflection.newProxy(clientInterface, new ClientInvocationHandler(env));
    }

}
