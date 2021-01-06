package org.firstinspires.ftc.teamcode.util;

import android.graphics.Camera;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

/**
 * A wrapper for the Vuforia stuff in the FTC SDK that makes it easier to use.
 */
public class VuforiaUtil {

//    private float phoneXRotate    = 0;
//    private float phoneYRotate    = 0;
//    private float phoneZRotate    = 0;

    public static class CameraDirection {
        public static final float FORWARD = 90;
        public static final float BACK = -90;
        public static final float PORTRAIT = 90;
        public static final float LANDSCAPE = 0;
    }

    public static class CameraState {
        public float XRotate;
        public float YRotate;
        public float ZRotate;
        public float ForwardDisplacement;
        public float LeftDisplacement;
        public float VerticalDisplacement;
        public VuforiaLocalizer.CameraDirection cameraDirection;
        public CameraState(VuforiaLocalizer.CameraDirection cameraDirection, float CameraOrientation, float CameraChoice, float ZRotate, float ForwardDisplacement, float LeftDisplacement, float VerticalDisplacement){
            this.XRotate = CameraOrientation;
            this.YRotate = CameraChoice;
            this.ZRotate = ZRotate;
            this.ForwardDisplacement = ForwardDisplacement;
            this.LeftDisplacement = LeftDisplacement;
            this.VerticalDisplacement = VerticalDisplacement;
            this.cameraDirection = cameraDirection;
        }
    }

}
