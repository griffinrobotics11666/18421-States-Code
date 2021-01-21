package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.Arrays;

@Autonomous(name="Starter Stack Detection", group="Discopolus")
public class    BotStackDetectionTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Bot bot = new Bot(hardwareMap);
//        bot.initVision(hardwareMap);
//        bot.activateVision();
        bot.initVision();
        bot.detectedStack = "None";
        while(!isStarted()) {
            bot.detectStarterStack(100);
            sleep(100);
        }
        telemetry.addData("detection: ", bot.detectedStack);
        telemetry.update();
//        while(!isStarted()){
//            bot.detectStarterStack(50);
//            telemetry.addData("None, Single, Quad", bot.none + ", " + bot.single + ", " + bot.quad);
//            telemetry.addData("stack", bot.detectedStack);
//            telemetry.addData("list", Arrays.toString(bot.tfod.objectLabels));
//            telemetry.addData("list length", bot.tfod.objectLabels.length);
//            telemetry.addData("is visible", bot.tfod.targetVisible);
//            telemetry.addData("recognitions null", bot.tfod.recognitions == null);
//            telemetry.addData("tfod", Arrays.toString(bot.tfod.tfod.getRecognitions().toArray()));
//            if(bot.tfod.recognitions != null){
//                telemetry.addData("recognition size", bot.tfod.recognitions.size());
//                telemetry.addData("recognitions", Arrays.toString(bot.tfod.recognitions.toArray()));
//
//            }
//            telemetry.update();
//        }
        bot.deactivateVision();
//        bot.deactivateVision();
    }
}
