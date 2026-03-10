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
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Degree;
import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.DegreesPerSecond;
import static edu.wpi.first.units.Units.DegreesPerSecondPerSecond;
import static edu.wpi.first.units.Units.Seconds;
import static edu.wpi.first.units.Units.Feet;
import static edu.wpi.first.units.Units.Pounds;
import static edu.wpi.first.units.Units.Radians;
import static edu.wpi.first.units.Units.Second;
import static edu.wpi.first.units.Units.Seconds;
import static edu.wpi.first.units.Units.Volts;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import yams.gearing.GearBox;
import yams.gearing.MechanismGearing;
import yams.mechanisms.SmartMechanism;
import yams.mechanisms.config.ArmConfig;
import yams.mechanisms.positional.Arm;
import yams.motorcontrollers.SmartMotorControllerConfig;
import yams.motorcontrollers.SmartMotorControllerConfig.ControlMode;
import yams.motorcontrollers.SmartMotorControllerConfig.MotorMode;
import yams.motorcontrollers.SmartMotorControllerConfig.TelemetryVerbosity;
import yams.motorcontrollers.SmartMotorController;
import yams.motorcontrollers.local.SparkWrapper;

public class IntakeSubsystem extends SubsystemBase {

  //   private final SparkFlex spark = new SparkFlex(10, MotorType.kBrushless);

  //   private SmartMotorControllerConfig smcConfig = new SmartMotorControllerConfig(this)
  //   .withControlMode(ControlMode.CLOSED_LOOP)
  //   // Feedback Constants (PID Constants)
  //   .withClosedLoopController(4, 0, 0, DegreesPerSecond.of(90), DegreesPerSecondPerSecond.of(45))
  //   // Feedforward Constants
  //   .withFeedforward(new ArmFeedforward(0, 0, 0))
  //   // Telemetry name and verbosity level
  //   .withTelemetry("ArmMotor", TelemetryVerbosity.HIGH)
  //   // Gearing from the motor rotor to final shaft.
  //   .withGearing(new MechanismGearing(GearBox.fromReductionStages(20)))
  //   // Motor properties to prevent over currenting.
  //   .withMotorInverted(false)
  //   .withIdleMode(MotorMode.BRAKE)
  //   .withStatorCurrentLimit(Amps.of(10))
  //   .withClosedLoopRampRate(Seconds.of(0.25))
  //   .withOpenLoopRampRate(Seconds.of(0.25))
  //   .withExternalEncoder(spark.getAbsoluteEncoder())
  //   .withExternalEncoderInverted(false)
  //   .withExternalEncoderGearing(1)
  //   .withExternalEncoderZeroOffset(Radians.of(0))
  //   .withSoftLimit(Degrees.of(-60), Degrees.of(60))
  //   .withUseExternalFeedbackEncoder(true);

  //   // Vendor motor controller object


  //   // Create our SmartMotorController from our Spark and config with the NEO.
  //   private SmartMotorController sparkSmartMotorController = new SparkWrapper(spark, DCMotor.getNEO(1), smcConfig);


  // private ArmConfig armCfg = new ArmConfig(sparkSmartMotorController)
  // .withStartingPosition(Degrees.of(0)) // Parallel to the ground
  // .withHorizontalZero(Degrees.of(0)) // Parallel to the ground at 0deg
  // // Starting position is where your arm starts
  // // Length and mass of your arm for sim.
  // .withLength(Feet.of(1.33))
  // .withMass(Pounds.of(5.1))
  // // Telemetry name and verbosity for the arm.
  // .withTelemetry("Arm", TelemetryVerbosity.HIGH);

  // // Arm Mechanism
  // private Arm arm = new Arm(armCfg);

  // /**
  //  * Set the angle of the arm, does not stop when the arm reaches the setpoint.
  //  * @param angle Angle to go to.
  //  * @return A command.
  //  */
  // public Command setAngle(Angle angle) { return arm.run(angle);}
  
  // /**
  //  * Set the angle of the arm, ends the command but does not stop the arm when the arm reaches the setpoint.
  //  * @param angle Angle to go to.
  //  * @param tolerance Angle tolerance for completion.
  //  * @return A Command
  //  */
  // public Command setAngleAndStop(Angle angle, Angle tolerance) { return arm.runTo(angle, tolerance);}
  
  // /**
  //  * Set arm closed loop controller to go to the specified mechanism position.
  //  * @param angle Angle to go to.
  //  */
  // public void setAngleSetpoint(Angle angle) { arm.setMechanismPositionSetpoint(angle); }

  // /**
  //  * Move the arm up and down.
  //  * @param dutycycle [-1, 1] speed to set the arm too.
  //  */
  // public Command set(double dutycycle) { return arm.set(dutycycle);}


    public final SparkMax m_intakeMotor;
    public final SparkFlex m_intakeAngleMotor;
    public final SparkClosedLoopController intakePIDController, anglePIDController;
    public final SparkAbsoluteEncoder m_encoder;
    public boolean intakeRunning = false, intakeDeployed = false;

    public IntakeSubsystem() {
        m_intakeMotor = new SparkMax(IntakeConstants.kIntakeCanId, MotorType.kBrushless); 
        m_intakeAngleMotor = new SparkFlex(IntakeConstants.kIntakeAngleCanId, MotorType.kBrushless);
        intakePIDController = m_intakeMotor.getClosedLoopController();
        anglePIDController = m_intakeAngleMotor.getClosedLoopController();
        m_encoder = m_intakeAngleMotor.getAbsoluteEncoder();

        m_intakeMotor.configure(IntakeConfigs.intakeConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        m_intakeAngleMotor.configure(IntakeConfigs.intakeAngleConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    // arm.updateTelemetry();
    SmartDashboard.putNumber("Absolute Encoder Position", m_encoder.getPosition());
    SmartDashboard.putNumber("PID Setpoint", anglePIDController.getSetpoint());
  }

    public void positionIntake() {
        System.out.printf("Entered positionIntake\n");
        if (intakeDeployed) {
            System.out.printf("Retracting Intake\n");
            anglePIDController.setSetpoint(IntakeConstants.kIntakeRetractPosition, ControlType.kPosition);
            intakeDeployed = false;
        } else {
            System.out.printf("Extending Intake\n");
            anglePIDController.setSetpoint(IntakeConstants.kIntakeExtendPosition, ControlType.kPosition);
            intakeDeployed = true;
        }
    }

    public void runIntake(double speed) {
        if (intakeRunning) {
            m_intakeMotor.set(0);
            intakeRunning = false;
        } else {
            intakePIDController.setSetpoint(speed, ControlType.kVelocity);
            intakeRunning = true;
        }
    }

    /*public void setDefaultCommand(Object setAngleSetpoint) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'setDefaultCommand'");
    }*/

    public void intakeSequence(double speed) {
        if (intakeDeployed) {
            m_intakeMotor.set(0);
            anglePIDController.setSetpoint(IntakeConstants.kIntakeRetractPosition, ControlType.kPosition);
            intakeRunning = false;
            intakeDeployed = false;
        } else {
            m_intakeMotor.set(speed);
            anglePIDController.setSetpoint(IntakeConstants.kIntakeExtendPosition, ControlType.kPosition);
            intakeRunning = true;
            intakeDeployed = true;
        }
    }
}