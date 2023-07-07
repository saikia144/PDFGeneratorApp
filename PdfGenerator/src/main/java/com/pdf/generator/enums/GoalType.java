package com.pdf.generator.enums;

public enum GoalType {
    TOTAL("total"),
    CONCEDED("conceded"),
    ASSISTS("assists"),
    SAVES("saves");

    private final String value;

    GoalType(String value) {
        this.value = value;
    }

    public static GoalType fromString(String value) {
        for (GoalType goalType : GoalType.values()) {
            if (goalType.value.equalsIgnoreCase(value)) {
                return goalType;
            }
        }
        return null;
    }
}
