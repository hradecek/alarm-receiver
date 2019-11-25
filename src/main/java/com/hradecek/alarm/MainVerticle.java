package com.hradecek.alarm;

import io.reactivex.Completable;
import io.reactivex.Observable;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.AbstractVerticle;

import com.hradecek.alarm.persistence.PersistenceVerticle;
import com.hradecek.alarm.receiver.AlarmReceiverVerticle;
import com.hradecek.alarm.receiver.RabbitMqReceiverVerticle;
import com.hradecek.alarm.util.Deployment;

/**
 * Main verticle, bootstrap whole application.
 */
public class MainVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);

    private static final int RECEIVER_VERTICLE_INSTANCES = 2;

    @Override
    public Completable rxStart() {
        return verticles().flatMapCompletable(this::deploy);
    }

    private static Observable<Deployment> verticles() {
        return Observable.fromArray(
                Deployment.create(AlarmReceiverVerticle.class,
                                  new DeploymentOptions().setInstances(RECEIVER_VERTICLE_INSTANCES)),
                Deployment.create(PersistenceVerticle.class,
                                  new DeploymentOptions().setWorker(true).setInstances(8)),
                Deployment.create(RabbitMqReceiverVerticle.class));
//                Deployment.create(WebVerticle.class));
    }

    private Completable deploy(final Deployment deployment) {
        return vertx.rxDeployVerticle(deployment.getDeploymentName(), deployment.getDeploymentOptions())
                    .doOnSuccess(id -> LOGGER.info("Deployed: {} with ID: {}", deployment, id))
                    .ignoreElement();
    }
}
