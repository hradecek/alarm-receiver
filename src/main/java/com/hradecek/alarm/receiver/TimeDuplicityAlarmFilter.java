package com.hradecek.alarm.receiver;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;

import io.vertx.codegen.annotations.Nullable;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import com.hradecek.alarm.model.AlarmSeverity;
import com.hradecek.alarm.model.RaisedAlarm;
import com.hradecek.alarm.model.UniqueId;

/**
 * Filter alarms which has been raised multiple times in specified time range.
 */
public class TimeDuplicityAlarmFilter implements AlarmFilter {

    private final Logger LOGGER = LoggerFactory.getLogger(TimeDuplicityAlarmFilter.class);

    private final Map<UniqueId, LocalDateTime> updates = new ConcurrentHashMap<>();
    private final BiPredicate<LocalDateTime, LocalDateTime> isInDuplicationTime;

    public TimeDuplicityAlarmFilter(long duplicityCheckTime) {
        this.isInDuplicationTime = (currentUpdate, lastUpdate) ->
                currentUpdate.isBefore(lastUpdate.plusMinutes(duplicityCheckTime));
    }

    @Override
    public void filter(final RaisedAlarm alarm, Handler<AsyncResult<@Nullable RaisedAlarm>> resultHandler) {
        final var id = new UniqueId(alarm.getProducerId(), alarm.getResourceId());
        if (updates.containsKey(id)) {
            if (alarm.getSeverity() == AlarmSeverity.CLEARED) {
                updates.remove(id);
            } else {
                if (isInDuplicationTime.test(alarm.getTime(), updates.get(id))) {
                    LOGGER.debug("Duplicate alarm received: {}", alarm.toJson());
                    resultHandler.handle(Future.succeededFuture(null));
                }
            }
        } else {
            if (alarm.getSeverity() != AlarmSeverity.CLEARED) {
                updates.put(id, alarm.getTime());
            } else {
                LOGGER.warn("No matching active alarm found for: {}", alarm.toJson());
                resultHandler.handle(Future.succeededFuture(null));
            }
        }
        resultHandler.handle(Future.succeededFuture(alarm));
    }
}
