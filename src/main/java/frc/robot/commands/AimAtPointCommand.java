package frc.robot.commands;

import org.photonvision.PhotonUtils;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import frc.robot.Constants.PoseConstants;
import frc.robot.Constants.SwerveConstants;
import frc.robot.subsystems.SwerveSubsystem;

public class AimAtPointCommand extends Command {
    
    SwerveSubsystem drive;
    CommandJoystick leftJoystick;

    public AimAtPointCommand(SwerveSubsystem drive, CommandJoystick leftJoystick) {
        this.drive = drive;
        this.leftJoystick = leftJoystick;
        addRequirements(drive);
    }

    @Override
    public void initialize() {
        System.out.println("aimAtPoint initialized for blue side");
        drive.getSwerveDrive().setHeadingCorrection(true);
    }

    @Override
    public void execute() {
        Rotation2d targetYaw = drive.isRedAlliance() ? PhotonUtils.getYawToPose(drive.getPose(), PoseConstants.kredHubPose) :
                                                        PhotonUtils.getYawToPose(drive.getPose(), PoseConstants.kblueHubPose);
        Rotation2d absoluteTargetYaw = targetYaw.plus(drive.getPose().getRotation());
        System.out.println("Target Yaw being sent: " + absoluteTargetYaw);
        drive.driveFieldOriented(drive.getTargetSpeeds((0.4 * leftJoystick.getY() + 0.6 * Math.pow(leftJoystick.getY(), 3) * -SwerveConstants.kSpeedMultiplierDefault),
                                  (0.4 * leftJoystick.getX() + 0.6 * Math.pow(leftJoystick.getX(), 3) * -SwerveConstants.kSpeedMultiplierDefault),
                                  absoluteTargetYaw));
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        drive.getSwerveDrive().setHeadingCorrection(false);
    }
}
