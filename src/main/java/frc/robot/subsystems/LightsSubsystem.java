package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.LightsConstants;

public class LightsSubsystem extends SubsystemBase {

    public final Spark m_Blinkin; // 4855

    public LightsSubsystem() {
        m_Blinkin = new Spark(4);
        setLEDs(LightsConstants.C1_AND_C2_SINELON);
    }

    @Override
    public void periodic() {
        String gameData;
        gameData = DriverStation.getGameSpecificMessage();
        if (gameData.length() > 0) { //Code to check which hub is inactive first, will need timer
            switch (gameData.charAt(0)) {
                case 'B':
                    // Blue case code
                    break;
                case 'R':
                    // Red case code
                    break;
                default:
                    // This is corrupt data
                    break;
            }
        } else {
            // Code for no data received yet
        }
    }

    public void setLEDs(double color) {
        m_Blinkin.set(color);
    }

}