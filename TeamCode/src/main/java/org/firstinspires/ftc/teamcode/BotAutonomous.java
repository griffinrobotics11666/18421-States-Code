package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Autonomous(name="Autonomous", group = "Discopolus")
/**
 * Team 18421's Autonomous for the 2020-2021 Ultimate Goal season.
 */
public class BotAutonomous extends LinearOpMode {
    private static Pose2d initialPose = new Pose2d(-63, -25, Math.toRadians(0));

    private static double triggerStart = 0.34;
    private static double triggerEnd = 0.1;
    private static double armDown = 1;
    private static double armUp = 0.1;

    private String ringCount;
    private Trajectory A1;
    private Trajectory A1End;
    private Trajectory B1;
    private Trajectory B1End;
    private Pose2d C;

    private Trajectory wobble1;
    private Trajectory wobble1End;
    private ElapsedTime Time = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {
        Bot drive = new Bot(hardwareMap);
        drive.telemetry.addTelemetry(telemetry);
        drive.usingVuforia = false;
        drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        drive.setPoseEstimate(initialPose);
        drive.initVision();

        drive.Arm.setPosition(armUp);
        drive.Trigger.setPosition(triggerStart);
        drive.Latch.setPosition(0.46);

        //Most Trajectories initialized here
        Trajectory followStack = drive.trajectoryBuilder(initialPose)
                .splineToConstantHeading(new Vector2d(-45,-33),0)
                .build();
        Trajectory A1 =  drive.trajectoryBuilder(followStack.end())
                .splineToSplineHeading(new Pose2d(12,-38, Math.toRadians(-180)), 0.0)
                .build();
        Trajectory A1End = drive.trajectoryBuilder(A1.end())
                .lineToSplineHeading(new Pose2d(A1.end().getX()+6, A1.end().getY()+6, Math.toRadians(-180.0)))
                .build();
        Trajectory B1 = drive.trajectoryBuilder(followStack.end())
                .splineTo(new Vector2d(-23, -20),0.0)
                .splineToSplineHeading(new Pose2d(25, -36, Math.toRadians(-90)), 0.0)
                .build();
        Trajectory B1End = drive.trajectoryBuilder(B1.end())
                .lineToSplineHeading(new Pose2d(B1.end().getX()+6, B1.end().getY()+6, Math.toRadians(-180.0)))
                .build();
        Trajectory C1 = drive.trajectoryBuilder(followStack.end(), Math.toRadians(-90))
                .splineToConstantHeading(new Vector2d(-37,-56), 0)
                .splineToSplineHeading(new Pose2d(48, -60, Math.toRadians(-90)), 0.0)
                .build();
        Trajectory C1End = drive.trajectoryBuilder(C1.end())
                .lineToSplineHeading(new Pose2d(C1.end().getX()-6, C1.end().getY(), 0.0))
                .build();

        drive.telemetry.addData("Ready!", "");
        drive.telemetry.update();
        while(!isStarted()) {
            drive.detectStarterStack(1);
        }
        waitForStart();

        Time.reset();

//        for(double i = 0; i <= 0.5; i+=0.0005){
//            drive.Arm.setPosition(i);
//        }

        //Detects Starter Stack
        drive.followTrajectory(followStack);
        drive.detectStarterStack(50);

        ringCount = drive.detectedStack;
        switch(drive.detectedStack){
            case "None": {
                wobble1 = A1;
                wobble1End = A1End;
                break;
            }
            case "Single": {
                wobble1 = B1;
                wobble1End = B1End;
                break;
            }
            case "Quad": {
                wobble1 = C1;
                wobble1End = C1End;
                break;
            }
        }

        //Moves loaded wobble goal accordingly
        drive.followTrajectory(wobble1);
        for(double i = 0; i <= 0.99; i+=0.0005){
            drive.Arm.setPosition(i);
        }
        drive.followTrajectory(wobble1End);

        //Shoots 3 rings into High Goal
        Trajectory goShoot = drive.trajectoryBuilder(drive.getPoseEstimate())
                .lineToLinearHeading(new Pose2d(-7, -34, Math.toRadians(0)))
                .build();
        drive.followTrajectory(goShoot);
        drive.Shooter.setVelocity(360, AngleUnit.DEGREES);
        sleep(1000);
        for(int i = 0; i<3; i++){
            drive.Trigger.setPosition(triggerEnd);
            sleep(300);
            drive.Trigger.setPosition(triggerStart);
            sleep(300);
        }

        //TODO: Pick up starter stack?
        //TODO: Pick up second wobble goal?

        //Parks on the white line
        Trajectory parkLine = drive.trajectoryBuilder(drive.getPoseEstimate())
                .forward(18)
                .build();
        drive.followTrajectory(parkLine);

        //Prints how long autonomous took
        drive.telemetry.addData("Time this took", Time.seconds());
        drive.telemetry.update();

        //Woohoo!! 56 points!!!
        drive.deactivateVision();
        drive.telemetry.addData("none, single, quad", drive.none + ", " + drive.single + ", " + drive.quad);
        drive.telemetry.addData("actually saw something: ",drive.actuallySawSomething);
    }
}
