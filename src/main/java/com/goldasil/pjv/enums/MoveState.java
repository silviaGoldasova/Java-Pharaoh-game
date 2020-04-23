package com.goldasil.pjv.enums;

/**
 * Contains all the possible states of the move in a game.
 *
 */
public enum MoveState {

    NONSPECIAL_SITUATION (0),
    ACES_ONLY (0),
    ACES_PLAYED (0),
    SEVENS_PLAYED (0),
    UNDERKNAVE_LEAVES_PLAYED (0),

    SEVEN_HEARTS_RETURN_PLAYED (0),
    LOOKING_FOR_SEVEN_HEARTS_RETURN (1),

    OVERKNAVE (0),
    OVERKNAVE_HEARTS (0),
    OVERKNAVE_LEAVES (0),
    OVERKNAVE_BELLS (0),
    OVERKNAVE_ACORNS (0),

    HEARTS_PLAYED (0),
    LEAVES_PLAYED (0),
    BELLS_PLAYED (0),
    ACORNS_PLAYED (0),

    WIN (0),
    DRAW (0),
    DRAW_PENIALTY (0),
    PASS (0),
    QUARTET_PLAYED (0);

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

    private int priority;

    MoveState() {
    }

    /**
     * Creates a move state the specified priority to take into account when processing a move in such a sstate.
     * @param priority
     */
    MoveState(int priority) {
        this.priority = priority;
    }

    /**
     * Gets the state in the Enum form based on the supplied String form.
     * @param state String form of the mvoe state
     * @return move state as an enum
     */
    public static MoveState getMoveState(String state) {
        return valueOf(state);
    }

    /**
     * Gets the priority of the enum
     * @return priority of the enum
     */
    public int getPriority() {
        return priority;
    }

}
