package com.hradecek.alarm.persistence;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.ext.sql.SQLClient;

import com.hradecek.alarm.model.RaisedAlarm;

/**
 * Implementation of {@link AlarmDataService} using SQL-like persistence.
 */
public class AlarmSqlServiceImpl implements AlarmDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmSqlServiceImpl.class);

    private static final String INSERT_INTO_LOG = "INSERT INTO alarmlog(id, value) VALUES (?, ?)";

    private SQLClient sqlClient;

    /**
     * Constructor.
     *
     * @param sqlClient SQL client for underlying DB
     */
    public AlarmSqlServiceImpl(SQLClient sqlClient) {
        this.sqlClient = sqlClient;
    }

    @Override
    public AlarmDataService insertIntoLog(final RaisedAlarm alarm, Handler<AsyncResult<Integer>> resultHandler) {
        sqlClient.rxUpdateWithParams(INSERT_INTO_LOG, new JsonArray().add(1).add(alarm.toJson().toString()))
                 .doOnSuccess(result -> LOGGER.debug(String.format("Persisted: %s", alarm.toJson())))
                 .subscribe(resultSet -> resultHandler.handle(Future.succeededFuture(resultSet.getUpdated())),
                            ex -> resultHandler.handle(Future.failedFuture(ex)));
        return this;
    }
}
