package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="Print Most Recent Log")
public class Logger extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        FtcDashboard dashboard = FtcDashboard.getInstance();
        MultipleTelemetry t = new MultipleTelemetry(dashboard.getTelemetry(), telemetry);

        t.setAutoClear(false);
        for(String s: Log.log){
            t.addData("", s);
        }
        t.update();

        waitForStart();
        while(opModeIsActive() && !isStopRequested()){
            sleep(1000);
        }
    }
}
