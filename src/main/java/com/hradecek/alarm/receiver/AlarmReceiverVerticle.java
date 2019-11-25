package com.hradecek.alarm.receiver;

import io.reactivex.Completable;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.AbstractVerticle;

import com.hradecek.alarm.persistence.AlarmDataService;
import com.hradecek.alarm.persistence.PersistenceVerticle;
import com.hradecek.alarm.util.ServiceUtils;

/**
 * <p>Main alarm receiver verticle.
 *
 * <p>Creates and register all service for alarm receiving.
 */
public class AlarmReceiverVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmReceiverVerticle.class);

    public static final String ALARM_RECEIVER_SERVICE_QUEUE = "alarmreceiver.service.queue";

    @Override
    public Completable rxStart() {
        final var alarmReceiverService = AlarmReceiverService.create(
                AlarmDataService.createProxy(vertx.getDelegate(), PersistenceVerticle.ALARM_DATA_SERVICE_QUEUE));

        return ServiceUtils.registerService(vertx.getDelegate(),
                                            AlarmReceiverService.class,
                                            alarmReceiverService.getDelegate(),
                                            ALARM_RECEIVER_SERVICE_QUEUE)
                           .doOnComplete(() -> LOGGER.info("Service '{}' registered.", AlarmReceiverService.class.getCanonicalName()));
    }
}
