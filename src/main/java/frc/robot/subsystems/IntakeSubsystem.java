package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkAbsoluteEncoder;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import frc.robot.Configs.IntakeConfigs;
import frc.robot.Constants.IntakeConstants;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeSubsystem extends SubsystemBase {

    public final SparkMax m_intakeMotor, m_intakeAngleMotor;
    public final SparkClosedLoopController intakePIDController, anglePIDController;
    public final SparkAbsoluteEncoder m_encoder;
    public boolean intakeRunning = false, intakeDeployed = false;

    public IntakeSubsystem() {
        m_intakeMotor = new SparkMax(IntakeConstants.kIntakeCanId, MotorType.kBrushless); 
        m_intakeAngleMotor = new SparkMax(IntakeConstants.kIntakeAngleCanId, MotorType.kBrushless);
        intakePIDController = m_intakeMotor.getClosedLoopController();
        anglePIDController = m_intakeAngleMotor.getClosedLoopController();
        m_encoder = m_intakeAngleMotor.getAbsoluteEncoder();

        m_intakeMotor.configure(IntakeConfigs.intakeConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        m_intakeAngleMotor.configure(IntakeConfigs.intakeAngleConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public void positionIntake() {
        if (intakeDeployed) {
            anglePIDController.setSetpoint(IntakeConstants.kIntakeRetractPosition, ControlType.kDutyCycle);
            intakeDeployed = false;
        } else {
            anglePIDController.setSetpoint(IntakeConstants.kIntakeExtendPosition, ControlType.kDutyCycle);
            intakeDeployed = true;
        }
    }

    public void runIntake(double speed) {
        if (intakeRunning) {
            m_intakeMotor.set(0);
            intakeRunning = false;
        } else {
            m_intakeMotor.set(speed);
            intakeRunning = true;
        }
    }

    public void intakeSequence(double speed) {
        if (intakeDeployed) {
            m_intakeMotor.set(0);
            anglePIDController.setSetpoint(IntakeConstants.kIntakeRetractPosition, ControlType.kDutyCycle);
            intakeRunning = false;
            intakeDeployed = false;
        } else {
            m_intakeMotor.set(speed);
            anglePIDController.setSetpoint(IntakeConstants.kIntakeExtendPosition, ControlType.kDutyCycle);
            intakeRunning = true;
            intakeDeployed = true;
        }
    }
}