package com.hradecek.alarm;

import co.paralleluniverse.fibers.Suspendable;

import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.sync.Sync;
import io.vertx.ext.sync.SyncVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.sockjs.BridgeEvent;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;
import io.vertx.core.AbstractVerticle;

import com.hradecek.alarm.receiver.AlarmReceiverVerticle;

public class WebVerticle extends AbstractVerticle {// extends SyncVerticle {

    //    @Suspendable
    @Override
    public void start() {
        final var httpServer = vertx.createHttpServer();
        final var router = Router.router(vertx);

        router.route("/eventbus/*").handler(createSocksJS());

        httpServer.requestHandler(router::accept).listen(8888);
    }

    private SockJSHandler createSocksJS() {
        final var sockJSHandler = SockJSHandler.create(vertx, new SockJSHandlerOptions().setHeartbeatInterval(5000));
        final var bridgeOptions = new BridgeOptions().addOutboundPermitted(new PermittedOptions().setAddress("alarms-sock"));
//        sockJSHandler.bridge(bridgeOptions, Sync.fiberHandler(this::handleEBRequests));
        sockJSHandler.bridge(bridgeOptions);

        return sockJSHandler;
    }
}
