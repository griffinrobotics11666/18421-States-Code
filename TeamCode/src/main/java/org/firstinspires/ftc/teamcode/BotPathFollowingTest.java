package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.path.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "GVF Path Following Test", group = "Discopolus")
public class BotPathFollowingTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Bot drive = new Bot(hardwareMap);
        drive.telemetry.addTelemetry(telemetry);

        Path test = drive.pathBuilder(new Pose2d())
                .lineToLinearHeading(new Pose2d(36, -12, Math.toRadians(90)))
                .forward(24)
                .build();

        waitForStart();

        drive.followPath(test);
    }
}
