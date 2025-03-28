// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants.ShooterConstants;
import frc.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class ShooterInit extends CommandBase {
	@SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
	private final Shooter shooter;

	/**
	 * Creates a new ExampleCommand.
	 *
	 * @param shooter The subsystem used by this command.
	 */
	public ShooterInit(Shooter shooter) {
		this.shooter = shooter;
		// Use addRequirements() here to declare subsystem dependencies.
		addRequirements(this.shooter);
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		shooter.moveDown();
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		shooter.stopTiltMotor();
		shooter.resetTiltEncoder();
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return shooter.getTiltAmps() > ShooterConstants.kStopAmps;
	}
}
