package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.util.GamepadEx;
import org.firstinspires.ftc.teamcode.util.MultipleGamepad;

@Config
@TeleOp(name="Bot Debug", group="Discopolus")
public class BotDebug extends LinearOpMode {
    public static double shooterVelocity = 0.0;
    public static double intakePower= 0.0;
    public static double ringPushingPosition = 0.34;
    public static double ringShootingPosition = 0.1;
    public static double linearSlidePower = 0.5;
    public static double armPosition = 0.5;
    public static double latchPosition = 0.5;
    public static boolean canDrive = true;

    private GamepadEx gamepad;

    private final ElapsedTime shootingClock = new ElapsedTime();
    public static double shootingDelay = 500.0;
    private boolean startedAiming = false;
    public static double shootingCooldown = 500.0;
    private enum ShootingState {
        AIM,
        SHOOT,
        RESET,
        WAIT
    }
    private ShootingState shoot = ShootingState.AIM;


    //Auto Aim Testing
    public static double highGoalZ = 91/2.54;
    public static Vector2d highGoal = new Vector2d(70.75, -46.5+12);
    public static double MaxError = 1;

    public static double shootingHeight = 3;
    public static double ShootingAngle = 33;
    public static double ShootingForwardOffset = 18;
    public static double ShooterRadius = 3;

    public static double MaxStartVelo = 335;
    public static double MinStartVelo = 291;

    public static double AngleOffset = 0;
    private double velocity = 0;

    @Override
    public void runOpMode() {
        telemetry.setAutoClear(false);
        telemetry.addData("","started OpMode");
        telemetry.update();
        gamepad = new GamepadEx(gamepad1);
        Bot bot = new Bot(hardwareMap);
        telemetry.addData("","created Bot instance");
        telemetry.update();
        bot.telemetry.addTelemetry(telemetry);
        bot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bot.setPoseEstimate(new Pose2d(-63, -25, 0));
        bot.initVision();
        bot.telemetry.addData("","Done Initializing");
        bot.telemetry.update();
        waitForStart();
        telemetry.setAutoClear(true);
        while (opModeIsActive()) {
            if(!startedAiming){
                bot.Shooter.setVelocity(shooterVelocity);
            }
            bot.Intake.setPower(intakePower);
//            bot.Trigger.setPosition(ringPushingPosition);
            bot.linearSlide.setPosition(linearSlidePower);
            bot.Arm.setPosition(armPosition);
            bot.Latch.setPosition(latchPosition);
            Pose2d currentPose = bot.getPoseEstimate();
            bot.telemetry.addData("Shooter Velocity",bot.Shooter.getVelocity());
            bot.telemetry.addData("GamepadEx x",gamepad.x.getState());
            bot.telemetry.addData("GamepadEx x last: ", gamepad.x.getLastState());
            bot.telemetry.addData("startedAiming: ", startedAiming);
            bot.telemetry.addData("velocity for shoot: ", velocity);
//Shooting Code
            switch(shoot){
                case AIM: {
                    if(gamepad.x.justPressed() && !startedAiming){
                        startedAiming = true;
                        bot.telemetry.addData("","You actually pressed x");
                        bot.turnAsync(highGoal.minus(currentPose.vec()).angle()-currentPose.getHeading()-AngleOffset);
                    }
                    if(!bot.isBusy() && startedAiming){
                        bot.telemetry.addData("it stopped turning yo","");
                        double maxVelo = MaxStartVelo;
                        double minVelo = MinStartVelo;
                        double velo = 0;
                        double x = Math.cos(Math.toRadians(ShootingAngle));
                        double y = Math.sin(Math.toRadians(ShootingAngle));
                        double i = highGoalZ;
                        while(i>MaxError){
                            velo = (maxVelo+minVelo)/2;
                            double t = highGoal.distTo(currentPose.vec())/(velo*x);
                            i = highGoalZ-(((-385.827/2)*t*t)+(velo*y*t)+shootingHeight);
                            if(Math.signum(i)==1){
                                minVelo = velo;
                            }
                            if(Math.signum(i)==-1){
                                maxVelo = velo;
                            }
                            if(Math.signum(i)==0){
                                break;
                            }
                        }
                        velocity = velo;
                        bot.telemetry.addData("found velocity: ", velo/ShooterRadius);
                        if(bot.Shooter.getVelocity(AngleUnit.RADIANS)>=velo/ShooterRadius){
                            shoot = ShootingState.SHOOT;
                            break;
                        }
                        bot.Shooter.setVelocity(velo/ShooterRadius, AngleUnit.RADIANS);
                    }
                    break;
                }
                case SHOOT: {
                    shootingClock.reset();
                    bot.Trigger.setPosition(ringShootingPosition);
                    shoot = ShootingState.RESET;
                    break;
                }
                case RESET: {
                    if(shootingClock.milliseconds()>= shootingDelay){
                        shootingClock.reset();
                        bot.Trigger.setPosition(ringPushingPosition);
                        shoot = ShootingState.WAIT;
                        break;
                    }
                }
                case WAIT: {
                    if(shootingClock.milliseconds()>= shootingCooldown){
                        shootingClock.reset();
                        shoot= ShootingState.AIM;
                        startedAiming = false;
                        break;
                    }
                }
            }

            if(canDrive && !startedAiming){
                // Create a vector from the gamepad x/y inputs
                // Then, rotate that vector by the inverse of that heading
                Vector2d input = new Vector2d(
                        -gamepad1.left_stick_y,
                        -gamepad1.left_stick_x
                ).rotated(-bot.getPoseEstimate().getHeading());

                // Pass in the rotated input + right stick value for rotation
                // Rotation is not part of the rotated input thus must be passed in separately
                bot.setWeightedDrivePower(
                        new Pose2d(
                                input.getX(),
                                input.getY(),
                                -gamepad1.right_stick_x
                        )
                );
            }
            gamepad.update();
            bot.update();
        }

        bot.deactivateVision();
    }

}
