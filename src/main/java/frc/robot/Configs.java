package frc.robot;

import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import frc.robot.Constants.ClimberConstants;
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
        static {
            flywheelConfig
                .idleMode(IdleMode.kBrake);
            flywheelConfig.closedLoop
                .pid(FlywheelConstants.kFlywheelP, FlywheelConstants.kFlywheelI, FlywheelConstants.kFlywheelD);
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