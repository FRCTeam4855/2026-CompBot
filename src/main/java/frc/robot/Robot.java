// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.ArrayList;
import java.util.List;

import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.FollowPathCommand;

import java.io.File;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.units.Units;
import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.ConveyorSubsystem;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.IndexerSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LightsSubsystem;
import frc.robot.subsystems.PowerSubsystem;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import frc.robot.subsystems.ClimberSubsystem;

/**
 * The methods in this class are called automatically corresponding to each
 * mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the
 * package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;
  private final CommandJoystick m_leftDriveController = new CommandJoystick(OperatorConstants.kDriverControllerPort);
  public static boolean blueAlliance = true;

  private final RobotContainer m_robotContainer;
  List<Subsystem> m_allSubsystems = new ArrayList<>();

  private final double maxTime = 25.0;
  private final Timer loopTimer = new Timer();

  private final double delayTime = 10.0;
  private final Timer delayTimer = new Timer();

  private boolean active = false;

  /**
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */
  public Robot() {
    m_robotContainer = new RobotContainer();
  }

  @Override
  public void robotInit() {
    DataLogManager.start();
    DriverStation.startDataLog(DataLogManager.getLog());

    FollowPathCommand.warmupCommand().schedule();

    // m_allSubsystems.add(ClimberSubsystem.getInstance());
    m_allSubsystems.add(ConveyorSubsystem.getInstance());
    m_allSubsystems.add(FlywheelSubsystem.getInstance());
    m_allSubsystems.add(IndexerSubsystem.getInstance());
    m_allSubsystems.add(IntakeSubsystem.getInstance());
    m_allSubsystems.add(LightsSubsystem.getInstance());
    // m_allSubsystems.add(SensorSubsystem.getInstance());
    m_allSubsystems.add(SwerveSubsystem.getInstance(new File(Filesystem.getDeployDirectory(), "swerve")));
    m_allSubsystems.add(PowerSubsystem.getInstance());

    m_allSubsystems.forEach(subsystem -> {
      if (subsystem instanceof frc.robot.subsystems.Subsystem) {
        ((frc.robot.subsystems.Subsystem) subsystem).robotInit();
      }
    });
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items
   * like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {

    SmartDashboard.putNumber("Driver Y", m_leftDriveController.getY());
    SmartDashboard.putNumber("Driver X", m_leftDriveController.getX());
    // SmartDashboard.putNumber("Transition Shift", delayTimer.get());
    // SmartDashboard.putNumber("Hub Shifts", loopTimer.get());
    // HubTracker.timeRemainingInCurrentShift().ifPresent(time -> 
    // SmartDashboard.putNumber("Time Left in Shift", time.in(Units.Seconds))); 
    //SmartDashboard.putBoolean("Hub Active", active);
    SmartDashboard.putNumber("Time Left in Shift", 
    HubTracker.timeRemainingInCurrentShift()
    .map(time -> time.in(Units.Seconds))
    .orElse(0.0)
);
      if (delayTimer.hasElapsed(delayTime)) {
      loopTimer.start();
      }
    
    // Runs the Scheduler. This is responsible for polling buttons, adding
    // newly-scheduled
    // commands, running already-scheduled commands, removing finished or
    // interrupted commands,
    // and running subsystem periodic() methods. This must be called from the
    // robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
    
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  /**
   * This autonomous runs the autonomous command selected by your
   * {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {
    m_allSubsystems.forEach(subsystem -> subsystem.autonomousInit());

    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      CommandScheduler.getInstance().schedule(m_autonomousCommand);
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    m_allSubsystems.forEach(subsystem -> subsystem.teleopInit());


    delayTimer.start();

    //Schedule the "Stop All" named command to run when the robot enters teleop, which will stop all motors and reset any necessary subsystem states. This is important to prevent unexpected behavior from subsystems that were running during autonomous.
    NamedCommands.getCommand("Stop All").schedule();
    
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {

      if (delayTimer.hasElapsed(delayTime)) {
      delayTimer.stop();
      }

      if (loopTimer.hasElapsed(maxTime)) {
      loopTimer.reset();
      active =! active;
    }
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
  }
}
