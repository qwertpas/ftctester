/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;


@Autonomous(name = "AutoFODOpMode_Linear", group = "chris")
public class AutoFODOpMode_Linear extends LinearOpMode {

    // The IMU sensor object
    BNO055IMU imu;

    // State used for updating telemetryAutoIMUOpMode_Linear
    Orientation angles;

    private DcMotor aDrive = null;
    private DcMotor bDrive = null;
    private DcMotor cDrive = null;
    private DcMotor dDrive = null;

    private double heading = 0;

    private void setPower(double aPower, double bPower, double cPower, double dPower) {
        aDrive.setPower(aPower);
        bDrive.setPower(bPower);
        cDrive.setPower(cPower);
        dDrive.setPower(dPower);
    }

    @Override
    public void runOpMode() {


        aDrive = hardwareMap.get(DcMotor.class, "aDrive");
        bDrive = hardwareMap.get(DcMotor.class, "bDrive");
        cDrive = hardwareMap.get(DcMotor.class, "cDrive");
        dDrive = hardwareMap.get(DcMotor.class, "dDrive");


        // Set up the parameters for how the IMU is going to give info
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled = false;

        //Initialize the IMU
        // Make sure it is configured on robot as IC2 port 0 "Expansion Hub Internal IMU" and named "imu"
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        Motors moto = new Motors(hardwareMap.get(DcMotor.class, "aDrive"), hardwareMap.get(DcMotor.class, "bDrive"), hardwareMap.get(DcMotor.class, "cDrive"), hardwareMap.get(DcMotor.class, "dDrive"));

        moto.clearmotors();
        // Wait until we're told to go
        waitForStart();



        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double origAngle = angles.firstAngle;
        //loop of turning
        while (opModeIsActive()) {

            //get update heading
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            heading = angles.firstAngle - origAngle;

            //Uses FOD
            moto.moveGlobalAngle(0, heading, 0.2);


            telemetry.addData("heading", formatAngle(AngleUnit.DEGREES, heading));
            telemetry.update();
        }
    }


    private double formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    private double formatDegrees(double degrees) {
        return AngleUnit.DEGREES.normalize(degrees);
    }
}
