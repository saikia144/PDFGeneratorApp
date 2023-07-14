package com.pdf.generator.enums;

public enum GoalType {
    total("total"),
    conceded("conceded"),
    assists("assists"),
    saves("saves");

    private final String value;

    GoalType(String value) {
        this.value = value;
    }

}
