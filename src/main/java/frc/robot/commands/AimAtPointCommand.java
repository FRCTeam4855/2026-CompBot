package frc.robot.commands;

import org.photonvision.PhotonUtils;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import frc.robot.Constants.OperatorConstants;
import frc.robot.Constants.PoseConstants;
import frc.robot.subsystems.SwerveSubsystem;
import swervelib.SwerveInputStream;

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
        drive.driveFieldOriented(drive.getTargetSpeeds(-(0.4 * leftJoystick.getY() + 0.6 * Math.pow(leftJoystick.getY(), 3)),
                              -(0.4 * leftJoystick.getX() + 0.6 * Math.pow(leftJoystick.getX(), 3)),
                              PhotonUtils.getYawToPose(drive.getPose(), 
                              PoseConstants.kblueHubPose)));
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
