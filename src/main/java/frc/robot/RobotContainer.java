// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.LightsConstants;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Autos;
import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.LightsSubsystem;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.SwerveSubsystem;
import swervelib.SwerveDrive;
import swervelib.SwerveInputStream;

import java.io.File;
import java.util.function.DoubleSupplier;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

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
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
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

  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandJoystick m_driverController =
      new CommandJoystick(OperatorConstants.kDriverControllerPort);

  private final CommandJoystick m_rotController =
      new CommandJoystick(OperatorConstants.kRotControllerPort);



  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    autoChooser = AutoBuilder.buildAutoChooser();
    SmartDashboard.putData("Auto Chooser", autoChooser);
    // Configure the trigger bindings
    configureBindings();

    SwerveInputStream driveAngularVelocity = SwerveInputStream.of(drivebase.getSwerveDrive(),
                                                                () -> m_driverController.getY() * 1,
                                                                () -> m_driverController.getX() * 1)
                                                            .withControllerRotationAxis(() -> m_rotController.getX() * -1)
                                                            .deadband(OperatorConstants.DEADBAND)
                                                            .scaleTranslation(0.8)
                                                            .allianceRelativeControl(true);

    SwerveInputStream driveDirectAngle = driveAngularVelocity.copy().withControllerHeadingAxis(m_driverController::getX,
                                                                                               m_driverController::getY)
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
    NamedCommands.registerCommand("Green", new RunCommand(()-> m_lights.setLEDs(LightsConstants.GREEN), m_lights));
    NamedCommands.registerCommand("Violet", new RunCommand(()-> m_lights.setLEDs(LightsConstants.VIOLET), m_lights));
    NamedCommands.registerCommand("Hot Pink", new RunCommand(()-> m_lights.setLEDs(LightsConstants.HOT_PINK), m_lights));
    NamedCommands.registerCommand("Aqua", new RunCommand(()-> m_lights.setLEDs(LightsConstants.AQUA), m_lights));

    
    m_driverController.button(2).whileTrue(Commands.run(drivebase::lock, drivebase).repeatedly());
    m_rotController.button(2).onTrue(Commands.runOnce(() -> toggleFieldOriented()));
    m_driverController.button(3).debounce(0.1).onTrue(new InstantCommand(() -> drivebase.getSwerveDrive().zeroGyro())); //gyro reset
    m_rotController.button(3).debounce(0.1).onTrue(new InstantCommand(() -> drivebase.getSwerveDrive().setGyroOffset(new Rotation3d(0, 0, Math.toRadians(90))))); //gyro reset
    m_driverController.button(4).whileTrue(drivebase.strafeLeft());
    m_driverController.button(5).whileTrue(drivebase.strafeRight());
    m_rotController.button(4).whileTrue(drivebase.forward());
    m_rotController.button(5).whileTrue(drivebase.backward());

    //light start
    m_driverController.button(6).whileTrue(new RunCommand(()-> m_lights.setLEDs(LightsConstants.VIOLET), m_lights));
    m_driverController.button(7).whileTrue(new RunCommand(()-> m_lights.setLEDs(LightsConstants.HOT_PINK), m_lights));
    m_driverController.button(8).whileTrue(new RunCommand(()-> m_lights.setLEDs(LightsConstants.GREEN), m_lights));
    m_driverController.button(9).whileTrue(new RunCommand(()-> m_lights.setLEDs(LightsConstants.AQUA), m_lights));
  }

  /**
   * Toggle whether the robot is currently in field oriented or robot oriented control. This will affect the default drive command, but will not affect any commands that are currently running.
   * The default  drive command will automatically switch between field oriented and robot oriented control based on the value of the FieldOriented flag, so toggling this will immediately change the behavior of the default drive command.
   * This does not affect any non-default drive commands, so if you have a command that explicitly requires the drivebase and is currently running, toggling this will not change the behavior of that command until it ends and the default command is scheduled again.
  */
  private void toggleFieldOriented() {
    FieldOriented = !FieldOriented;
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

