package frc.robot.commands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import frc.robot.subsystems.SwerveSubsystem;

public class RotateForBumpCommand extends Command {

    SwerveSubsystem drive;
    CommandJoystick leftJoystick;
    private final int[] angles = {135, 225, 315};
    private double target = 45;
    private Rotation2d targetRotation;
    public RotateForBumpCommand(SwerveSubsystem drive, CommandJoystick leftJoystick) {
        this.leftJoystick = leftJoystick;
        this.drive = drive;
        addRequirements(drive);
    }

    @Override
    public void initialize() {
        double gyroAngle = drive.getHeading().getDegrees();
        for (int angle : angles) {
            if (Math.abs(gyroAngle - angle) < Math.abs(gyroAngle - target)) {
                target = angle;
            }
        }
        targetRotation = new Rotation2d(Math.toRadians(target));
    }

    @Override
    public void execute() {
        drive.driveFieldOriented(drive.getTargetSpeeds(leftJoystick.getX(), leftJoystick.getY(), targetRotation));
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
