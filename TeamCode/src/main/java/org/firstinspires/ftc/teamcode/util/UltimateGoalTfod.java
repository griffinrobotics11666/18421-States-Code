package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper for the Tensorflow stuff in the FTC SDK that makes it easier to use.
 */
public class UltimateGoalTfod {
    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Quad";
    private static final String LABEL_SECOND_ELEMENT = "Single";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    public boolean targetVisible = false;
    public String[] objectLabels = new String[0];

    public UltimateGoalTfod(VuforiaLocalizer vuforia, @NotNull HardwareMap hardwareMap){
        this.vuforia = vuforia;
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, this.vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
        tfod.setZoom(2.5, 16.0/9.0);
    }

    public void activate() {
        tfod.activate();
    }

    public void deactivate(){
        tfod.deactivate();
    }

    public void shutdown(){
        tfod.shutdown();
    }

    public void update(){
        // getUpdatedRecognitions() will return null if no new information is available since
        // the last time that call was made.
        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
        targetVisible = false;
        objectLabels = new String[0];
        if (updatedRecognitions != null) {
            targetVisible = true;
            // step through the list of recognitions and display boundary info.
            for (int i = 0; i<updatedRecognitions.size(); i++) {
                objectLabels[i] = updatedRecognitions.get(i).getLabel();
                i++;
            }
        }
    }
}
