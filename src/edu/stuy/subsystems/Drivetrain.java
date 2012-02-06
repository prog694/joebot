/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.stuy.subsystems;

import edu.stuy.RobotMap;
import edu.stuy.commands.Autonomous;
import edu.stuy.commands.DriveManualJoystickControl;
import edu.stuy.util.VictorRobotDrive;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendablePIDController;


/**
 *
 * @author Kevin Wang
 */
public class Drivetrain extends Subsystem {
    private int direction;
    private double speed;
    public RobotDrive drive;
    public Solenoid gearShift;
    AnalogChannel sonar;
    public Encoder encoderLeft;
    public Encoder encoderRight;

    Gyro gyro;
    SendablePIDController controller;
    final int WHEEL_RADIUS = 3;
    final double CIRCUMFERENCE = 2 * Math.PI * WHEEL_RADIUS;
    final int ENCODER_CODES_PER_REV = 360;
    final double DISTANCE_PER_PULSE = CIRCUMFERENCE / ENCODER_CODES_PER_REV;
    double Kp = 0.035;
    double Ki = 0.0005;
    double Kd = 1.0;

    private double previousReading = -1.0;

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    public Drivetrain() {
        setForward();
        drive = new VictorRobotDrive(RobotMap.FRONT_LEFT_MOTOR, RobotMap.REAR_LEFT_MOTOR, RobotMap.FRONT_RIGHT_MOTOR, RobotMap.REAR_RIGHT_MOTOR);
        drive.setSafetyEnabled(false);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        
        encoderLeft = new Encoder(RobotMap.LEFT_ENCODER_CHANNEL_A, RobotMap.LEFT_ENCODER_CHANNEL_B, true);
        encoderRight = new Encoder(RobotMap.RIGHT_ENCODER_CHANNEL_A, RobotMap.RIGHT_ENCODER_CHANNEL_B, true);

        encoderLeft.start();
        encoderRight.start();

        encoderLeft.setDistancePerPulse(DISTANCE_PER_PULSE);
        encoderRight.setDistancePerPulse(DISTANCE_PER_PULSE);


        gyro = new Gyro(RobotMap.GYRO_CHANNEL);
        gyro.setSensitivity(0.007);

        controller = new SendablePIDController(Kp, Ki, Kd, gyro, new PIDOutput() {

            public void pidWrite(double output) {
                drive.arcadeDrive(profileSpeed(getSonarDistance_in()), -output); //TODO: Replace "1" with output from sonar sensor, in inches.
            }
        }, 0.005);

        gearShift = new Solenoid(RobotMap.GEAR_SHIFT);
        sonar = new AnalogChannel(RobotMap.SONAR_CHANNEL);
    }
    
    /**
     * Gets the analog voltage of the MaxBotics ultrasonic sensor, and debounces the input
     * @return Analog voltage reading from 0 to 5
     */
    public double getSonarVoltage () {
        double newReading = sonar.getVoltage ();
        double goodReading = previousReading;
        if (previousReading - (-1) < .001 || (newReading - previousReading) < .5){
            goodReading = newReading;
            previousReading = newReading;
        } else {
            previousReading = newReading;
        }
        return goodReading;
    }
    
    /**
     * Scales sonar voltage reading to inches
     * @return distance from alliance wall in in, as measured by sonar sensor
     */
    public double getSonarDistance_in() {
        return getSonarVoltage() * 512 / 5;
    }
            
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new DriveManualJoystickControl());
    }

    public Command getDefaultCommand(){
        return super.getDefaultCommand();
    }

    public void tankDrive(double leftValue, double rightValue) {
        drive.tankDrive(leftValue, rightValue);
        System.out.println(getSonarVoltage());
    }

    public void setGear(boolean high) {
        gearShift.set(high);
    }
    
    public boolean getGear() {
        return gearShift.get();
    }
    
    public void initController() {
        controller.setSetpoint(0);
        controller.enable();
    }
    
    public void endController() {
        controller.disable();
    }

    public void driveStraight() {
        controller.setSetpoint(0);  // Go straight
    }

    /**
     * Calculate average distance of the two encoders.  
     * @return Average of the distances (inches) read by each encoder since they were last reset.
     */
    public double getAvgDistance() {

        return (encoderLeft.getDistance() + encoderRight.getDistance()) / 2.0;

    }
    
    /**
     * Reset both encoders's tick, distance, etc. count to zero
     */
    public void resetEncoders() {
        encoderLeft.reset();
        encoderRight.reset();
    }

    
    /* Defines direction for autonomus as forwards */
    public final void setForward(){
        direction = -1;
    }

    /* Defines direction for autonomus as backwards */
    public final void setBackwards(){
        direction = 1;
    }

    // Updates speed relative to distance, the distance from the fender.
    public double profileSpeed(double sonarDistance) {
        double oldSpeed = speed;
        // If direction is forward, it is negative.
        if(direction < 0){
            // Distance at which ramping down occurs.
            if(sonarDistance - Autonomous.INCHES_FROM_EDGE_TO_SONAR < Autonomous.FENDER_DEPTH + Autonomous.RAMPING_DISTANCE){
                speed = oldSpeed / Autonomous.RAMPING_CONSTANT;
            }
            else if(oldSpeed < 1){
                speed = oldSpeed + 0.1;
            }
            if(speed < 0.1){
                speed = 0.1;
            }
        }
        // If direction is backward, it is positive.
        else if(direction > 0){
            if(Autonomous.INCHES_TO_BRIDGE - sonarDistance - Autonomous.INCHES_FROM_EDGE_TO_SONAR < Autonomous.RAMPING_DISTANCE){
                speed = oldSpeed / Autonomous.RAMPING_CONSTANT;
            }
            else if(oldSpeed < 1){
                speed = oldSpeed + 0.1;
            }
            if(speed < 0.1){
                speed = 0.1;
            }
        }
        return speed * direction;
    }
}
