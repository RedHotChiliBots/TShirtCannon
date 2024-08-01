// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.AnalogIOConstants;
import frc.robot.Constants.CANidConstants;

public class Chassis extends SubsystemBase {

	// The Romi has the left and right motors set to
	// PWM channels 0 and 1 respectively
	private final WPI_TalonSRX frontLeft = new WPI_TalonSRX(CANidConstants.kLeftFrontMotor);
	private final WPI_TalonSRX frontRight = new WPI_TalonSRX(CANidConstants.kRightFrontMotor);
	private final WPI_TalonSRX rearLeft = new WPI_TalonSRX(CANidConstants.kLeftRearMotor);
	private final WPI_TalonSRX rearRight = new WPI_TalonSRX(CANidConstants.kRightRearMotor);

	// Set up the differential drive controller
	private final MecanumDrive robotDrive;

	// ==============================================================
	// Initialize NavX AHRS board
	// Alternatively: I2C.Port.kMXP, SerialPort.Port.kMXP or SerialPort.Port.kUSB
	private final AHRS ahrs = new AHRS(SPI.Port.kMXP);

	// ==============================================================
	// Identify PDP and PCM
	private final PowerDistribution pdp = new PowerDistribution();
	// private final Compressor compressor = new Compressor(PneumaticsModuleType.CTREPCM);

	// ==============================================================
	// Identify compressor hi and lo sensors
	private final AnalogInput hiPressureSensor = new AnalogInput(AnalogIOConstants.kHiPressureChannel);
	private final AnalogInput loPressureSensor = new AnalogInput(AnalogIOConstants.kLoPressureChannel);

	// ==============================================================
	// Define Shuffleboard data
	private final ShuffleboardTab chassisTab = Shuffleboard.getTab("Chassis");
	private final NetworkTableEntry sbPitch = chassisTab.addPersistent("Pitch", 0).getEntry();
	private final NetworkTableEntry sbAngle = chassisTab.addPersistent("Angle", 0).getEntry();
	private final NetworkTableEntry sbHeading = chassisTab.addPersistent("Heading", 0).getEntry();

	private final ShuffleboardTab pneumaticsTab = Shuffleboard.getTab("Pneumatics");
	private final NetworkTableEntry sbHiPressure = pneumaticsTab.addPersistent("Hi Pressure", 0).getEntry();
	private final NetworkTableEntry sbLoPressure = pneumaticsTab.addPersistent("Lo Pressure", 0).getEntry();

	/** Creates a new Mecanum drivetrain>. */
	public Chassis() {
		System.out.println("++++++++++ Entering Chassis Constructor ++++++++++");
		DriverStation.reportWarning("++++++++++ Entering Chassis Constructor ++++++++++",false);

		// Invert the right side motors.
		frontRight.setInverted(true);
		rearRight.setInverted(true);

		robotDrive = new MecanumDrive(frontLeft, rearLeft, frontRight, rearRight);

		robotDrive.setDeadband(0.1);

		DriverStation.reportWarning("---------- Leaving Chassis Constructor ----------",false);
		System.out.println("---------- Leaving Chassis Constructor ----------");
	}

	@Override
	public void periodic() {
		// This method will be called once per scheduler run
		sbHiPressure.setDouble(getHiPressure());
		sbLoPressure.setDouble(getLoPressure());

		sbPitch.setDouble(getPitch());
		sbAngle.setDouble(getAngle());
		sbHeading.setDouble(getHeading());
	}

	@Override
	public void simulationPeriodic() {
		// This method will be called once per scheduler run during simulation
	}

	public PowerDistribution getPDP() {
		return pdp;
	}
	
	public void driveCartesian(double ySpeed, double xSpeed, double zRotation) {
		robotDrive.driveCartesian(ySpeed, xSpeed, zRotation);
	}

	public void driveFieldCartesian(double ySpeed, double xSpeed, double zRotation, double gyroAngle) {
		robotDrive.driveCartesian(ySpeed, xSpeed, zRotation, gyroAngle);
	}

	public void drivePolar(double magnitude, double angle, double zRotation) {
		robotDrive.drivePolar(magnitude, angle, zRotation);
	}

	public double getHeading() {
		return ahrs.getFusedHeading();
	}

	public double getAngle() {
		return ahrs.getAngle();
	}

	public double getPitch() {
		return ahrs.getPitch();
	}

	public double getRoll() {
		return ahrs.getRoll();
	}

	public double getHiPressure() {
		return 250.0 * (hiPressureSensor.getVoltage() / AnalogIOConstants.kInputVoltage) - 25.0;
	}

	public double getLoPressure() {
		return 250.0 * (loPressureSensor.getVoltage() / AnalogIOConstants.kInputVoltage) - 25.0;
	}
}
