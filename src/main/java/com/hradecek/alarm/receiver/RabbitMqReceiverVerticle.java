package com.hradecek.alarm.receiver;

import io.reactivex.Completable;
import io.reactivex.Single;

import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.rabbitmq.RabbitMQOptions;
import io.vertx.rabbitmq.RabbitMQOptionsConverter;
import io.vertx.reactivex.config.ConfigRetriever;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.rabbitmq.RabbitMQClient;
import io.vertx.reactivex.rabbitmq.RabbitMQMessage;

import com.hradecek.alarm.model.Alarm;

/**
 * <p>RabbitMQ verticle.
 *
 * <p>Connects to RabbitMQ based on provided configuration and listen for incoming alarms at alarms queue.
 * All received alarms are passed to {@link AlarmReceiverService#receiveAlarm(Alarm, Handler)}.
 */
public class RabbitMqReceiverVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqReceiverVerticle.class);

    private static final String CONFIG_RABBIT_MQ_PATH = "src/main/conf/rabbitmq.json";
    private static final String RABBIT_MQ_ALARMS_QUEUE = "alarms";

    private com.hradecek.alarm.reactivex.receiver.AlarmReceiverService alarmReceiverService;

    @Override
    public Completable rxStart() {
        alarmReceiverService = AlarmReceiverService.createProxy(vertx.getDelegate(),
                                                                AlarmReceiverVerticle.ALARM_RECEIVER_SERVICE_QUEUE);

        return readConfig().map(RabbitMqReceiverVerticle::jsonToRabbitMQOptions)
                           .map(rabbitMQOptions -> RabbitMQClient.create(vertx, rabbitMQOptions))
                           .flatMapCompletable(this::startRabbitMQClient);

    }

    private Completable startRabbitMQClient(final RabbitMQClient client) {
        return client.rxStart()
                     .doOnComplete(() -> LOGGER.info("RabbitMQ client connected."))
                     .andThen(createAlarmsConsumer(client));
    }

    private Completable createAlarmsConsumer(final RabbitMQClient client) {
        return client.rxBasicConsumer(RABBIT_MQ_ALARMS_QUEUE)
                     .map(consumer -> consumer.handler(this::alarmsHandler))
                     .ignoreElement()
                     .doOnComplete(() -> LOGGER.info("RabbitMQ '{}' queue consumer create", RABBIT_MQ_ALARMS_QUEUE));
    }

    private void alarmsHandler(final RabbitMQMessage alarmMessage) {
        alarmReceiverService.rxReceiveAlarm(new Alarm(alarmMessage.body().toJsonObject()))
                            .subscribe();
//                            .subscribe(alarm -> vertx.eventBus().publish("alarms-sock", alarm.toJson()));
    }

    private static RabbitMQOptions jsonToRabbitMQOptions(final JsonObject jsonOptions) {
        var rabbitMQOptions = new RabbitMQOptions();
        RabbitMQOptionsConverter.fromJson(jsonOptions, rabbitMQOptions);

        return rabbitMQOptions;
    }

    private Single<JsonObject> readConfig() {
        final var configStoreOptions = new ConfigStoreOptions()
                .setType("file")
                .setConfig(new JsonObject().put("path", CONFIG_RABBIT_MQ_PATH));
        return ConfigRetriever.create(vertx, new ConfigRetrieverOptions().addStore(configStoreOptions))
                               .rxGetConfig()
                               .doOnSuccess(config -> LOGGER.info("RabbitMQ configuration read:\n{}", config.encodePrettily()));
    }
}
