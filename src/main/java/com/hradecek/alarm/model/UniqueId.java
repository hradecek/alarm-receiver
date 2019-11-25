package com.hradecek.alarm.model;

import java.util.Objects;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.Shareable;

// TODO fromJson, toJson
// Thread-safe
@DataObject
public class UniqueId implements Shareable {

    private final String producerId;
    private final String resourceId;

    public UniqueId(String producerId, String resourceId) {
        this.producerId = producerId;
        this.resourceId = resourceId;
    }

    public enum JsonKey {
        PRODUCER_ID("producerId"),
        RESOURCE_ID("resourceId");

        private final String key;

        JsonKey(final String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    public UniqueId(final JsonObject uniqueIdJson) {
        this.producerId = uniqueIdJson.getString(JsonKey.PRODUCER_ID.key);
        this.resourceId = uniqueIdJson.getString(JsonKey.RESOURCE_ID.key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UniqueId uniqueId = (UniqueId) o;
        return Objects.equals(producerId, uniqueId.producerId) && Objects.equals(resourceId, uniqueId.resourceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(producerId, resourceId);
    }

    @Override
    public String toString() {
        return producerId + ":" + resourceId;
    }
}
