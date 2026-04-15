package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Configs.ConveyorConfigs;
import frc.robot.Constants.ConveyorConstants;

public class ConveyorSubsystem extends Subsystem {

    public final SparkMax m_elevatorSpark, m_conveyorSpark;
    public final SparkClosedLoopController m_elevatorController, m_conveyorController;
    public final RelativeEncoder m_elevatorEncoder;
    public boolean m_BallDetected, elevatorRunning = false, conveyorRunning = false, launchInProgress = false, fixingStall = false;
    private final DigitalInput m_BallSensor;
    private FlywheelSubsystem m_flywheelSubsystem;

    private static ConveyorSubsystem mInstance;

    public static ConveyorSubsystem getInstance() {
        if (mInstance == null) {
            mInstance = new ConveyorSubsystem();
        }
        return mInstance;
    }

    @Override
    public void robotInit() {
        DataLogManager.log("ConveyorSubsystem in robotInit");
    }

    @Override
    public void teleopInit() {
        DataLogManager.log("ConveyorSubsystem in teleopInit");
    }

    @Override
    public void autonomousInit() {
        DataLogManager.log("ConveyorSubsystem in autonomousInit");
    }

    public ConveyorSubsystem() {
        m_elevatorSpark = new SparkMax(ConveyorConstants.kElevatorCanId, MotorType.kBrushless);
        m_conveyorSpark = new SparkMax(ConveyorConstants.kConveyorCanId, MotorType.kBrushless);

        m_elevatorController = m_elevatorSpark.getClosedLoopController();
        m_conveyorController = m_conveyorSpark.getClosedLoopController();

        m_elevatorEncoder = m_elevatorSpark.getEncoder();

        m_elevatorSpark.configure(ConveyorConfigs.elevatorConfig, ResetMode.kResetSafeParameters,
                PersistMode.kPersistParameters);
        m_conveyorSpark.configure(ConveyorConfigs.conveyorConfig, ResetMode.kResetSafeParameters,
                PersistMode.kPersistParameters);

        m_BallSensor = new DigitalInput(0);
        m_flywheelSubsystem = FlywheelSubsystem.getInstance();
    }

    public void toggleConveyor() {
        if (conveyorRunning) {
            stopConveyor();
        } else {
            startConveyor();
        }
    }

    public void startConveyor() {
        m_conveyorController.setSetpoint(ConveyorConstants.kConveyorSpeed, ControlType.kVelocity);
        conveyorRunning = true;
    }

    public void reverseConveyor() {
        m_conveyorController.setSetpoint(-ConveyorConstants.kConveyorSpeed, ControlType.kVelocity);
        conveyorRunning = true;
    }

    public void stopConveyor() {
        m_conveyorSpark.set(0.0);
        conveyorRunning = false;
    }

    public void toggleElevator() {
        if (elevatorRunning) {
            stopElevator();
        } else {
            startElevatorIntake();
        }
    }

    public void reverseElevator() {
        m_elevatorController.setSetpoint(-ConveyorConstants.kElevatorIntakeSpeed, ControlType.kVelocity);
        elevatorRunning = true;
    }

    public void startElevatorLaunch() {
        m_elevatorController.setSetpoint(ConveyorConstants.kElevatorLaunchSpeed, ControlType.kVelocity);
        elevatorRunning = true;
        launchInProgress = true;
    }

    public void startElevatorIntake() {
        m_elevatorController.setSetpoint(ConveyorConstants.kElevatorIntakeSpeed, ControlType.kVelocity);
        elevatorRunning = true;
    }

    public void stopElevator() {
        m_elevatorSpark.set(0);
        elevatorRunning = false;
        launchInProgress = false;
    }

    @Override
    public void periodic() {
        m_BallDetected = !m_BallSensor.get();
        SmartDashboard.putBoolean("Ball Sensor", m_BallDetected);
        SmartDashboard.putBoolean("Elevator stalled?", checkStall());
        SmartDashboard.putBoolean("Stop Conveyor and Elevator", stopSystems());
        if(checkStall() && !fixingStall && elevatorRunning) { 
            fixingStall = true;
            CommandScheduler.getInstance().schedule(new SequentialCommandGroup(new WaitCommand(3),
            new InstantCommand(() -> fixStall()), 
            new WaitCommand(0.25), 
            new InstantCommand(() -> restartElevator()),
            new InstantCommand(() -> fixingStall = !fixingStall)));
        }

        if (stopSystems()) {
            stopElevator();
            stopConveyor();
        }
    }

    public void restartElevator() {
        if (elevatorRunning) {
            m_elevatorController.setSetpoint(ConveyorConstants.kElevatorIntakeSpeed, ControlType.kVelocity);
            elevatorRunning = true;
        }
    }

    public boolean stopSystems() {
        return m_BallDetected && !m_flywheelSubsystem.flywheelUpToSpeed && !launchInProgress && !m_flywheelSubsystem.overrideUpToSpeed;
    }

    public void fixStall() {
        if (checkStall()) {
            m_elevatorController.setSetpoint(-ConveyorConstants.kElevatorIntakeSpeed, ControlType.kVelocity);
            elevatorRunning = true;
        }
    }

    public boolean checkStall() {
        return !m_elevatorController.isAtSetpoint() && m_elevatorEncoder.getVelocity() < ConveyorConstants.kElevatorIntakeSpeed / 2 && elevatorRunning;
    }

    public void toggleStallFix() {
        fixingStall = !fixingStall;
    }
}
