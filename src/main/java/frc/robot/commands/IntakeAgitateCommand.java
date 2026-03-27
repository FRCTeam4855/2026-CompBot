package frc.robot.commands;

import com.revrobotics.spark.SparkBase.ControlType;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.IntakeConstants;
import frc.robot.subsystems.IntakeSubsystem;

public class IntakeAgitateCommand extends Command {
    private IntakeSubsystem intake;
    private Timer timer;

    public IntakeAgitateCommand(IntakeSubsystem intake) {
        this.intake = intake;
        timer = new Timer();
        addRequirements(intake);
    }

    @Override
    public void initialize() {
        System.out.println("IntakeAgitateCommand initialized");
        intake.intakeForward();
        intake.anglePIDController.setSetpoint(IntakeConstants.kIntakeAgitatePosition, ControlType.kPosition);
        timer.reset();
        timer.start();
    }

    @Override
    public void execute() {
        if (timer.hasElapsed(1.0)) {
            intake.anglePIDController.setSetpoint(IntakeConstants.kIntakeAgitatePosition, ControlType.kPosition);
            timer.reset();
        } else {
            if (timer.hasElapsed(0.5)) {
                intake.anglePIDController.setSetpoint(IntakeConstants.kIntakeExtendPosition, ControlType.kPosition);
            }
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        intake.anglePIDController.setSetpoint(IntakeConstants.kIntakeExtendPosition, ControlType.kPosition);
        intake.intakeDeployed = false;
        System.out.println("IntakeAgitateCommand finished");
    }
}
