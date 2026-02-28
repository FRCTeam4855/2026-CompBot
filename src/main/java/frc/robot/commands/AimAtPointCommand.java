package frc.robot.commands;

import org.photonvision.PhotonUtils;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import frc.robot.Constants.OperatorConstants;
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
    }

    @Override
    public void execute() {
        double joystickY = leftJoystick.getY();
        double joystickX = leftJoystick.getX();
        Rotation2d targetYaw = drive.isRedAlliance() ? PhotonUtils.getYawToPose(drive.getPose(), PoseConstants.kRedHubPose) :
                                                        PhotonUtils.getYawToPose(drive.getPose(), PoseConstants.kBlueHubPose);
        Rotation2d absoluteTargetYaw = targetYaw.plus(drive.getPose().getRotation());
        drive.driveFieldOriented(drive.getTargetSpeeds(joystickY,
                                joystickX,
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
