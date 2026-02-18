package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import frc.robot.Configs.IntakeConfigs;
import frc.robot.Constants.IntakeConstants;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
    
    public final SparkMax m_intakeMotor, m_intakeAngleMotor;
    public final SparkClosedLoopController intakePIDController, anglePIDController;

    public Intake() {
        m_intakeMotor = new SparkMax(IntakeConstants.kIntakeCanId, MotorType.kBrushless);
        m_intakeAngleMotor = new SparkMax(IntakeConstants.kIntakeAngleCanId, MotorType.kBrushless);
        intakePIDController = m_intakeMotor.getClosedLoopController();
        anglePIDController = m_intakeAngleMotor.getClosedLoopController();

        m_intakeMotor.configure(IntakeConfigs.intakeConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        m_intakeAngleMotor.configure(IntakeConfigs.intakeAngleConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }
}
