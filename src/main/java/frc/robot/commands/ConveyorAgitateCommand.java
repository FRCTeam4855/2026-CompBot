package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ConveyorSubsystem;

public class ConveyorAgitateCommand extends Command {

    private Timer timer;
    private ConveyorSubsystem conveyor;

    public ConveyorAgitateCommand(ConveyorSubsystem conveyor) {
        timer = new Timer();
        this.conveyor = conveyor;
        addRequirements(conveyor);
    }

    @Override
    public void initialize() {
        System.out.println("ConveyorAgitateCommand initialized");
        conveyor.startConveyor();
        timer.reset();
        timer.start();
    }

    @Override
    public void execute() {
        if (conveyor.conveyorRunning) {
            if (timer.hasElapsed(1.25)) {
                conveyor.startConveyor();
                timer.reset();
            } else if (timer.hasElapsed(1.0)) {
                conveyor.reverseConveyor();

            }
        }
    }

    @Override
    public boolean isFinished() {
        if (conveyor.conveyorRunning) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void end(boolean interrupted) {
        conveyor.stopConveyor();
        System.out.println("ConveyorAgitateCommand finished");
    }
}