// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.dashboard.SuperSubSystemTab;
import frc.robot.dashboard.SuperSystem;
import frc.util.PID.Gains;
import frc.util.motor.SuperTalonSRX;

public class ShootingSubsystem extends SuperSystem {
  private SuperTalonSRX leftShootingWheels;
  private SuperTalonSRX rightShootingWheels;
  private Gains shootingGains = new Gains("shooting gains", 0.25, 0, 0);
  /** Creates a new shootingSubsystem. */
  public ShootingSubsystem() {
    super("shooting Subsystem");
    rightShootingWheels = new SuperTalonSRX(Constants.RIGHT_SHOOTING_M0TOR, 40, false, false, 0, 1, 1, shootingGains, ControlMode.Velocity);
    leftShootingWheels = new SuperTalonSRX(Constants.LEFT_SHOOTIN_MOTOR, 40, false, false, 0, 1, 1, shootingGains, ControlMode.Velocity);
    // setDefaultCommand();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    // System.out.println(rightShootingWheels.getVelocity());
    getTab().putInDashboard("Right motor velocity", rightShootingWheels.getVelocity(), false);
    getTab().putInDashboard("Left motor velocity", leftShootingWheels.getVelocity(), false);
    getTab().putInDashboard("Right motor Output", rightShootingWheels.getOutput(), false);
    getTab().putInDashboard("Left motor Output", leftShootingWheels.getOutput(), false);
    getTab().putInDashboard("Right motor Amper", rightShootingWheels.getOutput(), false);
    getTab().putInDashboard("Left motor Amper", leftShootingWheels.getOutput(), false);
  }
  
  public void setVelocity(double Velocity){
    rightShootingWheels.set(ControlMode.Velocity, Velocity * -1);
    leftShootingWheels.set(ControlMode.Velocity, Velocity);
    // System.out.println("left velocity: " + leftShootingWheels.getVelocity());
  }

  public void setShootingOutput(double ShootingOutput){

    rightShootingWheels.set(ControlMode.PercentOutput, ShootingOutput * -1);
    leftShootingWheels.set(ControlMode.PercentOutput, ShootingOutput);
  
  }
  public double GetRightShootingWheelsVelocity(){
    return rightShootingWheels.getVelocity();
    } 

  public double getLeftShootingWheelsVelocity(){
    return leftShootingWheels.getVelocity();

  }
  public double GetRightShootingWheelsOutput(){
    return rightShootingWheels.getOutput();
    } 

  public double getLeftShootingWheelsOutput(){
    return leftShootingWheels.getOutput();

  }



  public void resetEncoder(){
    rightShootingWheels.reset(0);
    leftShootingWheels.reset(0);
  }


}
