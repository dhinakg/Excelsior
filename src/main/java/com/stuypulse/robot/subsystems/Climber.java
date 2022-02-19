/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import com.stuypulse.robot.Constants;
import com.stuypulse.robot.Constants.ClimberSettings;
import com.stuypulse.robot.Constants.MotorSettings;
import com.stuypulse.robot.Constants.Ports;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

/*-
 * Climbs at end of match
 *
 * Contains:
 *      - Change tilt of climber
 *      - Move climber via motor
 *      - Different tilt angles
 *      - Encoder + Solenoid used for stopping
 *
 * @author independence106(Jason Zhou)
 * @author Ca7Ac1(Ayan Chowdhury)
 * @author marcjiang7(Marc Jiang)
 * @author ambers7(Amber Shen)
 * @author Souloutz(Howard Kong)
 * @author jiayuyan0501(Jiayu Yan)
 * @author ijiang05(Ian Jiang)
 * @author TraceyLin(Tracey Lin)
 * @author annazheng14(Anna Zheng)
 * @author lonelydot(Raymond Zhang)
 * @author andylin2004(Andy Lin)
 * @author hwang30git(Hui Wang)
 */
public class Climber extends SubsystemBase {

    public enum Tilt {
        // first param is shorter, second is longer
        MAX_TILT(Value.kForward),
        NO_TILT(Value.kReverse);

        private final Value extended;

        private Tilt(Value extended) {
            this.extended = extended;
        }
    }

    private final DoubleSolenoid tilter;

    private final DigitalInput bottomLimitSwitch;
    private final DigitalInput topLimitSwitch;

    private final Solenoid stopper;

    private final CANSparkMax climber;

    public Climber() {
        climber = new CANSparkMax(Ports.Climber.MOTOR, MotorType.kBrushless);
        MotorSettings.CLIMBER.configure(climber);

        stopper = new Solenoid(PneumaticsModuleType.CTREPCM, Ports.Climber.STOPPER);

        if (ClimberSettings.ENABLE_TILT) {
            tilter =
                    new DoubleSolenoid(
                            PneumaticsModuleType.CTREPCM,
                            Ports.Climber.TILTER_FORWARD,
                            Ports.Climber.TILTER_REVERSE);
        } else {
            tilter = null;
        }

        bottomLimitSwitch = new DigitalInput(Ports.Climber.BOTTOM_LIMIT_SWITCH);
        topLimitSwitch = new DigitalInput(Ports.Climber.TOP_LIMIT_SWITCH);
    }

    public void setMotor(double speed) {
        if (stopper.get()) {
            DriverStation.reportWarning("Climber attempted to run while lock was enabled!", true);
            setMotorStop();
        } else {
            climber.set(speed);
        }
    }

    public void setMotorStop() {
        climber.stopMotor();
    }

    public boolean getTopReached() {
        return topLimitSwitch.get();
    }

    public boolean getBottomReached() {
        return bottomLimitSwitch.get();
    }

    public void setTilt(Tilt tilt) {
        if (tilter != null) {
            tilter.set(tilt.extended);
        } else {
            DriverStation.reportWarning(
                    "Climber attempted to tilt while solenoids are disabled!", true);
        }
    }

    public void setClimberLocked() {
        setMotorStop();
        stopper.set(true);
    }

    public void setClimberUnlocked() {
        stopper.set(false);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        if (Constants.DEBUG_MODE.get()) {
            if (tilter != null) {
                SmartDashboard.putString("Debug/Climber/Tilter", tilter.get().toString());
            }
            SmartDashboard.putBoolean("Debug/Climber/Stopper Active", stopper.get());
            SmartDashboard.putNumber("Debug/Climber/Climber Speed", climber.get());
        }
    }
}
