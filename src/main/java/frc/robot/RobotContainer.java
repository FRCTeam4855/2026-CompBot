// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.LightsConstants;
import frc.robot.Constants.OperatorConstants;
import frc.robot.Constants.SwerveConstants;
import frc.robot.commands.RotateForBumpCommand;
import frc.robot.subsystems.LightsSubsystem;
import frc.robot.subsystems.SwerveSubsystem;
import swervelib.SwerveInputStream;

import java.io.File;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  private final LightsSubsystem m_lights = new LightsSubsystem();
  private final SendableChooser<Command> autoChooser;
  // The robot's subsystems and commands are defined here...
  public final SwerveSubsystem drivebase = new SwerveSubsystem(new File(Filesystem.getDeployDirectory(), "swerve"));
  
  public static boolean FieldOriented = true;
  public static boolean SlowMode = false;
  public static double speedMultiplier = SwerveConstants.kSpeedMultiplierDefault;

  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandJoystick m_leftDriveController =
      new CommandJoystick(OperatorConstants.kDriverControllerPort);

  private final CommandJoystick m_rightDriveController =
      new CommandJoystick(OperatorConstants.kRotControllerPort);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    autoChooser = AutoBuilder.buildAutoChooser();
    SmartDashboard.putData("Auto Chooser", autoChooser);
    // Configure the trigger bindings
    configureBindings();

    SwerveInputStream driveAngularVelocity = SwerveInputStream.of(drivebase.getSwerveDrive(), 
                                                                () -> m_leftDriveController.getY() * -speedMultiplier,
                                                                () -> m_leftDriveController.getX() * -speedMultiplier)
                                                            .withControllerRotationAxis(() -> m_rightDriveController.getX() * -speedMultiplier)
                                                            .deadband(OperatorConstants.DEADBAND)
                                                            .scaleTranslation(0.8)
                                                            .allianceRelativeControl(true);

    SwerveInputStream driveDirectAngle = driveAngularVelocity.copy().withControllerHeadingAxis(m_leftDriveController::getX,
                                                                                               m_leftDriveController::getY)
                                                           .headingWhile(true);

    drivebase.setDefaultCommand(
      drivebase.run(()-> {
        if(FieldOriented) {
          drivebase.getSwerveDrive().driveFieldOriented(driveAngularVelocity.get());
        } else {
          drivebase.getSwerveDrive().drive(driveAngularVelocity.get());
        }
      })
    );

    //register named commands
    NamedCommands.registerCommand("Green", new RunCommand(()-> m_lights.setLEDs(LightsConstants.GREEN), m_lights).repeatedly());
    NamedCommands.registerCommand("Violet", new RunCommand(()-> m_lights.setLEDs(LightsConstants.VIOLET), m_lights).repeatedly());
    NamedCommands.registerCommand("Hot Pink", new RunCommand(()-> m_lights.setLEDs(LightsConstants.HOT_PINK), m_lights).repeatedly());
    NamedCommands.registerCommand("Aqua", new RunCommand(()-> m_lights.setLEDs(LightsConstants.AQUA), m_lights).repeatedly());

    NamedCommands.registerCommand("Intake Representation", new RunCommand(()-> m_lights.setLEDs(LightsConstants.GREEN), m_lights).repeatedly()
                                                                .alongWith(new InstantCommand(()-> System.out.println("Intaking!!!"))));

    NamedCommands.registerCommand("Launch Representation", new RunCommand(()-> m_lights.setLEDs(LightsConstants.RED), m_lights).repeatedly()
                                                                .alongWith(new InstantCommand(()-> System.out.println("Launching!!!"))));

    NamedCommands.registerCommand("Climb Representation", new RunCommand(()-> m_lights.setLEDs(LightsConstants.VIOLET), m_lights).repeatedly()
                                                                .alongWith(new InstantCommand(()-> System.out.println("Climbing!!!"))));
                            
    NamedCommands.registerCommand("setX", new RunCommand(drivebase::lock, drivebase).repeatedly());

  }

    public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {

      //drivebase commands
    m_leftDriveController.button(1).onChange(Commands.runOnce(() -> toggleSlowMode()));
    m_leftDriveController.button(2).whileTrue(Commands.run(drivebase::lock, drivebase).repeatedly());
    m_rightDriveController.button(2).onTrue(Commands.runOnce(() -> toggleFieldOriented()));
    m_rightDriveController.button(4).debounce(0.1).onTrue(new InstantCommand(() -> drivebase.getSwerveDrive().zeroGyro())); //gyro reset
    m_rightDriveController.button(3).debounce(0.1).onTrue(new InstantCommand(() -> drivebase.getSwerveDrive().setGyroOffset(new Rotation3d(0, 0, Math.toRadians(90))))); //gyro 90 offset
    
    //movement commands
    m_leftDriveController.povLeft().whileTrue(drivebase.strafeLeft());
    m_leftDriveController.povRight().whileTrue(drivebase.strafeRight());
    m_leftDriveController.povUp().whileTrue(drivebase.forward());
    m_leftDriveController.povDown().whileTrue(drivebase.backward());

    m_rightDriveController.button(1).whileTrue(new RotateForBumpCommand(drivebase, m_leftDriveController));

    //light commands
    m_leftDriveController.button(6).whileTrue(new RunCommand(()-> m_lights.setLEDs(LightsConstants.VIOLET), m_lights));
    m_leftDriveController.button(7).whileTrue(new RunCommand(()-> m_lights.setLEDs(LightsConstants.HOT_PINK), m_lights));
    m_leftDriveController.button(11).whileTrue(new RunCommand(()-> m_lights.setLEDs(LightsConstants.GREEN), m_lights));
    m_leftDriveController.button(10).whileTrue(new RunCommand(()-> m_lights.setLEDs(LightsConstants.AQUA), m_lights));

    m_rightDriveController.button(6).whileTrue(NamedCommands.getCommand("Intake Representation"));
    m_rightDriveController.button(7).whileTrue(NamedCommands.getCommand("Launch Representation"));
    m_rightDriveController.button(11).whileTrue(NamedCommands.getCommand("Climb Representation"));
  }

  /**
   * Toggle whether the robot is currently in field oriented or robot oriented control. This will affect the default drive command, but will not affect any commands that are currently running.
   * The default  drive command will automatically switch between field oriented and robot oriented control based on the value of the FieldOriented flag, so toggling this will immediately change the behavior of the default drive command.
   * This does not affect any non-default drive commands, so if you have a command that explicitly requires the drivebase and is currently running, toggling this will not change the behavior of that command until it ends and the default command is scheduled again.
  */
  private void toggleFieldOriented() {
    FieldOriented = !FieldOriented;
  }

  private void toggleSlowMode() {
    SlowMode = !SlowMode;
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  // public Command getAutonomousCommand() {
  //   // An example command will be run in autonomous
  //   return Autos.exampleAuto(drivebase);
  // }

}

