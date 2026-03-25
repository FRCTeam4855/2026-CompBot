package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkAbsoluteEncoder;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import frc.robot.Configs.IntakeConfigs;
import frc.robot.Constants.IntakeConstants;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class IntakeSubsystem extends Subsystem {

    public final SparkMax m_intakeMotor;
    public final SparkFlex m_intakeAngleMotor;
    public final SparkClosedLoopController intakePIDController, anglePIDController;
    public final SparkAbsoluteEncoder m_encoder;
    private Timer timer;
    public boolean intakeRunning = false, intakeDeployed = false;

    private static IntakeSubsystem mInstance;

    public static IntakeSubsystem getInstance() {
        if (mInstance == null) {
            mInstance = new IntakeSubsystem();
        }
        return mInstance;
    }

    @Override
    public void robotInit() {
        DataLogManager.log("IntakeSubsystem in robotInit");
    }

    @Override
    public void teleopInit() {
        DataLogManager.log("IntakeSubsystem in teleopInit");
    }

    @Override
    public void autonomousInit() {
        DataLogManager.log("IntakeSubsystem in autonomousInit");
    }

    public IntakeSubsystem() {
        m_intakeMotor = new SparkMax(IntakeConstants.kIntakeCanId, MotorType.kBrushless);
        m_intakeAngleMotor = new SparkFlex(IntakeConstants.kIntakeAngleCanId, MotorType.kBrushless);
        intakePIDController = m_intakeMotor.getClosedLoopController();
        anglePIDController = m_intakeAngleMotor.getClosedLoopController();
        m_encoder = m_intakeAngleMotor.getAbsoluteEncoder();

        m_intakeMotor.configure(IntakeConfigs.intakeConfig, ResetMode.kResetSafeParameters,
                PersistMode.kPersistParameters);
        m_intakeAngleMotor.configure(IntakeConfigs.intakeAngleConfig, ResetMode.kResetSafeParameters,
                PersistMode.kPersistParameters);
        timer = new Timer();
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Intake Position", m_encoder.getPosition());
        SmartDashboard.putNumber("Intake Arm Velocity", m_encoder.getVelocity());
        
        if (isBlocked()) {
            timer.start();
            if (timer.hasElapsed(1)) {
                if (isBlocked()) {
                    setIntakePosition(IntakeConstants.kIntakeAgitatePosition);
                    if (timer.hasElapsed(1.5)) {
                        setIntakePosition(IntakeConstants.kIntakeExtendPosition);
                        timer.reset();
                    }
                }
            }
        }
    }

    public boolean isBlocked() {
        return anglePIDController.getSetpoint() == IntakeConstants.kIntakeExtendPosition && Math.abs(anglePIDController.getSetpoint() - m_encoder.getPosition()) > 0.1;
    }
    public void toggleIntakePosition() {
        System.out.printf("Entered positionIntake\n");
        if (intakeDeployed) {
            System.out.printf("Retracting Intake\n");
            setIntakePosition(IntakeConstants.kIntakeRetractPosition);
            intakeDeployed = false;
        } else {
            System.out.printf("Extending Intake\n");
            setIntakePosition(IntakeConstants.kIntakeExtendPosition);
            intakeDeployed = true;
        }
    }

    public void setIntakePosition(double position) {
        anglePIDController.setSetpoint(position, ControlType.kPosition);
    }

    public void intakeToggle(double speed) {
        if (intakeRunning) {
            m_intakeMotor.set(0);
            intakeRunning = false;
        } else {
            intakePIDController.setSetpoint(speed, ControlType.kVelocity);
            intakeRunning = true;
        }
    }

    public void intakeStop() {
        m_intakeMotor.set(0);
        intakeRunning = false;
    }

    public void intakeForward() {
        intakePIDController.setSetpoint(IntakeConstants.kIntakeSpeed, ControlType.kVelocity);
        intakeRunning = true;
    }

    public void intakeReverse() {
        intakePIDController.setSetpoint(-IntakeConstants.kIntakeSpeed, ControlType.kVelocity);
        intakeRunning = true;
    }

    public void intakeDeploySequence() {
        intakePIDController.setSetpoint(IntakeConstants.kIntakeSpeed, ControlType.kVelocity);
        setIntakePosition(IntakeConstants.kIntakeExtendPosition);
        intakeRunning = true;
        intakeDeployed = true;
    }

    public void intakeRetractSequence() {
        m_intakeMotor.set(0);
        setIntakePosition(IntakeConstants.kIntakeRetractPosition);
        intakeRunning = false;
        intakeDeployed = false;
    }
}