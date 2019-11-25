package com.hradecek.alarm.model;

import java.util.LinkedList;
import java.util.List;

public class AlarmList {

    private final PredicatedLinkedListMap<UniqueId, RaisedAlarm> activeAlarms =
            new PredicatedLinkedListMap<>(alarm -> alarm.getSeverity() == AlarmSeverity.CLEARED);

    public RaisedAlarm add(final RaisedAlarm alarm) {
        return ((LinkedList<RaisedAlarm>) activeAlarms.push(new UniqueId(alarm.getProducerId(), alarm.getResourceId()), alarm)).getLast();
    }

    public boolean containsAlarm(final UniqueId uniqueId) {
        return activeAlarms.contains(uniqueId);
    }

    public List<RaisedAlarm> get(final UniqueId uniqueId) {
        return activeAlarms.get(uniqueId);
    }

    public int size() {
        return activeAlarms.size();
    }
}
