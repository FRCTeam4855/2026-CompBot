package frc.robot.commands;

import com.revrobotics.spark.SparkBase.ControlType;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.FlywheelSubsystem;

public class FlywheelControlCommand extends Command {
    FlywheelSubsystem flywheel;
    public FlywheelControlCommand(FlywheelSubsystem flywheel) {
            this.flywheel = flywheel;

            addRequirements(flywheel);
    }

    @Override
    public void initialize() {
        System.out.println("FlywheelControlCommand initialized");
        if (flywheel.flywheelRunning = true) {
            flywheel.m_flywheelL.set(0);
            flywheel.m_flywheelM.set(0);
            flywheel.m_flywheelR.set(0);
            flywheel.flywheelRunning = false;
        } else {
            flywheel.flywheelRunning = true;
        }
    }

    @Override
    public void execute() {
        if (flywheel.flywheelRunning) {
            flywheel.m_pidControllerL.setSetpoint(flywheel.goalFlywheelSpeed, ControlType.kVelocity);
            flywheel.m_pidControllerM.setSetpoint(flywheel.goalFlywheelSpeed, ControlType.kVelocity);
            flywheel.m_pidControllerR.setSetpoint(flywheel.goalFlywheelSpeed, ControlType.kVelocity);
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
