package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.util.tracking.Tracker;
import org.firstinspires.ftc.teamcode.util.tracking.TrackerKCF;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect2d;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class TrackerPipeline extends OpenCvPipeline {
    public static Rect2d boundBox = new Rect2d(40, 40, 40, 40);
    private Tracker tracker = TrackerKCF.create();
    private Rect2d outputBox = new Rect2d();
    private Mat output = new Mat();
    @Override
    public void init(Mat input){
        tracker.init(input, boundBox);
    }
    @Override
    public Mat processFrame(Mat input) {
        tracker.update(input, outputBox);
        input.copyTo(output);
        Imgproc.rectangle(output,outputBox.tl(),outputBox.br(), new Scalar(0, 255, 0), 5);
        return output;
    }
}
