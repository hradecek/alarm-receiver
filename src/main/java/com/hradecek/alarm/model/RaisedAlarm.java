package com.hradecek.alarm.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject
public class RaisedAlarm extends Alarm {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public enum JsonKey {
        TIME("time");

        private final String key;

        JsonKey(final String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    private final LocalDateTime time;

    public RaisedAlarm(JsonObject raisedAlarmJson) {
        super(raisedAlarmJson.getString(Alarm.JsonKey.PRODUCER_ID.getKey()),
              raisedAlarmJson.getString(Alarm.JsonKey.RESOURCE_ID.getKey()),
              raisedAlarmJson.getString(Alarm.JsonKey.SEVERITY.getKey()));
        this.time = LocalDateTime.parse(raisedAlarmJson.getString(JsonKey.TIME.key), DATE_TIME_FORMATTER);
    }

    public RaisedAlarm(Alarm alarm, LocalDateTime localDateTime) {
        super(alarm);
        this.time = localDateTime;
    }

    public JsonObject toJson() {
        return super.toJson().put(JsonKey.TIME.getKey(), DATE_TIME_FORMATTER.format(time));
    }

    public LocalDateTime getTime() {
        return time;
    }
}
