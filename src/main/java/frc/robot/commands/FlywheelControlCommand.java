package frc.robot.commands;

import com.revrobotics.spark.SparkBase.ControlType;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.FlywheelSubsystem;

public class FlywheelControlCommand extends Command {
    FlywheelSubsystem l_flywheel;
    public FlywheelControlCommand(FlywheelSubsystem flywheel) {
            this.l_flywheel = flywheel;

            addRequirements(flywheel);
    }

    @Override
    public void initialize() {
        System.out.println("FlywheelControlCommand initialized");
        if (l_flywheel.flywheelRunning == true) {
            l_flywheel.m_flywheelL.set(0);
            l_flywheel.m_flywheelM.set(0);
            l_flywheel.m_flywheelR.set(0);
            l_flywheel.flywheelRunning = false;
        } else {
            l_flywheel.flywheelRunning = true;
        }
    }

    @Override
    public void execute() {
        System.out.printf("Executing! Speed %d\n", l_flywheel.goalFlywheelSpeed);
        if (l_flywheel.flywheelRunning == true) {
            // l_flywheel.m_pidControllerL.setSetpoint(l_flywheel.goalFlywheelSpeed, ControlType.kVelocity);
            // l_flywheel.m_pidControllerM.setSetpoint(l_flywheel.goalFlywheelSpeed, ControlType.kVelocity);
            // l_flywheel.m_pidControllerR.setSetpoint(l_flywheel.goalFlywheelSpeed, ControlType.kVelocity);
            l_flywheel.m_pidControllerL.setSetpoint(1000, ControlType.kVelocity);
            l_flywheel.m_pidControllerM.setSetpoint(1000, ControlType.kVelocity);
            l_flywheel.m_pidControllerR.setSetpoint(1000, ControlType.kVelocity); //MANUALLY RUN FLYWHEEL AT ANY RPM
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
