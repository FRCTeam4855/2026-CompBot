package frc.robot;

import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import frc.robot.Constants.ConveyorConstants;
import frc.robot.Constants.FlywheelConstants;
import frc.robot.Constants.IndexerConstants;
import frc.robot.Constants.IntakeConstants;

public final class Configs {

    public static final class intakeConfigs {
        public static final SparkMaxConfig intakeLeaderConfig = new SparkMaxConfig();
        public static final SparkMaxConfig intakeFollowerConfig = new SparkMaxConfig();
        private static final SparkMaxConfig globalConfig = new SparkMaxConfig();
        public static final SparkFlexConfig intakeAngleConfig = new SparkFlexConfig();
        static {
            globalConfig
                .smartCurrentLimit(55)
                .idleMode(IdleMode.kCoast);
            intakeLeaderConfig
                //.smartCurrentLimit(55)
                //.idleMode(IdleMode.kCoast)
                .apply(globalConfig)
                .inverted(false);
            intakeLeaderConfig.closedLoop
                .pid(IntakeConstants.kIntakeP, IntakeConstants.kIntakeI, IntakeConstants.kIntakeD)
                .feedForward.kV(0.00205);

            intakeAngleConfig
                .smartCurrentLimit(80)
                .idleMode(IdleMode.kBrake);
            intakeAngleConfig.closedLoop
                .pid(IntakeConstants.kIntakeAngleP, IntakeConstants.kIntakeAngleI, IntakeConstants.kIntakeAngleD)
                .feedbackSensor(FeedbackSensor.kAbsoluteEncoder)
                .positionWrappingEnabled(true)
                .positionWrappingInputRange(0, 1)
                .outputRange(-0.5, 1)
                .feedForward
                    .kS(IntakeConstants.kIntakeAngleFFS)
                    .kV(IntakeConstants.kIntakeAngleFFV) //0.5
                    .kA(IntakeConstants.kIntakeAngleFFA) //0.05
                    .kG(IntakeConstants.kIntakeAngleFFG)
                    .kCos(IntakeConstants.kIntakeAngleFFCos)
                    .kCosRatio(1);
            // intakeAngleConfig.closedLoop.maxMotion
            //     .cruiseVelocity(15)
            //     .maxAcceleration(5)
            //     .allowedProfileError(1);
            intakeFollowerConfig
                //.smartCurrentLimit(55)
                //.idleMode(IdleMode.kCoast)
                .apply(globalConfig)
                .follow(IntakeConstants.kIntakeLeaderCanid, true);

            intakeAngleConfig.absoluteEncoder
                .inverted(false)
                .positionConversionFactor(1);
        }
    }

    public static final class FlywheelConfigs {
        public static final SparkFlexConfig flywheelConfig = new SparkFlexConfig();
        public static final SparkFlexConfig flywheelConfigR = new SparkFlexConfig();
        static {
            flywheelConfig
                .smartCurrentLimit(40)
                .idleMode(IdleMode.kCoast);
            flywheelConfig.closedLoop
                .pid(FlywheelConstants.kFlywheelP, FlywheelConstants.kFlywheelI, FlywheelConstants.kFlywheelD)
                .outputRange(0, 1.0)
                .feedForward
                    .kS(.011)
                    .kV(0.0018);
        }
    }

    public static final class IndexerConfigs {
        public static final SparkMaxConfig indexerConfig = new SparkMaxConfig();
        static {
            indexerConfig
                .smartCurrentLimit(40)
                .idleMode(IdleMode.kBrake);
            indexerConfig.closedLoop
                .pid(IndexerConstants.kIndexerP, IndexerConstants.kIndexerI, IndexerConstants.kIndexerD)
                .feedForward.kV(0.0023);
                
        }
    }

    public static final class ConveyorConfigs {
        public static final SparkMaxConfig conveyorConfig = new SparkMaxConfig();
        public static final SparkMaxConfig elevatorConfig = new SparkMaxConfig();
        static {
            conveyorConfig
                .smartCurrentLimit(50)
                .idleMode(IdleMode.kBrake);
            conveyorConfig.closedLoop 
                .pid(ConveyorConstants.kConveyorP, ConveyorConstants.kConveyorI, ConveyorConstants.kConveyorD)
                .feedForward.kV(0.00223);

            elevatorConfig
                .smartCurrentLimit(80)
                .idleMode(IdleMode.kBrake);
            elevatorConfig.closedLoop
                .pid(ConveyorConstants.kElevatorP, ConveyorConstants.kElevatorI, ConveyorConstants.kElevatorD)
                .feedForward.kV(0.0024);
        }
    }
}