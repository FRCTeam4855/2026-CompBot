package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.FlywheelSubsystem.FlywheelRequest;
import frc.robot.Constants.FlywheelConstants;

public class FlywheelControlCommand extends Command {
    FlywheelSubsystem flywheel;
    FlywheelRequest request;
    public FlywheelControlCommand(FlywheelSubsystem flywheel, FlywheelRequest request) {
            this.flywheel = flywheel;
            this.request = request;
            addRequirements(flywheel);
    }

    @Override
    public void initialize() {
        System.out.println("FlywheelControlCommand initialized");
        switch (request) {
            case STOP:
                flywheel.flywheelRunning = false;
                break;
            case START:
            case START_WAIT:            
                flywheel.flywheelRunning = true;
                break;
            case TOGGLE:
                flywheel.flywheelRunning = !flywheel.flywheelRunning;
                break;
            default:
                break;
        }
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        switch (request) {
            case START_WAIT:
                return (flywheel.m_encoderL.getVelocity() >= flywheel.goalFlywheelSpeed * FlywheelConstants.kFlywheelTolerance);
            case START:
            case TOGGLE:
            case STOP:    
            default:
                return true;
        }
    }

    @Override
    public void end(boolean interrupted) {
        System.out.println("Flywheel Control Command Finished");
        flywheel.flywheelRunning = false;
    }
}
