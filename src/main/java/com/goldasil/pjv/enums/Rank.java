package com.goldasil.pjv.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Rank {

    SEVEN,
    EIGHT,
    NINE,
    TEN,
    UNDERKNAVE,
    OVERKNAVE,
    KING,
    ACE,
    UNSPECIFIED;

    /*@JsonValue
    public String getName() {
        return this.name();
    }*/

}
