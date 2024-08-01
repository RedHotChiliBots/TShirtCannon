// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.CANidConstants;
import frc.robot.Constants.PDPidConstants;
import frc.robot.Constants.PWMChannelConstants;
import frc.robot.Constants.ShooterConstants;

public class Shooter extends SubsystemBase {

	// The Romi has the left and right motors set to
	// PWM channels 0 and 1 respectively
	private final WPI_TalonSRX tiltMotor = new WPI_TalonSRX(CANidConstants.kTiltMotor);
	private final Relay trigger = new Relay(PWMChannelConstants.kShooterLeftServo);

	private Chassis chassis;

//	private double tiltEncoderZeroPostion;

	// ==============================================================
	// Define Shuffleboard data
	private final ShuffleboardTab shooterTab = Shuffleboard.getTab("Shooter");
	private final NetworkTableEntry sbPosTics = shooterTab.addPersistent("Pos Tics", 0).getEntry();
	private final NetworkTableEntry sbPosInch = shooterTab.addPersistent("Pos Inch", 0).getEntry();
	private final NetworkTableEntry sbMinTics = shooterTab.addPersistent("Min Tics", 0).getEntry();
	private final NetworkTableEntry sbMinInch = shooterTab.addPersistent("Min Inch", 0).getEntry();
	private final NetworkTableEntry sbMaxTics = shooterTab.addPersistent("Max Tics", 0).getEntry();
	private final NetworkTableEntry sbMaxInch = shooterTab.addPersistent("Max Inch", 0).getEntry();
	private final NetworkTableEntry sbTiltPDPAmps = shooterTab.addPersistent("Tilt PDP Amps", 0).getEntry();
	private final NetworkTableEntry sbTiltStatorAmps = shooterTab.addPersistent("Tilt Stator Amps", 0).getEntry();
	private final NetworkTableEntry sbTiltSupplyAmps = shooterTab.addPersistent("Tilt Supply Amps", 0).getEntry();
	private final NetworkTableEntry sbTiltSpeed = shooterTab.addPersistent("Tilt Speed", 0).getEntry();

	public Shooter(Chassis chassis) {
		System.out.println("++++++++++ Entering Shooter Constructor ++++++++++");
		DriverStation.reportWarning("++++++++++ Entering Shooter Constructor ++++++++++",false);

		this.chassis = chassis;

		/* initialize firing tubes to off */
		stopTube();

		/* factory default values */
		tiltMotor.configFactoryDefault();
		tiltMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,
				ShooterConstants.kPIDLoopIdx,
				ShooterConstants.kTimeoutMs);
		tiltMotor.setSensorPhase(ShooterConstants.kSensorPhase);
		tiltMotor.setInverted(ShooterConstants.kMotorInvert);
		tiltMotor.configNominalOutputForward(0, ShooterConstants.kTimeoutMs);
		tiltMotor.configNominalOutputReverse(0, ShooterConstants.kTimeoutMs);
		tiltMotor.configPeakOutputForward(1, ShooterConstants.kTimeoutMs);
		tiltMotor.configPeakOutputReverse(-1, ShooterConstants.kTimeoutMs);

		tiltMotor.configAllowableClosedloopError(0,
				ShooterConstants.kPIDLoopIdx,
				ShooterConstants.kTimeoutMs);

		tiltMotor.config_kF(ShooterConstants.kPIDLoopIdx, ShooterConstants.kF, ShooterConstants.kTimeoutMs);
		tiltMotor.config_kP(ShooterConstants.kPIDLoopIdx, ShooterConstants.kP, ShooterConstants.kTimeoutMs);
		tiltMotor.config_kI(ShooterConstants.kPIDLoopIdx, ShooterConstants.kI, ShooterConstants.kTimeoutMs);
		tiltMotor.config_kD(ShooterConstants.kPIDLoopIdx, ShooterConstants.kD, ShooterConstants.kTimeoutMs);

		resetTiltEncoder();

		DriverStation.reportWarning("---------- Leaving Shooter Constructor ----------",false);
		System.out.println("---------- Leaving Shooter Constructor ----------");
	}

	@Override
	public void periodic() {
		// This method will be called once per scheduler run
		sbPosTics.setDouble(getTiltPosition());
		sbPosInch.setDouble(getTiltPosition() / ShooterConstants.ticsPerInch);
		sbMinTics.setDouble(ShooterConstants.loLimit);
		sbMinInch.setDouble(ShooterConstants.loLimit / ShooterConstants.ticsPerInch);
		sbMaxTics.setDouble(ShooterConstants.hiLimit);
		sbMaxInch.setDouble(ShooterConstants.hiLimit / ShooterConstants.ticsPerInch);
		sbTiltPDPAmps.setDouble(getTiltAmps());
		sbTiltSpeed.setDouble(getTiltSpeed());
		sbTiltStatorAmps.setDouble(tiltMotor.getStatorCurrent());
		sbTiltSupplyAmps.setDouble(tiltMotor.getSupplyCurrent());
	}

	@Override
	public void simulationPeriodic() {
		// This method will be called once per scheduler run during simulation
	}

	public void moveDown() {
		tiltMotor.set(ShooterConstants.kDownSpd);
	}

	public void setTiltSpeed(double spd) {
		tiltMotor.set(spd);
	}

	public double getTiltSpeed() {
		return tiltMotor.get();
	}

	public double getTiltAmps() {
		return chassis.getPDP().getCurrent(PDPidConstants.kTiltMotor);
	}

	public double getTiltPosition() {
		return tiltMotor.getSelectedSensorPosition();
	}

	public void setAngle(double angle) {
		tiltMotor.set(ControlMode.Position, angle);
	}

	public void stopTiltMotor() {
		tiltMotor.stopMotor();
	}

	public void resetTiltEncoder() {
		 tiltMotor.setSelectedSensorPosition(0.0,
		 		ShooterConstants.kPIDLoopIdx,
		 		ShooterConstants.kTimeoutMs);
		// /**
		//  * Grab the 360 degree position of the MagEncoder's absolute
		//  * position, and intitally set the relative sensor to match.
		//  */
		// int absolutePosition = tiltMotor.getSensorCollection().getPulseWidthPosition();

		// /* Mask out overflows, keep bottom 12 bits */
		// absolutePosition &= 0xFFF;
		// if (ShooterConstants.kSensorPhase) {
		// 	absolutePosition *= -1;
		// }
		// if (ShooterConstants.kMotorInvert) {
		// 	absolutePosition *= -1;
		// }

		// /* Set the quadrature (relative) sensor to match absolute */
		// tiltMotor.setSelectedSensorPosition(absolutePosition,
		// 		ShooterConstants.kPIDLoopIdx,
		// 		ShooterConstants.kTimeoutMs);

		// tiltEncoderZeroPostion = absolutePosition;
	}

	public void fireTube(int tube) {
		switch (tube) {
			case ShooterConstants.kLeftTube:
				trigger.set(Relay.Value.kForward);
				break;
			case ShooterConstants.kRightTube:
				trigger.set(Relay.Value.kReverse);
				break;
			default:
		}
	}

	public void stopTube() {
		trigger.set(Relay.Value.kOff);
	}
}
