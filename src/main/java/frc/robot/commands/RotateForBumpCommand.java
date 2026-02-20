package frc.robot.commands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import frc.robot.subsystems.SwerveSubsystem;

public class RotateForBumpCommand extends Command {

    SwerveSubsystem drive;
    CommandJoystick leftJoystick;
    private double target;
    private Rotation2d targetRotation;
    public RotateForBumpCommand(SwerveSubsystem drive, CommandJoystick leftJoystick) {
        this.leftJoystick = leftJoystick;
        this.drive = drive;
        addRequirements(drive);
    }

    @Override
    public void initialize() {
        System.out.println("RotateForBumpCommand initialized");
        int gyroAngle = (int) drive.getHeading().getDegrees();
        double signum = gyroAngle == 0 ? 1.0 : Math.signum(gyroAngle);
        target = gyroAngle / 90 * 90 + 45 * signum;
        targetRotation = new Rotation2d(Math.toRadians(target));
    }

    @Override
    public void execute() {
        drive.driveFieldOriented(drive.getTargetSpeeds(leftJoystick.getY(), leftJoystick.getX(), targetRotation));
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
