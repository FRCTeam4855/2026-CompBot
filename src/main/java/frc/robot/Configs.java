package frc.robot;

import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.FeedbackSensor;

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
}