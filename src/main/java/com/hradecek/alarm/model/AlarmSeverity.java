package com.hradecek.alarm.model;

// TODO: Just for testing purposes
// TODO: Can be @DataObject?
public enum AlarmSeverity {
    CRITICAL {
        @Override
        public String toString() {
            return "critical";
        }
    },

    CLEARED {
        @Override
        public String toString() {
            return "cleared";
        }
    };

    public static AlarmSeverity fromString(String severity) {
        return severity.equals("cleared") ? CLEARED : CRITICAL;
    }
}
