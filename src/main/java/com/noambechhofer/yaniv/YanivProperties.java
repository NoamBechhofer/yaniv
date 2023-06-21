package com.noambechhofer.yaniv;

import java.util.logging.Level;

/**
 * Using this in place of a config for now
 */
public final class YanivProperties {
    /** The maximum hand value a player may hold and still be allowed to call Yaniv */
    public static final int YANIV_VALUE = 7;
    /** path to folder containing resources for yaniv */
    public static final String RESOURCES_PATH = "C:\\Users\\noamb\\OneDrive\\Programming\\yaniv\\src\\main\\resources\\";
    /** the number of cards players start each round with */
    public static final int NUM_STARTING_CARDS = 5;
    /** minimum number of players that can play */
    public static final int MIN_PLAYERS = 2;
    /** maximum number of players that can play */
    public static final int MAX_PLAYERS = 8;
    /** when a Player's score crosses this threshold, they lose */
    public static final int LOSING_THRESHOLD = 200;
    /**
     * The minimum number of consecutive cards that is counted as a straight.
     */
    public static final int MINIMUM_STRAIGHT_LENGTH = 3;
    /** The logging level. Raise to {@link Level#WARNING} when not testing. */
    public static final Level LOG_LEVEL = Level.INFO;
    /** penalty added to a player's score when they get assaf'ed */
    public static final int ASSAF_PENALTY = 30;
    private YanivProperties() {
        throw new IllegalStateException("Utility class");
    }
}
