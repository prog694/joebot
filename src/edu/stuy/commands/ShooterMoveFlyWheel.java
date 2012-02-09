/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.stuy.commands;

import edu.stuy.subsystems.Shooter;

/**
 *
 * @author Kevin Wang
 */
public class ShooterMoveFlyWheel extends CommandBase {

    double distanceInches;
    
    public ShooterMoveFlyWheel(double distanceInches) {
        requires(shooter);
        setDistanceInches(distanceInches);
    }
    
    public void setDistanceInches(double distanceInches) {
        this.distanceInches = distanceInches;
    }

    // Called just before this Command runs the first time
    protected void initialize() {

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double[] rpm = shooter.lookupRPM(distanceInches);
        shooter.setFlywheelSpeeds(rpm[0], rpm[1]);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        shooter.setFlywheelSpeeds(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}