// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
    public static final int kRotControllerPort = 1;
    public static final int kOperatorControllerPort = 2;
    public static final double DEADBAND = 0.05;    //joystick deadband
  }

  public static class SwerveConstants {
      //public static final double ROBOT_MASS = (60.55); 
      //public static final Matter CHASSIS    = new Matter(new Translation3d(0, 0, Units.inchesToMeters(8)), ROBOT_MASS);
      public static final double LOOP_TIME  = 0.13; //s, 20ms + 110ms sprk max velocity lag
      public static final double MAX_SPEED  = 4.3;
      public static final double kSpeedMultiplierDefault = 1.0;   
      public static final double kSpeedMultiplierSlow = 0.2;
      public static final double kScaleTranslation = 0.8; //Used for normal drive
      public static final double kScaleTranslationLow = 0.65; //Used for drive with the getTargetSpeedsMethod

      public static final Pose2d kTestPose = new Pose2d(2, 2, new Rotation2d(0));
  }

  public static final class PoseConstants {
    public static final Pose2d kBlueHubPose = new Pose2d(4.6256,4.0345, new Rotation2d(0));
    public static final Pose2d kRedHubPose = new Pose2d(11.9408,4.0345, new Rotation2d(0));
  }

  public static final class FlywheelConstants{
    public static final int kFlywheelLCanId = 14;
    public static final int kFlywheelMCanId = 15;
    public static final int kFlywheelRCanId = 16;

    public static final int kFlywheelCurrentLimit = 40;
    public static final double kFlywheelP = 0.001;
    public static final double kFlywheelI = 0;
    public static final double kFlywheelD = 0.04;

    public static final double kFlywheelTolerance = 0.95; // The percentage of the goal flywheel speed that is considered "up to speed" for the START_WAIT flywheel request

    public static final int[] kFlywheelSpeeds = { 0,    // 0     0
                                                  2000, // 0.125 1
                                                  2000, // 0.25  2
                                                  2000, // 0.375 3
                                                  2000, // 0.5   4
                                                  2000, // 0.625 5
                                                  2000, // 0.75  6
                                                  2000, // 0.875 7
                                                  2000, // 1.0   8
                                                  2300, // 1.125 9
                                                  2300, // 1.25  10
                                                  2300, // 1.375 11
                                                  2300, // 1.5   12
                                                  2400, // 1.625 13
                                                  2450, // 1.75  14
                                                  2500, // 1.875 15
                                                  2550, // 2.0   16
                                                  2600, // 2.125 17
                                                  2650, // 2.25  18
                                                  2700, // 2.375 19
                                                  2750, // 2.5   20
                                                  2850, // 2.625 21
                                                  2900, // 2.75  22
                                                  2950, // 2.875 23
                                                  3000, // 3.0   24
                                                  3100, // 3.125 25
                                                  3150, // 3.25  26
                                                  3200, // 3.375 27
                                                  3250, // 3.5   28
                                                  3300, // 3.625 29
                                                  3350, // 3.75  30
                                                  3425, // 3.875 31
                                                  3500, // 4.0   32
                                                  3675, // 4.125 33
                                                  3800, // 4.25  34
                                                  3900, // 4.375 35
                                                  4000, // 4.5   36
                                                  4100, // 4.625 37
                                                  4200, // 4.75  38
                                                  4300, // 4.875 39
                                                  4400, // 5.0   40
                                                  4500, // 5.125 41
                                                  4600, // 5.25  42
                                                  4700, // 5.375 43
                                                  4800, // 5.5   44
                                                  4900, // 5.625 45
                                                  5000, // 5.75  46
                                                  5100, // 5.875 47
                                                  5200};// 6.0   48
    public static final int kFlywheelDelieverSpeed = 4000;
    public static final int kFlywheelTestOverrideSpeed = 0;
    public static final int kFlywheelOverrideAdjustment = 25; // The amount to increase/decrease the flywheel speed by when the operator requests an override adjustment
  }

  public static final class IndexerConstants {
    public static final int kIndexerCanId = 12;
    public static final double kIndexerP = 0.0002;
    public static final double kIndexerI = 0;
    public static final double kIndexerD = 0.015;
    public static final int kIndexerSpeed = 3000;    
  }

  public static final class IntakeConstants {
    public static final int kIntakeCanId = 9;
    public static final double kIntakeP = 0.0002;
    public static final double kIntakeI = 0.0;
    public static final double kIntakeD = 0.0;

    public static final int kIntakeAngleCanId = 10;
    public static final double kIntakeAngleP = 2.1;
    public static final double kIntakeAngleI = 0;
    public static final double kIntakeAngleD = 0;

    public static final int kIntakeCurrentLimit = 20;
    public static final int kIntakeAngleCurrentLimit = 20;

    public static final double kIntakeSpeed = 5000;
    public static final double kIntakeRetractPosition = .185;
    // Angle reletive to 1 revolution of the intake arm
    public static final double kIntakeAgitatePosition = 0.12;
    public static final double kIntakeExtendPosition = -0.03;
  }

  public static final class ConveyorConstants {
    public static final int kConveyorCanId = 13;
    public static final double kConveyorP = 0.0002;
    public static final double kConveyorI = 0;
    public static final double kConveyorD = 0.05;
    public static final int kConveyorSpeed = 3000;

    public static final int kElevatorCanId = 11;
    public static final double kElevatorP = 0.0002;
    public static final double kElevatorI = 0;
    public static final double kElevatorD = 0.0;
    public static final int kElevatorLaunchSpeed = 3000;
    public static final int kElevatorIntakeSpeed = 2000;
  }

  public static final class ClimberConstants {
    public static final int kClimberCanId = -1;
    public static final double kClimberP = 1;
    public static final double kClimberI = 0;
    public static final double kClimberD = 0;

    public static final int kClimberCurrentLimit = 40;

    public static final double kClimberExtendPosition = 1;
    public static final double kClibmerRetractPosition = 0;
  }

  public static final class LightsConstants {
    public final static double RAINBOW_RAINBOWPALETTE = -.99;
    public final static double RAINBOW_PARTYPALETTE = -.97;
    public final static double RAINBOW_OCEANPALETTE = -.95;
    public final static double RAINBOW_LAVAPALETTE = -.93;
    public final static double RAINBOW_FORESTPALETTE = -.91;
    public final static double RAINBOW_GLITTER = -.89;
    public final static double CONFETTI = -.87;
    public final static double SHOT_RED = -.85;
    public final static double SHOT_BLUE = -.83;
    public final static double SHOT_WHITE = -.81;
    public final static double SINELON_RAINBOWPALETTE = -.79;
    public final static double SINELON_PARTYPALETTE = -.77;
    public final static double SINELON_OCEANPALETTE = -.75;
    public final static double SINELON_LAVAPALETTE = -.73;
    public final static double SINELON_FORESTPALETTE = -.71;
    public final static double BPM_RAINBOWPALETTE = -.69;
    public final static double BPM_PARTYPALETTE = -.67;
    public final static double BPM_OCEANPALETTE = -.65;
    public final static double BPM_LAVAPALETTE = -.63;
    public final static double BPM_FORESTPALETTE = -.61;
    public final static double FIRE_MEDIUM = -.59;
    public final static double FIRE_LARGE = -.57;
    public final static double TWINKLES_RAINBOWPALETTE = -.55;
    public final static double TWINKLES_PARTYPALETTE = -.53;
    public final static double TWINKLES_OCEANPALETTE = -.55;
    public final static double TWINKLES_LAVAPALETTE = -.53;
    public final static double TWINKLES_FORESTPALETTE = -.51;
    public final static double COLORWAVES_RAINBOWPALETTE = -.45;
    public final static double COLORWAVES_PARTYPALETTE = -.43;
    public final static double COLORWAVES_OCEANPALETTE = -.41;
    public final static double COLORWAVES_LAVAPALETTE = -.39;
    public final static double COLORWAVES_FORESTPALETTE = -.37;
    public final static double LARSONSCAN_RED = -.35;
    public final static double LARSONSCAN_GRAY = -.33;
    public final static double LIGHTCHASE_RED = -.31;
    public final static double LIGHTCHASE_BLUE = -.29;
    public final static double LIGHTCHASE_GRAY = -.27;
    public final static double HEARTBEAT_RED = -.25;
    public final static double HEARTBEAT_BLUE = -.23;
    public final static double HEARTBEAT_WHITE = -.21;
    public final static double HEARTBEAT_GRAY = -.19;
    public final static double BREATH_RED = -.17;
    public final static double BREATH_BLUE = -.15;
    public final static double BREATH_GRAY = -.13;
    public final static double STROBE_RED = -.11;
    public final static double STROBE_BLUE = -.09;
    public final static double STROBE_GOLD = -.07;
    public final static double STROBE_WHITE = -.05;
    public final static double C1_END_TO_END_BLEND_TO_BLACK = -.03;
    public final static double C1_LARSONSCAN = -.01;
    public final static double C1_LIGHTCHASE = .01;
    public final static double C1_HEARTBEAT_SLOW = .03;
    public final static double C1_HEARTBEAT_MEDIUM = .05;
    public final static double C1_HEARTBEAT_FAST = .07;
    public final static double C1_BREATH_SLOW = .09;
    public final static double C1_BREATH_FAST = .11;
    public final static double C1_SHOT = .13;
    public final static double C1_STROBE = .15;
    public final static double C2_END_TO_END_BLEND_TO_BLACK = .17;
    public final static double C2_LARSONSCAN = .19;
    public final static double C2_LIGHTCHASE = .21;
    public final static double C2_HEARTBEAT_SLOW = .23;
    public final static double C2_HEARTBEAT_MEDIUM = .25;
    public final static double C2_HEARTBEAT_FAST = .27;
    public final static double C2_BREATH_SLOW = .29;
    public final static double C2_BREATH_FAST = .31;
    public final static double C2_SHOT = .33;
    public final static double C2_STROBE = .35;
    public final static double SPARKLE_C1_ON_C2 = .37;
    public final static double SPARKLE_C2_ON_C1 = .39;
    public final static double C1_AND_C2_GRADIENT = .41;
    public final static double C1_AND_C2_BPM = .43;
    public final static double C1_AND_C2_END_TO_END_BLEND = .45;
    public final static double END_TO_END_BLEND = .47; 
    public final static double C1_AND_C2_NO_BLEND = .49;
    public final static double C1_AND_C2_TWINKLES = .51;
    public final static double C1_AND_C2_COLOR_WAVES = .53;
    public final static double C1_AND_C2_SINELON = .55;
    public final static double HOT_PINK = .57;
    public final static double DARK_RED = .59;
    public final static double RED = .61;
    public final static double RED_ORANGE = .63;
    public final static double ORANGE = .65;
    public final static double GOLD = .67;
    public final static double YELLOW = .69;
    public final static double LAWN_GREEN = .71;
    public final static double LIME = .73;
    public final static double DARK_GREEN = .75;
    public final static double GREEN = .77;
    public final static double BLUE_GREEN = .79;
    public final static double AQUA = .81;
    public final static double SKY_BLUE = .83;
    public final static double DARK_BLUE = .85;
    public final static double BLUE = .87;
    public final static double BLUE_VIOLET = .89;
    public final static double VIOLET = .91;
    public final static double WHITE = .93;
    public final static double GRAY = .95;
    public final static double DARK_GRAY = .97;
    public final static double BLACK = .99;
  }

  public static final class PowerSubsystemConstants {
    public static final int kPDP_CAN_ID = 17;
    public static final ModuleType kPDP_ModuleType = ModuleType.kRev;
  }
}
