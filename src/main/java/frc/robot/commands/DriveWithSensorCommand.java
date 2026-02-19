package frc.robot.commands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.SensorSubsystem;

public class DriveWithSensorCommand extends Command {
    SwerveSubsystem drive;
    ChassisSpeeds speed;
    
    public DriveWithSensorCommand(SwerveSubsystem drive, ChassisSpeeds speed) {
        this.drive = drive;
        this.speed = speed;
        addRequirements(drive);
    }

    @Override
    public void initialize() {
        System.out.println("DriveWithSensorCommand initialized");
    }

    @Override
    public void execute() {
        drive.getSwerveDrive().drive(speed);
    }

    @Override
    public boolean isFinished() {
        if(SensorSubsystem.getSensor1() && !SensorSubsystem.getSensor2()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void end(boolean interrupted) {
        System.out.println("DriveWithSensorCommand finished");
    }
}