// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Chassis;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class ChassisCartesianDrive extends CommandBase {
	@SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
	private final Chassis chassis;
	private final DoubleSupplier yAxis;
	private final DoubleSupplier xAxis;
	private final DoubleSupplier rot;

	/**
	 * Creates a new ExampleCommand.
	 *
	 * @param subsystem The subsystem used by this command.
	 */
	public ChassisCartesianDrive(Chassis chassis,
			DoubleSupplier yAxis,
			DoubleSupplier xAxis,
			DoubleSupplier rot) {
		this.yAxis = yAxis;
		this.xAxis = xAxis;
		this.rot = rot;
		this.chassis = chassis;
		// Use addRequirements() here to declare subsystem dependencies.
		addRequirements(this.chassis);
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
		// unused
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		chassis.driveCartesian(yAxis.getAsDouble(), xAxis.getAsDouble(), rot.getAsDouble());
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
