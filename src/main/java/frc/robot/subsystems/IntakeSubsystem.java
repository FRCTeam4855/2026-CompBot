package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkAbsoluteEncoder;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import frc.robot.Constants;
import frc.robot.Configs.intakeConfigs;
import frc.robot.Constants.IntakeConstants;
import frc.robot.commands.IntakeDownCommand;
import frc.robot.commands.IntakeToggleCommand;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class IntakeSubsystem extends Subsystem {

    public final SparkMax m_intakeLeaderMotor;
    public final SparkMax m_intakeFollowerMotor;
    public final SparkFlex m_intakeAngleMotor;
    public final SparkClosedLoopController intakePIDController, anglePIDController;
    public final SparkAbsoluteEncoder m_encoder;
    private Timer timer;
    public boolean intakeRunning = false, intakeDeployed = false, clearingBlock = false;
    private SparkFlexConfig updatedIntakeAngleConfig = new SparkFlexConfig();

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
        m_intakeLeaderMotor = new SparkMax(IntakeConstants.kIntakeLeaderCanid, MotorType.kBrushless);
        m_intakeFollowerMotor = new SparkMax(IntakeConstants.kIntakeFollowerCanid, MotorType.kBrushless);
        m_intakeAngleMotor = new SparkFlex(IntakeConstants.kIntakeAngleCanId, MotorType.kBrushless);
        intakePIDController = m_intakeLeaderMotor.getClosedLoopController();
        anglePIDController = m_intakeAngleMotor.getClosedLoopController();
        m_encoder = m_intakeAngleMotor.getAbsoluteEncoder();

        m_intakeLeaderMotor.configure(intakeConfigs.intakeLeaderConfig, ResetMode.kResetSafeParameters,
                PersistMode.kPersistParameters);
        m_intakeFollowerMotor.configure(intakeConfigs.intakeFollowerConfig, ResetMode.kResetSafeParameters,
                PersistMode.kPersistParameters);
        timer = new Timer();
        m_intakeAngleMotor.configure(intakeConfigs.intakeAngleConfig, ResetMode.kResetSafeParameters,
                PersistMode.kPersistParameters);
        if (IntakeConstants.kIntakeDebug) {
            SmartDashboard.putNumber("Intake arm P", IntakeConstants.kIntakeAngleP);
            SmartDashboard.putNumber("Intake arm I", IntakeConstants.kIntakeAngleI);
            SmartDashboard.putNumber("Intake arm D", IntakeConstants.kIntakeAngleD);
            SmartDashboard.putNumber("Intake arm FF S", IntakeConstants.kIntakeAngleFFS);
            SmartDashboard.putNumber("Intake arm FF V", IntakeConstants.kIntakeAngleFFV);
            SmartDashboard.putNumber("Intake arm FF A", IntakeConstants.kIntakeAngleFFA);
            SmartDashboard.putNumber("Intake arm FF G", IntakeConstants.kIntakeAngleFFG);
            SmartDashboard.putNumber("Intake arm FF Cos", IntakeConstants.kIntakeAngleFFCos);
            SmartDashboard.putNumber("Intake speed", IntakeConstants.kIntakeSpeed);
        }
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Intake Position", m_encoder.getPosition());
        SmartDashboard.putNumber("Intake Arm Velocity", m_encoder.getVelocity());
        SmartDashboard.putBoolean("Intake Blocked", isBlocked());
        // if (isBlocked() || clearingBlock) {
        //     if (!clearingBlock) {
        //         clearingBlock = true;
        //         timer.reset();
        //         timer.start();
        //     }
        //     if (timer.hasElapsed(1)) {
        //         if (isBlocked() || anglePIDController.getSetpoint() == IntakeConstants.kIntakeAgitatePosition) { TODO
        //             if (anglePIDController.getSetpoint() != IntakeConstants.kIntakeAgitatePosition)
        //                 setIntakePosition(IntakeConstants.kIntakeAgitatePosition);
        //             if (timer.hasElapsed(1.5)) {
        //                 setIntakePosition(IntakeConstants.kIntakeExtendPosition);
        //                 clearingBlock = false;
        //                 timer.stop();
        //             }
        //         } else {
        //             clearingBlock = false;
        //             timer.stop();
        //         }
        //     }
        // }
    }

    public boolean isBlocked() {
        return anglePIDController.getSetpoint() == IntakeConstants.kIntakeExtendPosition && Math.abs(anglePIDController.getSetpoint() - m_encoder.getPosition()) > 0.1;
    }

    public void toggleIntakePosition() {
        System.out.printf("Entered positionIntake\n");
        if (intakeDeployed) {
            System.out.printf("Retracting Intake\n");

            if (Constants.IntakeConstants.kIntakeDebug) {
                updatedIntakeAngleConfig.closedLoop
                    .pid(SmartDashboard.getNumber("Intake arm P", IntakeConstants.kIntakeAngleP), SmartDashboard.getNumber("Intake arm I", IntakeConstants.kIntakeAngleI),            SmartDashboard.getNumber("Intake arm D", IntakeConstants.kIntakeAngleD))
                    .feedForward
                        .kS(SmartDashboard.getNumber("Intake arm FF S", IntakeConstants.kIntakeAngleFFS))
                        .kV(SmartDashboard.getNumber("Intake arm FF V", IntakeConstants.kIntakeAngleFFV))
                        .kA(SmartDashboard.getNumber("Intake arm FF A", IntakeConstants.kIntakeAngleFFA))
                        .kG(SmartDashboard.getNumber("Intake arm FF G", IntakeConstants.kIntakeAngleFFG))
                        .kCos(SmartDashboard.getNumber("Intake arm FF Cos", IntakeConstants.kIntakeAngleFFCos));

                m_intakeAngleMotor.configure(updatedIntakeAngleConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
            }

            anglePIDController.setSetpoint(IntakeConstants.kIntakeRetractPosition, ControlType.kPosition);
            intakeDeployed = false;
        } else {
            System.out.printf("Extending Intake\n");
            
            if (Constants.IntakeConstants.kIntakeDebug) {
                updatedIntakeAngleConfig.closedLoop
                    .pid(SmartDashboard.getNumber("Intake arm P", IntakeConstants.kIntakeP), SmartDashboard.getNumber("Intake arm I", IntakeConstants.kIntakeI),            SmartDashboard.getNumber("Intake arm D", IntakeConstants.kIntakeD))
                    .feedForward
                        .kS(SmartDashboard.getNumber("Intake arm FF S", IntakeConstants.kIntakeAngleFFS))
                        .kV(SmartDashboard.getNumber("Intake arm FF V", IntakeConstants.kIntakeAngleFFV))
                        .kA(SmartDashboard.getNumber("Intake arm FF A", IntakeConstants.kIntakeAngleFFA))
                        .kG(SmartDashboard.getNumber("Intake arm FF G", IntakeConstants.kIntakeAngleFFG))
                        .kCos(SmartDashboard.getNumber("Intake arm FF Cos", IntakeConstants.kIntakeAngleFFCos));

                m_intakeAngleMotor.configure(updatedIntakeAngleConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
            }

            CommandScheduler.getInstance().schedule(new IntakeDownCommand(mInstance));
            intakeDeployed = true;
        }
    }

    public void setIntakePosition(double position) {
        anglePIDController.setSetpoint(position, ControlType.kPosition);
    }

    public void intakeToggle() {
        if (intakeRunning) {
            intakeStop();
            intakeRunning = false;
        } else {
            intakeForward();
            intakeRunning = true;
        }
    }

    public void intakeStop() {
        m_intakeLeaderMotor.set(0);
        intakeRunning = false;
    }

    public void intakeForward() {
        intakePIDController.setSetpoint(IntakeConstants.kIntakeDebug ? SmartDashboard.getNumber("Intake Speed", IntakeConstants.kIntakeSpeed)
            : IntakeConstants.kIntakeSpeed, ControlType.kVelocity);
        intakeRunning = true;
    }

    // public void intakeForward() {
    //     intakePIDController.setSetpoint(IntakeConstants.kIntakeSpeed, ControlType.kVelocity);
    //     intakeRunning = true;
    // }

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
        m_intakeLeaderMotor.set(0);
        setIntakePosition(IntakeConstants.kIntakeRetractPosition);
        intakeRunning = false;
        intakeDeployed = false;
    }
}