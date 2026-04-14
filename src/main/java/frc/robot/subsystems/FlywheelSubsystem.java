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

import com.revrobotics.spark.SparkBase.ControlType;

public class FlywheelSubsystem extends Subsystem {

    public final SparkFlex m_flywheelL, m_flywheelM, m_flywheelR;
    public final SparkClosedLoopController m_pidControllerL, m_pidControllerM, m_pidControllerR;
    public final RelativeEncoder m_encoderL, m_encoderM, m_encoderR;
    public boolean flywheelRunning = false, flywheelUpToSpeed = false, delieverSpeed = false, overrideUpToSpeed = false;
    public int goalFlywheelSpeed = 0, flywheelAdjustment = 0;
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
                .apply(FlywheelConfigs.flywheelConfig) // Applies all of the flywheelConfig config settings into the
                                                       // private config
                .inverted(true); // Adds the inverted configuration setting

        m_flywheelL.configure(FlywheelConfigs.flywheelConfig, ResetMode.kResetSafeParameters,
                PersistMode.kPersistParameters);
        m_flywheelM.configure(FlywheelConfigs.flywheelConfig, ResetMode.kResetSafeParameters,
                PersistMode.kPersistParameters);
        m_flywheelR.configure(flywheelRConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        setFlywheelSpeed(0); // Ensure flywheel is stopped on initialization
        lastFlywheelSpeed = 0;
    }

    @SuppressWarnings("unused")
    @Override
    public void periodic() {
        double distanceToHub = swerve.getCachedDistanceToHub();
        int speedIndex = (int) Math.round(distanceToHub * 8);
        goalFlywheelSpeed = (speedIndex < 47 ? FlywheelConstants.kFlywheelSpeeds[speedIndex]
                : FlywheelConstants.kFlywheelSpeeds[47]) + flywheelAdjustment;
        if (FlywheelConstants.kFlywheelTestOverrideSpeed > 0) {
            goalFlywheelSpeed = FlywheelConstants.kFlywheelTestOverrideSpeed;
        } else if (delieverSpeed) {
            goalFlywheelSpeed = FlywheelConstants.kFlywheelDelieverSpeed;
        } else if (overrideUpToSpeed) {
            goalFlywheelSpeed = FlywheelConstants.kFlywheelSpeeds[23];
        }

        if (flywheelRunning) {
            if (goalFlywheelSpeed != lastFlywheelSpeed) { // Only update the flywheel speed if it has changed to avoid
                                                          // unnecessary CAN traffic
                setFlywheelSpeed(goalFlywheelSpeed);
                lastFlywheelSpeed = goalFlywheelSpeed;
            }
            // Use the average of all three encoders to determine whether the flywheel is up
            // to speed to get a more accurate reading and reduce the impact of any one
            // encoder having a bad reading
            flywheelUpToSpeed = (((m_encoderL.getVelocity() + m_encoderM.getVelocity() + m_encoderR.getVelocity())
                    / 3.0) >= goalFlywheelSpeed * FlywheelConstants.kFlywheelTolerance);
        } else {
            if (lastFlywheelSpeed != 0) { // Only stop the flywheel if it was previously running to avoid unnecessary
                                          // CAN traffic
                setFlywheelSpeed(0);
                lastFlywheelSpeed = 0;
                flywheelUpToSpeed = false;
            }
        }

        SmartDashboard.putNumber("Goal Flywheel Speed", goalFlywheelSpeed);
        SmartDashboard.putNumber("Speed Index", speedIndex);
        SmartDashboard.putNumber("Current Flywheel Speed", m_encoderL.getVelocity());
        SmartDashboard.putNumber("Distance to Hub", distanceToHub);
        SmartDashboard.putNumber("Flywheel Adjustment", flywheelAdjustment);
        SmartDashboard.putBoolean("Flywheel Override", overrideUpToSpeed);
    }

    private void setFlywheelSpeed(int speed) {
        if (speed == 0) {
            m_flywheelL.set(0);
            m_flywheelM.set(0);
            m_flywheelR.set(0);
        } else {
            m_pidControllerL.setSetpoint(speed, ControlType.kVelocity);
            m_pidControllerM.setSetpoint(speed, ControlType.kVelocity);
            m_pidControllerR.setSetpoint(speed, ControlType.kVelocity);
        }
    }

    public void toggleDelieverSpeed() {
        delieverSpeed = !delieverSpeed;
    }

    public void incrementFlywheelSpeed(int adjustment) {
        flywheelAdjustment += adjustment;
    }

    public void decrementFlywheelSpeed(int adjustment) {
        flywheelAdjustment -= adjustment;
    }

    public void toggleOverride() {
        overrideUpToSpeed = !overrideUpToSpeed;
    }

    public void setOverride(boolean override) {
        overrideUpToSpeed = override;
    }
}