package com.hradecek.alarm.util;

import io.reactivex.Completable;

import io.vertx.core.Vertx;
import io.vertx.serviceproxy.ServiceBinder;

public class ServiceUtils {

    private ServiceUtils() {
        throw new AssertionError("Cannot instantiate utility class.");
    }

    public static <T> Completable registerService(Vertx vertx, Class<T> klazz, T service, String address) {
        return Completable.fromRunnable(() -> new ServiceBinder(vertx).setAddress(address).register(klazz, service));
    }
}
