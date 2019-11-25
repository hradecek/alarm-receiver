package com.hradecek.alarm.receiver;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

import com.hradecek.alarm.model.Alarm;
import com.hradecek.alarm.model.RaisedAlarm;
import com.hradecek.alarm.reactivex.persistence.AlarmDataService;

/**
 * Alarm receiver services provide interface for receiving alarm.
 */
@ProxyGen
@VertxGen
public interface AlarmReceiverService {

    @GenIgnore
    static com.hradecek.alarm.reactivex.receiver.AlarmReceiverService create(final AlarmDataService alarmDataService) {
        return new com.hradecek.alarm.reactivex.receiver.AlarmReceiverService(new AlarmReceiverServiceImpl(alarmDataService));
    }

    @GenIgnore
    static com.hradecek.alarm.reactivex.receiver.AlarmReceiverService createProxy(Vertx vertx, String address) {
        return new com.hradecek.alarm.reactivex.receiver.AlarmReceiverService(new AlarmReceiverServiceVertxEBProxy(vertx, address));
    }

    /**
     * Receive an alarm
     *
     * @param alarm received alarm
     * @param resultHandler void or failure
     * @return {@link AlarmReceiverService}
     */
    @Fluent
    AlarmReceiverService receiveAlarm(Alarm alarm, Handler<AsyncResult<RaisedAlarm>> resultHandler);
}
