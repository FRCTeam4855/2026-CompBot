// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.Constants.SwerveConstants;
import frc.robot.commands.AimAtPointCommand;
import frc.robot.commands.FlywheelControlCommand;
import frc.robot.commands.IntakeAgitateCommand;
import frc.robot.commands.IntakeDownCommand;
import frc.robot.commands.LoadElevatorCommand;
import frc.robot.commands.RotateForBumpCommand;
import frc.robot.subsystems.ConveyorSubsystem;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.IndexerSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.FlywheelSubsystem.FlywheelRequest;
import swervelib.SwerveInputStream;

import java.io.File;
import java.util.Optional;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
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
  private final IntakeSubsystem m_intakeSubsystem = IntakeSubsystem.getInstance();
  private final ConveyorSubsystem m_conveyorSubsystem = ConveyorSubsystem.getInstance();
  private final FlywheelSubsystem m_flywheelSubsystem =  FlywheelSubsystem.getInstance();
  private final SendableChooser<Command> autoChooser;
  public static final SwerveSubsystem drivebase = SwerveSubsystem.getInstance(new File(Filesystem.getDeployDirectory(), "swerve"));
  private final IndexerSubsystem m_indexerSubsystem = IndexerSubsystem.getInstance();
  
  public static boolean FieldOriented = true;
  public static boolean SlowMode = false;
  public static double speedMultiplier = SwerveConstants.kSpeedMultiplierDefault;
  public static Optional<Alliance> alliance = DriverStation.getAlliance();

  private final CommandJoystick m_leftDriveController =
      new CommandJoystick(OperatorConstants.kDriverControllerPort);

  private final CommandJoystick m_rightDriveController =
      new CommandJoystick(OperatorConstants.kRotControllerPort);

  private final GenericHID m_operatorBoard =
      new GenericHID(OperatorConstants.kOperatorControllerPort);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    //register named commands

    NamedCommands.registerCommand("setX", new RunCommand(drivebase::lock, drivebase).repeatedly());

    NamedCommands.registerCommand("Intake Forward", new InstantCommand(() -> m_intakeSubsystem.intakeForward()));
    NamedCommands.registerCommand("Intake Reverse", new InstantCommand(() -> m_intakeSubsystem.intakeReverse()));
    NamedCommands.registerCommand("Intake Stop", new InstantCommand(() -> m_intakeSubsystem.intakeStop()));

    NamedCommands.registerCommand("Intake Retract Sequence", new InstantCommand(() -> m_intakeSubsystem.intakeRetractSequence()));

    NamedCommands.registerCommand("Conveyor Start", new InstantCommand(() -> m_conveyorSubsystem.startConveyor()));
    NamedCommands.registerCommand("Conveyor Stop", new InstantCommand(() -> m_conveyorSubsystem.stopConveyor()));
    NamedCommands.registerCommand("Conveyor Toggle", new InstantCommand(() -> m_conveyorSubsystem.toggleConveyor()));

    NamedCommands.registerCommand("Elevator Start", new InstantCommand(() -> m_conveyorSubsystem.startElevatorIntake()));
    NamedCommands.registerCommand("Elevator Stop", new InstantCommand(() -> m_conveyorSubsystem.stopElevator()));
    NamedCommands.registerCommand("Elevator Toggle", new InstantCommand(() -> m_conveyorSubsystem.toggleElevator()));

    NamedCommands.registerCommand("Indexer Start", new InstantCommand(() -> m_indexerSubsystem.startIndexer()));
    NamedCommands.registerCommand("Indexer Stop", new InstantCommand(() -> m_indexerSubsystem.stopIndexer()));
    NamedCommands.registerCommand("Indexer Toggle", new InstantCommand(() -> m_indexerSubsystem.toggleIndexer()));

    NamedCommands.registerCommand("Intake Deploy Sequence", new SequentialCommandGroup(new InstantCommand(() -> m_intakeSubsystem.intakeForward()),
                                                      new IntakeDownCommand(m_intakeSubsystem),
                                                      new WaitCommand(3.0), 
                                                      new LoadElevatorCommand(m_conveyorSubsystem)));

    NamedCommands.registerCommand("Launch Conveyor Sequence", new InstantCommand(() -> m_indexerSubsystem.startIndexer())
                                                   .alongWith(new InstantCommand(() -> m_conveyorSubsystem.startElevatorLaunch()))
                                                   .alongWith(new InstantCommand(() -> m_conveyorSubsystem.startConveyor())));

    NamedCommands.registerCommand("Stop All", new SequentialCommandGroup(new InstantCommand(() -> m_conveyorSubsystem.stopConveyor(), m_conveyorSubsystem),
                                                   new InstantCommand(() -> m_conveyorSubsystem.stopElevator(), m_conveyorSubsystem),
                                                   new InstantCommand(() -> m_indexerSubsystem.stopIndexer(), m_indexerSubsystem),
                                                   new InstantCommand(() -> m_intakeSubsystem.intakeRetractSequence(), m_intakeSubsystem),
                                                   new FlywheelControlCommand(m_flywheelSubsystem, FlywheelRequest.STOP)));

    NamedCommands.registerCommand("Stop Launch", new InstantCommand(() -> m_conveyorSubsystem.stopConveyor())
                                                   .alongWith(new InstantCommand(() -> m_conveyorSubsystem.stopElevator()))
                                                   .alongWith(new InstantCommand(() -> m_indexerSubsystem.stopIndexer()))
                                                   .alongWith(new FlywheelControlCommand(m_flywheelSubsystem, FlywheelRequest.STOP)));

    NamedCommands.registerCommand("Launch Sequence", new SequentialCommandGroup(
                                                    new FlywheelControlCommand(m_flywheelSubsystem, FlywheelRequest.START_WAIT),
                                                    NamedCommands.getCommand("Launch Conveyor Sequence")
                                                    .alongWith(new SequentialCommandGroup(new WaitCommand(1),
                                                    new IntakeAgitateCommand(m_intakeSubsystem)))));

    NamedCommands.registerCommand("Start Flywheel", new FlywheelControlCommand(m_flywheelSubsystem, FlywheelRequest.START_WAIT));

    NamedCommands.registerCommand("Aim At Hub", new AimAtPointCommand(drivebase, m_leftDriveController));

    NamedCommands.registerCommand("Drive Forward", drivebase.forward());

    // Build auto chooser AFTER registering named commands
    autoChooser = AutoBuilder.buildAutoChooser();
    SmartDashboard.putData("Auto Chooser", autoChooser);

    SmartDashboard.putNumber("Match Time", DriverStation.getMatchTime());

    // Configure the trigger bindings
    configureBindings();

    SwerveInputStream driveAngularVelocity = SwerveInputStream.of(drivebase.getSwerveDrive(),
                                                                () -> {
                                                                  double yInput = m_leftDriveController.getY();
                                                                  return (0.4 * yInput + 0.6 * yInput * yInput * yInput) * -speedMultiplier;
                                                                },
                                                                () -> {
                                                                  double xInput = m_leftDriveController.getX();
                                                                  return (0.4 * xInput + 0.6 * xInput * xInput * xInput) * -speedMultiplier;
                                                                })
                                                            .withControllerRotationAxis(() -> -m_rightDriveController.getX())
                                                            .deadband(OperatorConstants.DEADBAND)
                                                            .scaleTranslation(SwerveConstants.kScaleTranslation)
                                                            .allianceRelativeControl(true);

    // SwerveInputStream driveDirectAngle = driveAngularVelocity.copy().withControllerHeadingAxis(m_leftDriveController::getX,
    //                                                                                            m_leftDriveController::getY)
    //                                                        .headingWhile(true);

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
    
      //drivebase commands
    m_leftDriveController.button(1).whileTrue(new AimAtPointCommand(drivebase, m_leftDriveController));
    m_leftDriveController.button(2).whileTrue(Commands.run(drivebase::lock, drivebase).repeatedly());

    m_rightDriveController.button(2).onTrue(Commands.runOnce(() -> toggleFieldOriented()));
    m_rightDriveController.button(4).debounce(0.1).onTrue(new InstantCommand(() -> drivebase.getSwerveDrive().zeroGyro())); //gyro reset
    m_rightDriveController.button(3).debounce(0.1).onTrue(new InstantCommand(() -> drivebase.getSwerveDrive().setGyroOffset(new Rotation3d(0, 0, Math.toRadians(90))))); //gyro 90 offset
    m_rightDriveController.button(5).whileTrue(new RunCommand(() -> drivebase.driveToPose(new Pose2d(2, 2, new Rotation2d(0))), drivebase));
    
    //movement commands
    m_leftDriveController.povLeft().whileTrue(drivebase.strafeLeft());
    m_leftDriveController.povRight().whileTrue(drivebase.strafeRight());
    m_leftDriveController.povUp().whileTrue(drivebase.forward());
    m_leftDriveController.povDown().whileTrue(drivebase.backward());

    m_rightDriveController.button(1).whileTrue(new RotateForBumpCommand(drivebase, m_leftDriveController));

    //Operator Buttons

    new JoystickButton(m_operatorBoard, 1).onTrue(new InstantCommand(
      () -> m_intakeSubsystem.intakeToggle()));

    new JoystickButton(m_operatorBoard, 2).onTrue(new InstantCommand(
      () -> m_intakeSubsystem.intakeReverse()));

    new JoystickButton(m_operatorBoard, 3).onTrue(new InstantCommand(
      () -> m_intakeSubsystem.toggleIntakePosition(), m_intakeSubsystem));

    new JoystickButton(m_operatorBoard, 4).toggleOnTrue(new InstantCommand(() -> m_conveyorSubsystem.startConveyor()));

    new JoystickButton(m_operatorBoard, 5).onTrue(new FlywheelControlCommand(
      m_flywheelSubsystem, FlywheelRequest.START));

    new JoystickButton(m_operatorBoard, 6).onTrue(new FlywheelControlCommand(
      m_flywheelSubsystem, FlywheelRequest.STOP));

    new JoystickButton(m_operatorBoard, 8).onTrue(new InstantCommand(
      () -> m_conveyorSubsystem.toggleElevator()));

    new JoystickButton(m_operatorBoard, 11).onTrue(new InstantCommand(
      () -> m_indexerSubsystem.toggleIndexer()));

    new JoystickButton(m_operatorBoard, 14).onTrue(new InstantCommand(
      () -> m_conveyorSubsystem.reverseElevator()));

    new JoystickButton(m_operatorBoard, 15).onChange(new InstantCommand(
      () -> m_flywheelSubsystem.toggleDelieverSpeed()));

    new JoystickButton(m_operatorBoard, 16).whileTrue(new IntakeAgitateCommand(m_intakeSubsystem));

    new JoystickButton(m_operatorBoard, 17).onTrue(new LoadElevatorCommand(m_conveyorSubsystem));

    new JoystickButton(m_operatorBoard, 18).onTrue(NamedCommands.getCommand("Intake Deploy Sequence"));

    new JoystickButton(m_operatorBoard, 21).onTrue(NamedCommands.getCommand("Launch Sequence"));

    new JoystickButton(m_operatorBoard, 22).onTrue(NamedCommands.getCommand("Stop All"));

    new JoystickButton(m_operatorBoard, 19).onTrue(new InstantCommand(
      () -> m_flywheelSubsystem.incrementFlywheelSpeed(Constants.FlywheelConstants.kFlywheelOverrideAdjustment)));

    new JoystickButton(m_operatorBoard, 20).onTrue(new InstantCommand(
      () -> m_flywheelSubsystem.decrementFlywheelSpeed(Constants.FlywheelConstants.kFlywheelOverrideAdjustment)));

    new JoystickButton(m_operatorBoard, 12).onChange(new InstantCommand(
      () -> m_flywheelSubsystem.toggleOverride()));

    new JoystickButton(m_operatorBoard, 13).onTrue(new InstantCommand(
      () -> m_intakeSubsystem.intakeReverse()).alongWith(new InstantCommand(()-> m_conveyorSubsystem.reverseConveyor())).alongWith(new InstantCommand(() -> m_flywheelSubsystem.setOverride(true))));

    new JoystickButton(m_operatorBoard, 13).onFalse(new InstantCommand(
      () -> m_intakeSubsystem.intakeStop()).alongWith(new InstantCommand(()-> m_conveyorSubsystem.stopConveyor())).alongWith(new InstantCommand(() -> m_flywheelSubsystem.setOverride(false))));
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

