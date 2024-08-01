// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.commands.ChassisCartesianDrive;
import frc.robot.commands.ChassisCartesianFieldDrive;
import frc.robot.commands.ChassisPolarDrive;
import frc.robot.commands.ShooterTilt;
import frc.robot.commands.ShooterFire;
import frc.robot.commands.ShooterInit;
import frc.robot.commands.ShooterStop;

import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants.OIConstants;
import frc.robot.Constants.ShooterConstants;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
	// =============================================================
	// Define SubSystems
	private final Chassis chassis = new Chassis();
	private final Shooter shooter = new Shooter(chassis);

	private final Compressor compressor = new Compressor(PneumaticsModuleType.CTREPCM);

	// =============================================================
	// Define Joysticks
	public final XboxController driver = new XboxController(OIConstants.kDriverControllerPort);
	public final XboxController operator = new XboxController(OIConstants.kOperatorControllerPort);

	// =============================================================
	// Define Commands
	public final ChassisCartesianDrive chassisCartesianDrive = new ChassisCartesianDrive(chassis,
			() -> -driver.getLeftY(),
			() -> driver.getLeftX(),
			() -> driver.getRightX());

	public final ChassisCartesianFieldDrive chassisCartesianFieldDrive = new ChassisCartesianFieldDrive(chassis,
			() -> -driver.getLeftY(),
			() -> driver.getLeftX(),
			() -> driver.getRightX(),
			() -> chassis.getHeading());

	public final ChassisPolarDrive chassisPolarDrive = new ChassisPolarDrive(chassis,
			() -> driver.getLeftY(),
			() -> driver.getLeftX(),
			() -> driver.getRightX());

	public final ShooterTilt shooterTilt = new ShooterTilt(shooter,
			() -> (driver.getLeftTriggerAxis()+driver.getRightTriggerAxis()));

	public final ShooterInit shooterInit = new ShooterInit(shooter);
	public final ShooterFire shooterFireLeft = new ShooterFire(shooter,
			ShooterConstants.kLeftTube);
	public final ShooterFire shooterFireRight = new ShooterFire(shooter,
			ShooterConstants.kRightTube);
	public final ShooterStop shooterStop = new ShooterStop(shooter);

	private Command m_autoCommand;

	/**
	 * The container for the robot. Contains subsystems, OI devices, and commands.
	 */
	public RobotContainer() {
		System.out.println("++++++++++ Entering RobotContainer Constructor ++++++++++");
		DriverStation.reportWarning("++++++++++ Entering RobotContainer Constructor ++++++++++",false);			

		// ==============================================================================
		// Add Subsystems to Dashboard
		SmartDashboard.putData("Chassis", chassis);
		SmartDashboard.putData("Shooter", shooter);
		SmartDashboard.putData("Conpressor", compressor);
		
		// =============================================================
		// Configure default commands for each subsystem
		chassis.setDefaultCommand(chassisCartesianDrive);
		shooter.setDefaultCommand(shooterTilt);

		// Configure the button bindings
		configureButtonBindings();

		DriverStation.reportWarning("---------- Leaving RobotContainer Constructor ----------",false);			
		System.out.println("---------- Leaving RobotContainer Constructor ----------");
	}

	/**
	 * Use this method to define your button->command mappings. Buttons can be
	 * created by
	 * instantiating a {@link edu.wpi.first.wpilibj.GenericHID} or one of its
	 * subclasses ({@link
	 * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing
	 * it to a {@link
	 * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
	 */
	private void configureButtonBindings() {
		// new JoystickButton(driver, Button.kA.value).whenPressed(chassisCartesianDrive);
		// new JoystickButton(driver, Button.kB.value).whenPressed(chassisCartesianFieldDrive);
		// new JoystickButton(driver, Button.kY.value).whenPressed(chassisPolarDrive);

		new JoystickButton(driver, Button.kLeftBumper.value).whenPressed(shooterFireLeft);
		new JoystickButton(driver, Button.kRightBumper.value).whenPressed(shooterFireRight);
		new JoystickButton(driver, Button.kX.value).whenPressed(shooterInit);
	}

	/**
	 * Use this to pass the autonomous command to the main {@link Robot} class.
	 *
	 * @return the command to run in autonomous
	 */
	public Command getAutonomousCommand() {
		// An ExampleCommand will run in autonomous
		return m_autoCommand;
	}
}
