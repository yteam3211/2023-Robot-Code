// // Copyright (c) FIRST and other WPILib contributors.
// // Open Source Software; you can modify and/or share it under the terms of
// // the WPILib BSD license file in the root directory of this project.

// package frc.robot.commands.NewArm;

// import edu.wpi.first.wpilibj2.command.CommandBase;
// import frc.robot.subsystems.ArmSubsystem;

// public class outputMiddle extends CommandBase {
//   ArmSubsystem armSubsystem;
//   public outputMiddle(ArmSubsystem armSubsystem) {
//     this.armSubsystem = armSubsystem;
//   }

//   /** Creates a new outputMiddle. */


//   // Called when the command is initially scheduled.
//   @Override
//   public void initialize() {}
  
//   // Called every time the scheduler runs while the command is scheduled.
//   @Override
//   public void execute() {
//     armSubsystem.setArmMidOutput(0.7);
//   }
  
//   // Called once the command ends or is interrupted.
//   @Override
//   public void end(boolean interrupted) {
//   armSubsystem.setArmMidOutput(0);
//   }

//   // Returns true when the command should end.
//   @Override
//   public boolean isFinished() {
//     return false;
//   }
// }
