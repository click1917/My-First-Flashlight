package com.example.mmcke.myfirstflashlightapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;


public class FlashlightActivity extends AppCompatActivity {


    //Decalre variables to be used in app. Note that they are all declared private since no other class needs to interact with them.
    //A good practice is to only allow variables to be updated or retrieved using getter and setter functions.
    private CameraManager cameraManager;
    private String cameraId;
    private Switch lightSwitch;
    private ImageView lightbulb;
    private Boolean flashlightOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Ensures that the activity_flashlight.xml file is displayed when the activity is created
        setContentView(R.layout.activity_flashlight);

        //Initialize flashlight to off
        flashlightOn = false;

        //Initialize the Switch and ImageView variables to point to the correct elements in the activity_flashlight.xml file
        lightSwitch = findViewById(R.id.light_switch);
        lightbulb = findViewById(R.id.lightbulb);

        //Check if Flashlight is available. If not, display error message to user.

        //This code retrieves a boolean true if the device has a flash camera (i.e. has FEATURE_CAMERA_FLASH)
        Boolean isFlashAvailable = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        //If the device does not have a flash camera isFlashAvailable will be false and we need to display an error message to the user
        if (isFlashAvailable == false) {


            //We will display our error message as an AlertDialog. Basically a pop-up window.

            //Create and AlertDialog. This only happens if the user can't run the app due to not having a supported flash camera
            AlertDialog alert = new AlertDialog.Builder(this)
                    .create();

            //Set the alerts Title and Message
            alert.setTitle("Device Incompatible");
            alert.setMessage("Sorry, Your device is unable to use this flashlight app.");

            //We don't want the alert to clode until the user hits OK. So, we set up an OnClickListener that "listens" for a user click on the OK button.
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

                //This will run when the user clicks on the OK button in the AlertDialog
                public void onClick(DialogInterface dialog, int which) {
                    //Since the device is not supported, closes the application for the user.
                    finish();
                    System.exit(0);
                }
            });

            //This ensured the alert is displayed to the user.
            alert.show();
            return;
        }

        //Create an on-click listener that will detect when on-off switch is toggled.
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        lightSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Will try to turn on flashlight/flash but throws exception if unable.
                try {
                    if (!lightSwitch.isChecked()) {
                        turnOffFlashLight();
                    } else {
                        turnOnFlashLight();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void turnOnFlashLight() {

        try {
            //Ensures that the device is running Android Marshmallow, otherwise it will crash the app
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Turns on "TorchMode" aka the flash
                cameraManager.setTorchMode(cameraId, true);
                //Updates imageView with the "on" image
                lightbulb.setImageResource(R.drawable.lighbulb_on);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void turnOffFlashLight() {

        try {
            //Ensures that the device is running Android Marshmallow, otherwise it will crash the app
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Turns off "TorchMode" aka the flash
                cameraManager.setTorchMode(cameraId, false);
                //Updates imageView with the "off" image
                lightbulb.setImageResource(R.drawable.lightbulb_off);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
