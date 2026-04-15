package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ConveyorSubsystem;

public class LoadElevatorCommand extends Command {

    ConveyorSubsystem conveyorSubsystem;

    public LoadElevatorCommand(ConveyorSubsystem conveyorSubsystem) {
        this.conveyorSubsystem = conveyorSubsystem;
        addRequirements(conveyorSubsystem);
    }

    @Override
    public void initialize() {
        conveyorSubsystem.startElevatorIntake();
        conveyorSubsystem.startConveyor();
    }

    @Override
    public boolean isFinished() {
        if (conveyorSubsystem.m_BallDetected) {
            conveyorSubsystem.stopConveyor();
            conveyorSubsystem.stopElevator();
            return true;
        } else {
            return false;
        }
    }
}
