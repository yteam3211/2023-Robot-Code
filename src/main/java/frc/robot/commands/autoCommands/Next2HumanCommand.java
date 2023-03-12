// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.autoCommands;

import frc.robot.autos.AutoCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotButtons;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.armCollectSubsystem;
import frc.robot.commands.timercommand.timeSetPointCollectCommand;
import frc.robot.subsystems.CollectSubsystem;
import frc.robot.subsystems.CartridgeSubsystem;
import frc.robot.subsystems.armSubsystem;
import frc.robot.subsystems.collectWheels;
import frc.robot.subsystems.shootingSubsystem;
import frc.util.vision.Limelight;
import frc.robot.commands.ArmCollectCommand;
import frc.robot.commands.BalanceCommand;
import frc.robot.commands.LimelightCommand;
import frc.robot.commands.ShootingCommand;
import frc.robot.commands.ShootingGroupCommand;
import frc.robot.commands.StartAuto;
import frc.robot.commands.TurnToZeroCommand;
import frc.robot.commands.armCollectOutput;
// import frc.robot.commands.ClosingCollectGroupCommand;
import frc.robot.commands.resetCommand;
import frc.robot.commands.shootingOutputCommand;
import frc.robot.commands.timercommand.TimerArmPosition;
import frc.robot.commands.timercommand.moveInParallel;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class Next2HumanCommand extends SequentialCommandGroup {
  /** Creates a new Next2Human. */
  // private final Swerve s_Swerve;
  private Swerve s_Swerve;
  private shootingSubsystem ShootingSubsystem;
  private CartridgeSubsystem cartridgeSubsystem;
  private CollectSubsystem collectSubsystem;
  // private ClosingCollectGroupCommand collectGroupCommand;
  private timeSetPointCollectCommand timeSetPointCollectCommand;
  private TimerArmPosition TimerArmPosition;
  private collectWheels collectWheels;
  private armCollectSubsystem armCollectSubsystem;
  private Limelight limelight;

  public Next2HumanCommand(Swerve swerve,
  CollectSubsystem collectSubsystem,
  CartridgeSubsystem cartridgeSubsystem,
  collectWheels collectWheels, armCollectSubsystem armCollectSubsystem, Limelight limelight, shootingSubsystem shootingSubsystem) {
    // Add your commands in the addCommands() call, e.g.
    this.s_Swerve = swerve;
    this.collectSubsystem = collectSubsystem;
    this.cartridgeSubsystem = cartridgeSubsystem;
    this.collectWheels = collectWheels;
    this.limelight = limelight;
    this.armCollectSubsystem = armCollectSubsystem;
    this.ShootingSubsystem = shootingSubsystem;
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(new InstantCommand(() -> swerve.zeroGyro()), new resetCommand(ShootingSubsystem, collectSubsystem, armCollectSubsystem, cartridgeSubsystem),
    new ShootingCommand(shootingSubsystem, cartridgeSubsystem, armCollectSubsystem, 0.5, 0.4),
    new StartAuto(AutoCommand.getAutoCommand(swerve, "Next 2 Human - start"), armCollectSubsystem, swerve),
    new moveInParallel(swerve, collectSubsystem, collectWheels, armCollectSubsystem, cartridgeSubsystem, AutoCommand.getAutoCommand(swerve, "next 2 human & 1 cube"), 290, 5.2, 2, 0.2, -0.3, 0.2),
    new LimelightCommand(limelight, swerve, true, -0.2, 0),
    new ShootingGroupCommand(shootingSubsystem, armCollectSubsystem, cartridgeSubsystem , 5.2, 0 , 0.3, 0.5)
    ); 
  }
}
