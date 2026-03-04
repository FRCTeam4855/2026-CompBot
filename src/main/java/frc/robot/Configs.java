package frc.robot;

import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import frc.robot.Constants.ClimberConstants;
import frc.robot.Constants.ConveyorConstants;
import frc.robot.Constants.FlywheelConstants;
import frc.robot.Constants.IntakeConstants;

public final class Configs {

    public static final class IntakeConfigs {
        public static final SparkMaxConfig intakeConfig = new SparkMaxConfig();
        public static final SparkMaxConfig intakeAngleConfig = new SparkMaxConfig();
        static {
            intakeConfig
                .idleMode(IdleMode.kBrake);
            intakeConfig.closedLoop
                .pid(IntakeConstants.kIntakeP, IntakeConstants.kIntakeI, IntakeConstants.kIntakeD);

            intakeAngleConfig
                .idleMode(IdleMode.kBrake);
            intakeAngleConfig.closedLoop
                .pid(IntakeConstants.kIntakeAngleP, IntakeConstants.kIntakeAngleI, IntakeConstants.kIntakeAngleD);
            intakeAngleConfig.absoluteEncoder
                .inverted(false);
        }
    }

    public static final class FlywheelConfigs {
        public static final SparkMaxConfig flywheelConfig = new SparkMaxConfig();
        public static final SparkMaxConfig indexerConfig = new SparkMaxConfig();
        static {
            flywheelConfig
                .idleMode(IdleMode.kBrake);
            flywheelConfig.closedLoop
                .pid(FlywheelConstants.kFlywheelP, FlywheelConstants.kFlywheelI, FlywheelConstants.kFlywheelD);

            indexerConfig
                .idleMode(IdleMode.kBrake);
            indexerConfig.closedLoop
                .pid(FlywheelConstants.kIndexerP, FlywheelConstants.kIndexerI, FlywheelConstants.kIndexerD);
        }
    }

    public static final class ConveyorConfigs {
        public static final SparkMaxConfig conveyorConfig = new SparkMaxConfig();
        public static final SparkMaxConfig elevatorConfig = new SparkMaxConfig();
        static {
            conveyorConfig
                .idleMode(IdleMode.kBrake);
            conveyorConfig.closedLoop 
                .pid(ConveyorConstants.kConveyorP, ConveyorConstants.kConveyorI, ConveyorConstants.kConveyorD);

            elevatorConfig
                .idleMode(IdleMode.kBrake);
            elevatorConfig.closedLoop
                .pid(ConveyorConstants.kElevatorP, ConveyorConstants.kElevatorI, ConveyorConstants.kElevatorD);
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