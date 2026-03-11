package frc.robot.commands;

import com.revrobotics.spark.SparkBase.ControlType;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.FlywheelSubsystem;

public class FlywheelControlCommand extends Command {
    FlywheelSubsystem l_flywheel;
    public FlywheelControlCommand(FlywheelSubsystem flywheel) {
            this.l_flywheel = flywheel;

            addRequirements(l_flywheel);
    }

    @Override
    public void initialize() {
        System.out.println("FlywheelControlCommand initialized");
            l_flywheel.flywheelRunning = !l_flywheel.flywheelRunning;
    }

    @Override
    public void execute() {
        //System.out.printf("Executing! Speed %d\n", l_flywheel.goalFlywheelSpeed);
        if (l_flywheel.flywheelRunning == true) {
            l_flywheel.m_pidControllerL.setSetpoint(l_flywheel.goalFlywheelSpeed, ControlType.kVelocity);
            l_flywheel.m_pidControllerM.setSetpoint(l_flywheel.goalFlywheelSpeed, ControlType.kVelocity);
            l_flywheel.m_pidControllerR.setSetpoint(l_flywheel.goalFlywheelSpeed, ControlType.kVelocity);
        }
    }

    @Override
    public boolean isFinished() {
        return !l_flywheel.flywheelRunning;
    }

    @Override
    public void end(boolean interrupted) {
        System.out.println("Flywheel Control Command Finished");
        l_flywheel.m_flywheelL.set(0);
        l_flywheel.m_flywheelM.set(0);
        l_flywheel.m_flywheelR.set(0);
        l_flywheel.flywheelRunning = false;
    }
}
