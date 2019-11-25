package com.hradecek.alarm.receiver;

import java.time.LocalDateTime;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import com.hradecek.alarm.model.Alarm;
import com.hradecek.alarm.model.AlarmList;
import com.hradecek.alarm.model.RaisedAlarm;
import com.hradecek.alarm.reactivex.persistence.AlarmDataService;
import com.hradecek.alarm.reactivex.receiver.AlarmFilter;

/**
 * Implementation for {@link AlarmReceiverService} using in-memory alarm list.
 */
public class AlarmReceiverServiceImpl implements AlarmReceiverService {

    private final Logger LOGGER = LoggerFactory.getLogger(AlarmReceiverService.class);

    private final AlarmDataService alarmDataService;

    private final AlarmList alarmList = new AlarmList();
    private final AlarmFilter timeDuplicityFilter = new AlarmFilter(new TimeDuplicityAlarmFilter(1L));

    public AlarmReceiverServiceImpl(final AlarmDataService alarmDataService) {
        this.alarmDataService = alarmDataService;
    }

    @Override
    public AlarmReceiverService receiveAlarm(final Alarm alarm, Handler<AsyncResult<RaisedAlarm>> resultHandler) {
        LOGGER.debug("Received: {}", alarm.toJson());
        final var raisedAlarm = new RaisedAlarm(alarm, LocalDateTime.now());

        timeDuplicityFilter.rxFilter(raisedAlarm)
                           .doOnSuccess(this::insertToLog)
                           .map(alarmList::add)
                           .doOnSuccess(storedAlarm -> LOGGER.info("Raised: {}", storedAlarm.toJson()))
                           .subscribe(storedAlarm -> resultHandler.handle(Future.succeededFuture(storedAlarm)),
                                      ex -> resultHandler.handle(Future.failedFuture(ex)));
        return this;
    }

    private void insertToLog(final RaisedAlarm raisedAlarm) {
        alarmDataService.rxInsertIntoLog(raisedAlarm).ignoreElement().subscribe();
    }
}
