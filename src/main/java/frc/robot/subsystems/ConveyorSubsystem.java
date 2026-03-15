package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Configs.ConveyorConfigs;
import frc.robot.Constants.ConveyorConstants;

public class ConveyorSubsystem extends Subsystem {
    
    public final SparkMax m_elevatorSpark, m_conveyorSpark;
    public final SparkClosedLoopController m_elevatorController, m_conveyorController;
    public boolean elevatorRunning = false, conveyorRunning = false;
    private final DigitalInput m_BallSensor;
    public boolean m_BallDetected;
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

        m_elevatorSpark.configure(ConveyorConfigs.elevatorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        m_conveyorSpark.configure(ConveyorConfigs.conveyorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

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

    public void startElevatorLaunch() {
        m_elevatorController.setSetpoint(ConveyorConstants.kElevatorLaunchSpeed, ControlType.kVelocity);
        elevatorRunning = true;
    }

    public void startElevatorIntake() {
        m_elevatorController.setSetpoint(ConveyorConstants.kElevatorIntakeSpeed, ControlType.kVelocity);
        elevatorRunning = true;
    }

    public void stopElevator() {
        m_elevatorSpark.set(0);
        elevatorRunning = false;
    }
    
    @Override
    public void periodic() {        
        // This method will be called once per scheduler run        
        m_BallDetected = !m_BallSensor.get();
        SmartDashboard.putBoolean("Ball Sensor", m_BallDetected);
        if(m_BallDetected && !m_flywheelSubsystem.flywheelUpToSpeed) {
            stopElevator();
            stopConveyor();
        }
    }
}
