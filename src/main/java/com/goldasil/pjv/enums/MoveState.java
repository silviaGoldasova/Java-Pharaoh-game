package com.goldasil.pjv.enums;

public enum MoveState {

    NONSPECIAL_SITUATION,
    ACES_ONLY,
    ACES_PLAYED,
    SEVENS_PLAYED,
    UNDERKNAVE_LEAVES_PLAYED,

    SEVEN_HEARTS_RETURN_PLAYED,
    LOOKING_FOR_SEVEN_HEARTS_RETURN,

    OVERKNAVE,
    OVERKNAVE_HEARTS,
    OVERKNAVE_LEAVES,
    OVERKNAVE_BELLS,
    OVERKNAVE_ACORNS,

    HEARTS_PLAYED,
    LEAVES_PLAYED,
    BELLS_PLAYED,
    ACORNS_PLAYED,

    DRAW,
    DRAW_PENIALTY,
    SKIP;

    /*private static final Map<String, MoveStates> ENUM_MAP;

    static {
        Map<String, MoveState> map = new ConcurrentHashMap<String, MoveState>();
        for (MoveState instance : MoveStates.values()) {
            map.put(instance.getName(),instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    public static MyEnum get (String name) {
        return ENUM_MAP.get(name);
    }*/

    public static MoveState getMoveState(String state) {
        return valueOf(state);
    }

}
