/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.stuy.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 * @author Kevin Wang
 */
public class Autonomous extends CommandGroup {

    /* For inches to fender */
    private static final int DIST_FENDER_TO_KEY = 110;
    private static final double BUMPER_EDGE_TO_WHEEL_CENTER = 9.5;
    private static final int BOT_LENGTH_WITH_BUMPERS = 44;
    private static final double DIST_ALLIANCESTATION_TO_BRIDGE = 280.4;
    private static final double FENDER_DEPTH = 38.5;
    private static final double TOLERANCE = 2;
    public static final double INCHES_TO_FENDER = DIST_FENDER_TO_KEY - (BOT_LENGTH_WITH_BUMPERS - BUMPER_EDGE_TO_WHEEL_CENTER);

    /* For inches to bridge from fender */
    public static final double INCHES_TO_BRIDGE = DIST_ALLIANCESTATION_TO_BRIDGE - FENDER_DEPTH - BOT_LENGTH_WITH_BUMPERS -TOLERANCE;

    public static double FENDER_SPEED = 5;
    public static double FENDER_WIDE_SPEED = 5;
    public static double FENDER_NARROW_SPEED = 5;
    public static double SIDE_SPEED = 5;
    public static double KEY_SPEED = 5; // For auton only
    public static double MAX_DIST_SPEED = 5;

    public Autonomous() {
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.


    }
}
