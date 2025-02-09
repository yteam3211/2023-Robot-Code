// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.IntakeCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.CollectSubsystem;

public class CollectPosition extends CommandBase {
  private final CollectSubsystem collectSubsystem;
  private double collectPoint;
  /** Creates a new setPoitCollectCommand. */
  public CollectPosition(CollectSubsystem collectSubsystem, double collectPoint) {
    this.collectSubsystem = collectSubsystem;
    this.collectPoint = collectPoint;
    // this.armCollect = armCollect;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(collectSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // armSubsystem.setArmMidAndBase(middleArmPosition, baseArmPosition);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // SmartDashboard.putNumber("123456789", point);
    collectSubsystem.setPosition(collectPoint);
    // armSubsystem.setArmMidAndBase(middleArmPosition, baseArmPosition);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    collectSubsystem.setPosition(0);
    // new ArmCollectCommand(armCollectSubsystem, 6.11, 0.6).schedule();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
