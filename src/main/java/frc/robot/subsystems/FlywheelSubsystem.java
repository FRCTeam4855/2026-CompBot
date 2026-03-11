package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkFlexConfig;

import frc.robot.Constants.FlywheelConstants;
import frc.robot.RobotContainer;
import frc.robot.Configs.FlywheelConfigs;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkBase.ControlType;

public class FlywheelSubsystem extends Subsystem {
    
    public final SparkFlex m_flywheelL, m_flywheelM, m_flywheelR;
    public final SparkClosedLoopController m_pidControllerL, m_pidControllerM, m_pidControllerR;
    public final RelativeEncoder m_encoderL, m_encoderM, m_encoderR;
    public boolean flywheelRunning = false;
    public int goalFlywheelSpeed = 0;
    private int lastFlywheelSpeed = 0;
    private SwerveSubsystem swerve = RobotContainer.drivebase;
    public enum FlywheelRequest {
        STOP, START, START_WAIT, TOGGLE
    }   
    
    private static FlywheelSubsystem mInstance;
    public static FlywheelSubsystem getInstance() {
      if (mInstance == null) {
        mInstance = new FlywheelSubsystem();
      }
      return mInstance;
    }

    @Override
    public void robotInit() {
        DataLogManager.log("FlywheelSubsystem in robotInit");
    }

    @Override
    public void teleopInit() {
        DataLogManager.log("FlywheelSubsystem in teleopInit");
    }

    @Override
    public void autonomousInit() {
        DataLogManager.log("FlywheelSubsystem in autonomousInit");
    }

    public FlywheelSubsystem() {
        m_flywheelL = new SparkFlex(FlywheelConstants.kFlywheelLCanId, MotorType.kBrushless); 
        m_flywheelM = new SparkFlex(FlywheelConstants.kFlywheelMCanId, MotorType.kBrushless); 
        m_flywheelR = new SparkFlex(FlywheelConstants.kFlywheelRCanId, MotorType.kBrushless);
        m_pidControllerL = m_flywheelL.getClosedLoopController();
        m_pidControllerM = m_flywheelM.getClosedLoopController();
        m_pidControllerR = m_flywheelR.getClosedLoopController();
        m_encoderL = m_flywheelL.getEncoder();
        m_encoderM = m_flywheelM.getEncoder();
        m_encoderR = m_flywheelR.getEncoder();

        SparkBaseConfig flywheelRConfig = new SparkFlexConfig() // Creates a private Flywheel configuration
            .apply(FlywheelConfigs.flywheelConfig)  // Applies all of the flywheelConfig config settings into the private config
            .inverted(true);               // Adds the inverted configuration setting

        m_flywheelL.configure(FlywheelConfigs.flywheelConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        m_flywheelM.configure(FlywheelConfigs.flywheelConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        m_flywheelR.configure(flywheelRConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        setFlywheelSpeed(0);  // Ensure flywheel is stopped on initialization
        lastFlywheelSpeed = 0;
    }

    @Override
    public void periodic() {
        int speedIndex = (int) Math.round(swerve.getDistanceToHub() * 4);
        goalFlywheelSpeed = speedIndex < 24 ? FlywheelConstants.kFlywheelSpeeds[speedIndex] : FlywheelConstants.kFlywheelSpeeds[24];

        if(flywheelRunning) {
            if(goalFlywheelSpeed != lastFlywheelSpeed) { // Only update the flywheel speed if it has changed to avoid unnecessary CAN traffic
                setFlywheelSpeed(goalFlywheelSpeed);
                lastFlywheelSpeed = goalFlywheelSpeed;
            }
        } else {
            if(lastFlywheelSpeed != 0) { // Only stop the flywheel if it was previously running to avoid unnecessary CAN traffic
                setFlywheelSpeed(0);
                lastFlywheelSpeed = 0;
            }
        }

        SmartDashboard.putNumber("Goal Flywheel Speed", goalFlywheelSpeed);
        SmartDashboard.putNumber("Speed Index", speedIndex);
        SmartDashboard.putNumber("Current Flywheel Speed", m_encoderL.getVelocity());
        SmartDashboard.putNumber("Distance to Hub", swerve.getDistanceToHub());
    }

    private void setFlywheelSpeed(int speed) {
        if(speed == 0) {
            m_flywheelL.set(0);
            m_flywheelM.set(0);
            m_flywheelR.set(0);
        } else {
            m_pidControllerL.setSetpoint(speed, ControlType.kVelocity);
            m_pidControllerM.setSetpoint(speed, ControlType.kVelocity);
            m_pidControllerR.setSetpoint(speed, ControlType.kVelocity);
        }
    }
}