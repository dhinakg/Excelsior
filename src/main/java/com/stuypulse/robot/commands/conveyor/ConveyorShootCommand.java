/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.conveyor;

import com.stuypulse.robot.subsystems.Conveyor;
import com.stuypulse.robot.subsystems.Conveyor.Direction;

import edu.wpi.first.wpilibj2.command.CommandBase;

/*
 * @author Ivan Wei (ivanw8288@gmail.com)
 * @author Ivan Chen (ivanchen07@gmail.com)
 * @author Tony Chen (tchenpersonal50@gmail.com)
 * @author Gus Watkins (gus@guswatkins.net)
 * @author Kelvin Zhao (kzhao31@github)
 * @author Richie Xue (keobkeig/GlitchRich)
 * @author Rui Dong (ruidong0629@gmail.com)
 * @author Anthony Chen (achen318)
 * @author Joseph Mei (Gliese667Cc/SaggyTroy)
 * @author Eric Lin (ericlin071906@gmail.com)
 * @author Carmin Vuong (carminvuong@gmail.com)
 * @author Jeff Chen (jeffc998866@gmail.com)
 * @author Sudipta Chakraborty (sudiptacc)
 * @author Andrew Che (andrewtheemerald@gmail.com)
 * @author Niki Chen (nikichen6769@gmail.com)
 * @author Vincent Wang (vinowang921@gmail.com)
 * @author Edmund Chin (edmundc421@gmail.com)
 */

public class ConveyorShootCommand extends CommandBase {

    public static class Ejectionless extends ConveyorShootCommand {
        public Ejectionless(Conveyor conveyor) {
            super(conveyor, true);
        }
    }

    private final Conveyor conveyor;
    private final boolean ejectionless;

    /** Creates a new ConveyorShootCommand. */
    private ConveyorShootCommand(Conveyor conveyor, boolean ejectionless) {
        this.conveyor = conveyor;
        this.ejectionless = ejectionless;

        addRequirements(conveyor);
    }

    public ConveyorShootCommand(Conveyor conveyor) {
        this(conveyor, false);
    }

    @Override
    public void execute() {
        conveyor.setTopBelt(Direction.FORWARD);
        conveyor.setGandalf(
                !ejectionless && conveyor.hasOpponentBall()
                        ? Direction.REVERSE
                        : Direction.FORWARD);
    }

    @Override
    public void end(boolean interrupted) {
        conveyor.setGandalf(Direction.STOPPED);
        conveyor.setTopBelt(Direction.STOPPED);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
