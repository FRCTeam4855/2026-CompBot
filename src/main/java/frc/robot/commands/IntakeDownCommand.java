package frc.robot.commands;

import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkBase.ControlType;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.IntakeConstants;
import frc.robot.subsystems.IntakeSubsystem;

public class IntakeDownCommand extends Command {

    IntakeSubsystem intake;
    private Timer timer;

    public IntakeDownCommand(IntakeSubsystem intake) {
        this.intake = intake;
        addRequirements(intake);
        timer = new Timer();
    }

    @Override
    public void initialize() {
        timer.start();
        timer.start();
        if (intake.m_encoder.getPosition() < 0.125) {
            intake.anglePIDController.setSetpoint(IntakeConstants.kIntakeExtendPosition, ControlType.kPosition, ClosedLoopSlot.kSlot0);
        } else {
            intake.anglePIDController.setSetpoint(IntakeConstants.kIntakeSlowPosition, ControlType.kPosition, ClosedLoopSlot.kSlot0);
        }

    }

    @Override
    public boolean isFinished() {
        if ((intake.m_encoder.getPosition() > IntakeConstants.kIntakeSlowPosition - 0.005
                && intake.m_encoder.getPosition() < IntakeConstants.kIntakeSlowPosition + 0.005) || timer.get() > 1) {
            intake.anglePIDController.setSetpoint(IntakeConstants.kIntakeExtendPosition, ControlType.kPosition, ClosedLoopSlot.kSlot1);
            timer.stop();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void end(boolean interrupted) {
        System.out.println("IntakeDownCommand Finished");
    }
}
