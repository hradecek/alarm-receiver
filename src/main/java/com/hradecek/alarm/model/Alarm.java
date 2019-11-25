package com.hradecek.alarm.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject
public class Alarm {

    public enum JsonKey {
        PRODUCER_ID("producerId"),
        RESOURCE_ID("resourceId"),
        SEVERITY("severity");

        private final String key;

        JsonKey(final String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    private final String producerId;
    private final String resourceId;
    private final AlarmSeverity severity;

    public Alarm(final Alarm alarm) {
        this.producerId = alarm.producerId;
        this.resourceId = alarm.resourceId;
        this.severity = alarm.severity;
    }

    public Alarm(final JsonObject alarmJson) {
        this.producerId = alarmJson.getString(JsonKey.PRODUCER_ID.key);
        this.resourceId = alarmJson.getString(JsonKey.RESOURCE_ID.key);
        this.severity = AlarmSeverity.fromString(alarmJson.getString(JsonKey.SEVERITY.key));
    }

    public Alarm(final String producerId, final String resourceId, final String alarmSeverity) {
        this.producerId = producerId;
        this.resourceId = resourceId;
        this.severity = AlarmSeverity.fromString(alarmSeverity);
    }

    public JsonObject toJson() {
        return new JsonObject().put(Alarm.JsonKey.PRODUCER_ID.getKey(), getProducerId())
                .put(Alarm.JsonKey.RESOURCE_ID.getKey(), getResourceId())
                .put(Alarm.JsonKey.SEVERITY.getKey(), getSeverity().toString());
    }

    public String getProducerId() {
        return producerId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public AlarmSeverity getSeverity() {
        return severity;
    }
}
