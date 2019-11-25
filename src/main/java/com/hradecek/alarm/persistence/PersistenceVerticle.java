package com.hradecek.alarm.persistence;

import io.reactivex.Completable;
import io.reactivex.Single;

import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.config.ConfigRetriever;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.asyncsql.PostgreSQLClient;
import io.vertx.reactivex.ext.sql.SQLClient;

import com.hradecek.alarm.util.ServiceUtils;

/**
 * <p>Main persistence verticle.
 *
 * <p>Loads all necessary configuration and registers all persistence related verticles.
 */
public class PersistenceVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersistenceVerticle.class);

    public static final String ALARM_DATA_SERVICE_QUEUE = "alarmdata.service.queue";

    private static final String CONFIG_POSTGRES_PATH = "src/main/conf/postgres.json";

    @Override
    public Completable rxStart() {
        return readConfig().map(config -> PostgreSQLClient.createShared(vertx, config))
                           .flatMapCompletable(this::registerService);
    }

    private Completable registerService(final SQLClient sqlClient) {
        return ServiceUtils.registerService(
                vertx.getDelegate(),
                AlarmDataService.class,
                AlarmDataService.create(sqlClient).getDelegate(),
                ALARM_DATA_SERVICE_QUEUE);
    }

    private Single<JsonObject> readConfig() {
        final ConfigStoreOptions configStoreOptions = new ConfigStoreOptions()
                .setType("file")
                .setConfig(new JsonObject().put("path", CONFIG_POSTGRES_PATH));
        return ConfigRetriever.create(vertx, new ConfigRetrieverOptions().addStore(configStoreOptions))
                              .rxGetConfig()
                              .doOnSuccess(config -> LOGGER.info("PostgresSQL configuration read:\n{}", config.encodePrettily()));
    }
}
