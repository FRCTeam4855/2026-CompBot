package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import frc.robot.Constants.FlywheelConstants;
import frc.robot.Configs.FlywheelConfigs;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class FlywheelSubsystem extends SubsystemBase {
    
    public final SparkFlex m_flywheelL, m_flywheelM, m_flywheelR;
    public final SparkClosedLoopController m_PIDControllerL, m_PIDControllerM, m_PIDControllerR;
    public final RelativeEncoder m_encoderL, m_encoderM, m_encoderR;
    public boolean runFlywheel = false;
    
    public FlywheelSubsystem() {
        m_flywheelL = new SparkFlex(FlywheelConstants.kflywheelLCanId, MotorType.kBrushless); 
        m_flywheelM = new SparkFlex(FlywheelConstants.kflywheelMCanId, MotorType.kBrushless); 
        m_flywheelR = new SparkFlex(FlywheelConstants.kflywheelRCanId, MotorType.kBrushless); 
        m_PIDControllerL = m_flywheelL.getClosedLoopController();
        m_PIDControllerM = m_flywheelM.getClosedLoopController();
        m_PIDControllerR = m_flywheelR.getClosedLoopController();
        m_encoderL = m_flywheelL.getEncoder();
        m_encoderM = m_flywheelM.getEncoder();
        m_encoderR = m_flywheelR.getEncoder();


        m_flywheelL.configure(FlywheelConfigs.flywheelConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    @Override
    public void periodic() {
        
    }

    public void runFlywheel() {
        
    }
}