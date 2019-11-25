package com.hradecek.alarm.persistence;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.reactivex.ext.sql.SQLClient;

import com.hradecek.alarm.model.RaisedAlarm;

/**
 * Provide interface for interacting with persistence layer for the alarms.
 */
@ProxyGen
@VertxGen
public interface AlarmDataService {

    @GenIgnore
    static com.hradecek.alarm.reactivex.persistence.AlarmDataService create(SQLClient sqlClient) {
        return new com.hradecek.alarm.reactivex.persistence.AlarmDataService(new AlarmSqlServiceImpl(sqlClient));
    }

    @GenIgnore
    static com.hradecek.alarm.reactivex.persistence.AlarmDataService createProxy(Vertx vertx, String address) {
        return new com.hradecek.alarm.reactivex.persistence.AlarmDataService(new AlarmDataServiceVertxEBProxy(vertx, address));
    }

    /**
     * Persist specified alarm.
     *
     * @param alarm alarm to be persisted
     * @param resultHandler ID of inserted alarm or failure
     * @return {@link AlarmDataService}
     */
    @Fluent
    AlarmDataService insertIntoLog(RaisedAlarm alarm, Handler<AsyncResult<Integer>> resultHandler);
}
