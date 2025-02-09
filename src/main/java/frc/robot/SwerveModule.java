package frc.robot;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;

import frc.lib.math.Conversions;
import frc.lib.util.CTREModuleState;
import frc.lib.util.SwerveModuleConstants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.sensors.CANCoder;

import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;

public class SwerveModule {
    public int moduleNumber;
    private Rotation2d angleOffset;
    private Rotation2d lastAngle;

    private CANSparkMax mAngleMotor;
    private TalonFX mDriveMotor;
    // private TalonSRX angleEncoder;
    private CANCoder angleEncoder;

    private RelativeEncoder integratedAngleEncoder;
    private final SparkMaxPIDController angleController;
    

    SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(Constants.SwerveConstant.driveKS, Constants.SwerveConstant.driveKV, Constants.SwerveConstant.driveKA);

    public SwerveModule(int moduleNumber, SwerveModuleConstants moduleConstants){
        this.moduleNumber = moduleNumber;
        this.angleOffset = moduleConstants.angleOffset;
        
        
        /* Angle Encoder Config */
        angleEncoder = new CANCoder(moduleConstants.cancoderID);
        configAngleEncoder();

        /* Angle Motor Config */
        mAngleMotor = new CANSparkMax(moduleConstants.angleMotorID, MotorType.kBrushless);
        integratedAngleEncoder = mAngleMotor.getEncoder();
        angleController = mAngleMotor.getPIDController();
        configAngleMotor();

        /* Drive Motor Config */
        mDriveMotor = new TalonFX(moduleConstants.driveMotorID);
        configDriveMotor();

        lastAngle = getState().angle;
    }

    public void setDesiredState(SwerveModuleState desiredState, boolean isOpenLoop){
        /* This is a custom optimize function, since default WPILib optimize assumes continuous controller which CTRE and Rev onboard is not */
        desiredState = CTREModuleState.optimize(desiredState, getState().angle); 
        setAngle(desiredState);
        setSpeed(desiredState, isOpenLoop);
    }

    private void setSpeed(SwerveModuleState desiredState, boolean isOpenLoop){
        double desiredSpeed = desiredState.speedMetersPerSecond;
        double breakDesiredSpeed = desiredSpeed;
        // System.out.println("break" + RobotButtons.BreakValue.getAsDouble());
        if (RobotButtons.halfSpeed.getAsBoolean()) {
            // desiredSpeed /= 2.3;
        }
        else if (RobotButtons.BreakValue.getAsDouble() > 0.01){
            // System.out.println("break 222");
            breakDesiredSpeed *= ((1.05 - RobotButtons.BreakValue.getAsDouble()));
            if(breakDesiredSpeed > 1)
                breakDesiredSpeed -= 0.15;
            desiredSpeed = breakDesiredSpeed;
        }

        if(isOpenLoop){
            double percentOutput = desiredSpeed / Constants.SwerveConstant.maxSpeed;
            mDriveMotor.set(ControlMode.PercentOutput, percentOutput);
        }
        else {
            double velocity = Conversions.MPSToFalcon(desiredSpeed, Constants.SwerveConstant.wheelCircumference, Constants.SwerveConstant.driveGearRatio);
            mDriveMotor.set(ControlMode.Velocity, velocity, DemandType.ArbitraryFeedForward, feedforward.calculate(desiredSpeed));
        }
    }

    private void setAngle(SwerveModuleState desiredState){
        // Prevent rotating module if speed is less then 1%. Prevents jittering.
        Rotation2d angle =
            (Math.abs(desiredState.speedMetersPerSecond) <= (Constants.SwerveConstant.maxSpeed * 0.01))
                ? lastAngle
                : desiredState.angle;

        angleController.setReference(angle.getDegrees(), ControlType.kPosition);
        lastAngle = angle;
    }

    public void forceSetAngle(Rotation2d angle){
        angleController.setReference(angle.getDegrees(), ControlType.kPosition);
    }

    private Rotation2d getAngle(){
        return Rotation2d.fromDegrees(integratedAngleEncoder.getPosition());
    }

    public Rotation2d getCanCoder(){
        return Rotation2d.fromDegrees(angleEncoder.getAbsolutePosition());
        // return Rotation2d.fromDegrees(angleEncoder.getSelectedSensorPosition() * (360.0 / 4096.0)); // for a relative Encoder (4096 = CPR of the Encoder)
    }

    public void resetToAbsolute(){
        // double absolutePosition = Conversions.degreesToFalcon(getCanCoder().getDegrees() - angleOffset.getDegrees(), Constants.Swerve.angleGearRatio); // for an absolute Encoder
        // double absolutePosition = 0; // for a relative Encoder
        integratedAngleEncoder.setPosition(getCanCoder().getDegrees() - angleOffset.getDegrees());
    }


    private void configAngleEncoder(){        
        angleEncoder.configFactoryDefault();
        angleEncoder.configAllSettings(Robot.ctreConfigs.swerveCanCoderConfig);
    }

    private void configAngleMotor(){
        mAngleMotor.restoreFactoryDefaults();
        mAngleMotor.setSmartCurrentLimit(Constants.SwerveConstant.angleContinuousCurrentLimit);
        mAngleMotor.setInverted(Constants.SwerveConstant.angleMotorInvert);
        mAngleMotor.setIdleMode(Constants.SwerveConstant.angleNeutralMode);
        integratedAngleEncoder.setPositionConversionFactor(Constants.SwerveConstant.angleConversionFactor);
        angleController.setP(Constants.SwerveConstant.angleKP);
        angleController.setI(Constants.SwerveConstant.angleKI);
        angleController.setD(Constants.SwerveConstant.angleKD);
        angleController.setFF(Constants.SwerveConstant.angleKF);
        mAngleMotor.burnFlash();
        resetToAbsolute();
    }

    private void configDriveMotor(){        
        mDriveMotor.configFactoryDefault();
        mDriveMotor.configAllSettings(Robot.ctreConfigs.swerveDriveFXConfig);
        mDriveMotor.setInverted(Constants.SwerveConstant.driveMotorInvert);
        mDriveMotor.setNeutralMode(Constants.SwerveConstant.driveNeutralMode);
        mDriveMotor.setSelectedSensorPosition(0);
    }

    public SwerveModuleState getState(){
        return new SwerveModuleState(
            Conversions.falconToMPS(mDriveMotor.getSelectedSensorVelocity(), Constants.SwerveConstant.wheelCircumference, Constants.SwerveConstant.driveGearRatio), 
            getAngle()
        ); 
    }

    public SwerveModulePosition getPosition(){
        return new SwerveModulePosition(
            Conversions.falconToMeters(mDriveMotor.getSelectedSensorPosition(), Constants.SwerveConstant.wheelCircumference, Constants.SwerveConstant.driveGearRatio), 
            getAngle()
        );
    }
}