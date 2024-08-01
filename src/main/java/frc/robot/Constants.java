// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
	public static final class CANidConstants {
		public static final int kPDP = 0;
		public static final int kCompressor = 0;

		public static final int kLeftFrontMotor = 3;
		public static final int kLeftRearMotor = 1;
		public static final int kRightFrontMotor = 5;
		public static final int kRightRearMotor = 7;

		public static final int kTiltMotor = 43;
	}

	public static final class PDPidConstants {
		public static final int kTiltMotor = 2;
	}

	public static final class AnalogIOConstants {
		public static final int kHiPressureChannel = 0;
		public static final int kLoPressureChannel = 1;

		public static final double kInputVoltage = 5.0;
	}

	public static final class PWMChannelConstants {
		public static final int kShooterLeftServo = 0;
		public static final int kShooterRightServo = 1;
	}

	public static final class OIConstants {
		public static final int kDriverControllerPort = 0;
		public static final int kOperatorControllerPort = 1;

		public static final long kRumbleDelay = 1000; // milliseconds
	}

	public static final class ChassisConstants {
		public static final double kCountsPerRevolution = 1440.0;
		public static final double kWheelDiameterInch = 2.75591; // 70 mm
	}

	public static final class ShooterConstants {
		public static final double kDownSpd = 0.2;
		public static final double kStopAmps = 3.0;
		
		public static final int kPIDLoopIdx = 0;
		public static final int kTimeoutMs = 30; // ms

		public static final boolean kSensorPhase = true;
		public static final boolean kMotorInvert = false;

		public static final double kP = 0.15;
		public static final double kI = 0.0;
		public static final double kD = 1.0;
		public static final double kF = 0.0;
		public static final double kIZone = 0;
		public static final double kPeakOutput = 1.0;

		public static final int kLeftTube = 1;
		public static final int kRightTube = 2;

		public static final double kFireTime = 0.02;

		private static final double turnsPerInch = 5.0;

		private static final double motorMaxRPMs = 21000.0;
		private static final double gearBoxRatio = 20.0;
		private static final double encoderTicsPerRev = 24.0;
		private static final double shaftTicsPerRev = gearBoxRatio * encoderTicsPerRev;

		public static final double ticsPerInch = shaftTicsPerRev * turnsPerInch;

		private static final double base = 10.0; // inches
		private static final double height = 10.0; // inches
		private static final double maxLength = 14.14; // Math.sqrt((Math.pow(base,2)+Math.pow(height,2))); // inches 14.14
		private static final double maxTurns = maxLength * turnsPerInch;
		private static final double maxTics = maxTurns * shaftTicsPerRev;

		public static final double loLimit = 0.0 + 500.0;	// stay about 1/4" away from stop
		public static final double hiLimit = maxTics - 500.0;	// stay 1/4" away from end

		private static final double maxShaftRPMs = motorMaxRPMs / gearBoxRatio;
		private static final double maxShaftRPSs = maxShaftRPMs / 60.0;

		private static final double maxTravelTime = 5.0; // seconds
		private static final double execInterval = 20.0; // ms
		private static final double maxIntervals = maxTravelTime * (1000.0 / execInterval);

		public static final double kAngleDelta = maxTics / maxIntervals;
	}
}
