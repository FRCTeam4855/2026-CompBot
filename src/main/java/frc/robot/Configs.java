package frc.robot;

import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import frc.robot.Constants.ClimberConstants;
import frc.robot.Constants.ConveyorConstants;
import frc.robot.Constants.FlywheelConstants;
import frc.robot.Constants.IntakeConstants;

public final class Configs {

    public static final class IntakeConfigs {
        public static final SparkMaxConfig intakeConfig = new SparkMaxConfig();
        public static final SparkFlexConfig intakeAngleConfig = new SparkFlexConfig();
        static {
            intakeConfig
                .idleMode(IdleMode.kBrake);
            intakeConfig.closedLoop
                .pid(IntakeConstants.kIntakeP, IntakeConstants.kIntakeI, IntakeConstants.kIntakeD)
                .feedForward.kV(0.00205);

            intakeAngleConfig
                .idleMode(IdleMode.kBrake);
            intakeAngleConfig.closedLoop
                .pid(IntakeConstants.kIntakeAngleP, IntakeConstants.kIntakeAngleI, IntakeConstants.kIntakeAngleD)
                .feedbackSensor(FeedbackSensor.kAbsoluteEncoder)
                .positionWrappingEnabled(true)
                .positionWrappingInputRange(0, 1)
                .outputRange(-0.5, 0.5)
                .feedForward
                    .kS(0.02)
                    .kV(0)
                    .kA(0)
                    .kG(0)
                    .kCos(.74)
                    .kCosRatio(1/360);
            intakeAngleConfig.absoluteEncoder
                .inverted(false)
                .positionConversionFactor(360);
        }
    }

    public static final class FlywheelConfigs {
        public static final SparkFlexConfig flywheelConfig = new SparkFlexConfig();
        public static final SparkFlexConfig flywheelConfigR = new SparkFlexConfig();
        public static final SparkMaxConfig indexerConfig = new SparkMaxConfig();
        static {
            flywheelConfig
                .idleMode(IdleMode.kCoast);
            flywheelConfig.closedLoop
                .pid(FlywheelConstants.kFlywheelP, FlywheelConstants.kFlywheelI, FlywheelConstants.kFlywheelD)
                .outputRange(0, 1.0)
                .feedForward
                    .kS(.011)
                    .kV(0.0018);

            indexerConfig
                .idleMode(IdleMode.kBrake);
            indexerConfig.closedLoop
                .pid(FlywheelConstants.kIndexerP, FlywheelConstants.kIndexerI, FlywheelConstants.kIndexerD)
                .feedForward.kV(0.0023);
                
        }
    }

    public static final class ConveyorConfigs {
        public static final SparkMaxConfig conveyorConfig = new SparkMaxConfig();
        public static final SparkMaxConfig elevatorConfig = new SparkMaxConfig();
        static {
            conveyorConfig
                .idleMode(IdleMode.kBrake);
            conveyorConfig.closedLoop 
                .pid(ConveyorConstants.kConveyorP, ConveyorConstants.kConveyorI, ConveyorConstants.kConveyorD)
                .feedForward.kV(0.00223);

            elevatorConfig
                .idleMode(IdleMode.kBrake);
            elevatorConfig.closedLoop
                .pid(ConveyorConstants.kElevatorP, ConveyorConstants.kElevatorI, ConveyorConstants.kElevatorD)
                .feedForward.kV(0.0024);
        }
    }
    

    public static final class ClimberConfigs {
        public static final SparkMaxConfig climberConfig = new SparkMaxConfig();
        static {
            climberConfig
                .idleMode(IdleMode.kBrake)
                .smartCurrentLimit(ClimberConstants.kClimberCurrentLimit);
            climberConfig.closedLoop
                .pid(ClimberConstants.kClimberP, ClimberConstants.kClimberI, ClimberConstants.kClimberD);
        }
    }
}