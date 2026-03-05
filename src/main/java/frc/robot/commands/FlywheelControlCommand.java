package frc.robot.commands;

import com.revrobotics.spark.SparkBase.ControlType;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.FlywheelSubsystem;

public class FlywheelControlCommand extends Command {
    FlywheelSubsystem flywheel;
    public FlywheelControlCommand(FlywheelSubsystem flywheel) {
            this.flywheel = flywheel;
    }

    @Override
    public void initialize() {
        System.out.println("FlywheelControlCommand initialized");
    }

    @Override
    public void execute() {
            flywheel.m_pidControllerL.setSetpoint(flywheel.goalFlywheelSpeed, ControlType.kVelocity);
            flywheel.m_pidControllerM.setSetpoint(flywheel.goalFlywheelSpeed, ControlType.kVelocity);
            flywheel.m_pidControllerR.setSetpoint(flywheel.goalFlywheelSpeed, ControlType.kVelocity);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {

    }
}
