package frc.robot.commands;

import com.revrobotics.spark.SparkBase.ControlType;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.IntakeConstants;
import frc.robot.subsystems.IntakeSubsystem;

public class IntakeDownCommand extends Command {

    IntakeSubsystem intake;

    public IntakeDownCommand(IntakeSubsystem intake) {
        this.intake = intake;
        addRequirements(intake);
    }
    
    @Override
    public void initialize() {
            intake.anglePIDController.setSetpoint(IntakeConstants.kIntakeSlowPosition, ControlType.kPosition);
            intake.intakeDeployed = true;
    }

    @Override
    public boolean isFinished() {
       if (intake.m_encoder.getPosition() > IntakeConstants.kIntakeSlowPosition - 0.002 && intake.m_encoder.getPosition() < IntakeConstants.kIntakeSlowPosition + 0.002) {
            intake.anglePIDController.setSetpoint(IntakeConstants.kIntakeExtendPosition, ControlType.kPosition);
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
