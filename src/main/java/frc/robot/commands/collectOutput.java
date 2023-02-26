// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.CollectSubsystem;
import frc.robot.subsystems.ShootingSubsystem;
import frc.robot.subsystems.collectWheels;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class collectOutput extends CommandBase {
  private final collectWheels collectWheels;
  private double WheelsOutput;
  private double centeringOutput;


  

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public 
  collectOutput(collectWheels collectWheels, double WheelsOutput, double centeringOutput) {
    this.collectWheels = collectWheels;
    this.WheelsOutput = WheelsOutput;
    this.centeringOutput = centeringOutput;


    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(collectWheels);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    collectWheels.WheelsSetOutput(WheelsOutput);
    collectWheels.centeringSetOutput(centeringOutput);

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    collectWheels.WheelsSetOutput(0);
    collectWheels.centeringSetOutput(0);
  }  

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    
    return false;  
    }
}

