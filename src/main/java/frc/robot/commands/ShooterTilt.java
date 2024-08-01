// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants.ShooterConstants;
import frc.robot.subsystems.Shooter;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class ShooterTilt extends CommandBase {
	@SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
	private final Shooter shooter;
	private final DoubleSupplier speed;

	/**
	 * Creates a new ExampleCommand.
	 *
	 * @param shooter The subsystem used by this command.
	 */
	public ShooterTilt(Shooter shooter, DoubleSupplier speed) {
		this.shooter = shooter;
		this.speed = speed;
		// Use addRequirements() here to declare subsystem dependencies.
		addRequirements(this.shooter);
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
		// unused
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		double spd = speed.getAsDouble();

		if ((shooter.getTiltPosition() > ShooterConstants.hiLimit && spd > 0.0) ||
			(shooter.getTiltPosition() < ShooterConstants.loLimit && spd < 0.0)) {
			// outside of bounds, don't move the tilt
		} else {
			shooter.setTiltSpeed(spd);
		}
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		// unused
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return false;
	}
}
