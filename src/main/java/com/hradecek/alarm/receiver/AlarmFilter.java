package com.hradecek.alarm.receiver;

import io.vertx.codegen.annotations.Nullable;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import com.hradecek.alarm.model.RaisedAlarm;

/**
 * Filter out alarm based on specified conditions or pass through.
 */
@VertxGen
@FunctionalInterface
public interface AlarmFilter {

    void filter(final RaisedAlarm value, Handler<AsyncResult<@Nullable RaisedAlarm>> resultHandler);
}
