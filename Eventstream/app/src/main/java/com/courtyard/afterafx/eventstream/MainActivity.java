package com.courtyard.afterafx.eventstream;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.parse.ParseFile;


public class MainActivity extends Activity {

    private Camera mCamera;
    public static byte[] scaledPhoto;
    public String currentEvent;
    private ParseFile image;
    private ParseFile thumbnail;

    private static final int MEDIA_TYPE_IMAGE = 1;

    private final static String TAG = "MainActivity";


    //onCreate
    //Called when the app is opened! Creates and sets variables

    /**
     * ---------------------------------------------------
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the content view to be the activity_main.xml file
        setContentView(R.layout.activity_main);


        //Create the capture button by finding the element with "button_capture" id in the xml
        Button captureButton = (Button) findViewById(R.id.button_capture);
        Button profileButton = (Button) findViewById(R.id.button_profile);
        Button eventsButton = (Button) findViewById(R.id.button_events);
        Button createButton = (Button) findViewById(R.id.button_create);


        // Create an instance of Camera
        mCamera = getCameraInstance();


        // Create our Preview view and set it as the content of our activity.
        CameraPreview mPreview = new CameraPreview(this, mCamera);
        final FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);


        // Add a listener to the Capture button
        captureButton.setOnClickListener(

                new View.OnClickListener() {

                    public void onClick(View v) {
                        if (mCamera == null)
                            return;

                        // get an image from the camera
                        mCamera.takePicture(new Camera.ShutterCallback() {

                            @Override
                            public void onShutter() {
                                //nothing to do
                            }
                        }, null, new Camera.PictureCallback() {

                            @Override
                            public void onPictureTaken(byte[] data, Camera camera) {
                                if (data != null) {

                                    File image = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                                    if (image == null) {
                                        Log.d(TAG, "Error creating media file, check storage permissions: ");
                                        return;
                                    }

                                    try {
                                        FileOutputStream fos = new FileOutputStream(image);
                                        fos.write(data);
                                        setScaledPhoto(data);
                                        fos.close();
                                    } catch (FileNotFoundException e) {
                                        Log.d(TAG, "File not found: " + e.getMessage());
                                    } catch (IOException e) {
                                        Log.d(TAG, "Error accessing file: " + e.getMessage());
                                    }
                                } else {
                                    Log.d(TAG, "Data is null");
                                }
                            }
                        });

                        /**--------------------------------------------------------*/
                        //After you take a picture hide the buttons.

                        View b = findViewById(R.id.button_capture);
                        b.setVisibility(View.GONE);

                        View n = findViewById(R.id.button_profile);
                        n.setVisibility(View.GONE);

                        View m = findViewById(R.id.button_events);
                        m.setVisibility(View.GONE);

                        View k = findViewById(R.id.button_create);
                        k.setVisibility(View.GONE);

                        /**--------------------------------------------------------*/


                        //After taking a picture, open the CameraFragment
                        FragmentManager manager = getFragmentManager();
                        Fragment fragment = manager.findFragmentById(R.id.photo_preview);


                        if (fragment == null) {
                            fragment = new CameraFragment();
                            //add id of the FrameLayout to fill, and the fragment that will hold
                            manager.beginTransaction().add(R.id.main_Layout, fragment).commit();
                        }

                        /**--------------------------------------------------------*/


                    }

                }


        );

        profileButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                        startActivity(intent);
                    }
                }
        );

        eventsButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, EventsActivity.class);
                        startActivity(intent);
                    }
                }
        );


        createButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent createIntent = new Intent(MainActivity.this, CreateEvent.class);
                        startActivity(createIntent);
                    }
                }
        );


    }

    /**
     * ---------------------------------------------------
     */

    protected void onDestroy() {
        super.onDestroy();
        if (mCamera != null) {
            mCamera.release();
        }
    }

    //onPause
    //Called when the app is paused by either pressing the BACK or HOME key

    /**
     * ---------------------------------------------------
     */

    @Override
    protected void onPause() {
        //super.onPause();

        Log.d(TAG, "onPause");

        //isFinishing() called when app is closing!
        //Releases the Camera fully
        if (this.isFinishing()) {
            //Release the camera when app is closed to background or exited
            mCamera.release();
            mCamera = null;
            Log.d(TAG, "isFinishing()");
        }

        //Called when switching between activities!
        // Only stops the preview but does not release the Camera
        if (mCamera != null && !this.isFinishing()) {
            mCamera.stopPreview();
            //mCamera.release();
            Log.d(TAG, "NOT isFinishing()");
        }

        super.onPause();

    }

    //onResume

    /**
     * ---------------------------------------------------
     */

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if (mCamera == null) {
            try {
                mCamera = Camera.open();
            } catch (Exception e) {
                Log.i(TAG, "No camera: " + e.getMessage());
                //Toast.makeText(getActivity(), "No camera detected", Toast.LENGTH_LONG).show();
            }
        }
    }


    // Setters & Getters

    /**
     * ---------------------------------------------------
     */


    public ParseFile getImageFile() {

        return image;
    }

    public ParseFile getThumbnailFile() {
        return thumbnail;
    }


    public void setScaledPhoto(byte[] data) {
        this.scaledPhoto = data;
    }

    public static byte[] getScaledPhoto() {
        return scaledPhoto;
    }


    //Create camera instance, a safe way to get an instance of the Camera object.
    /**
     * ---------------------------------------------------
     */

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            Log.d(TAG, "Open Camera");
            c = Camera.open(); // attempt to get a Camera instance
            Camera.Parameters params = c.getParameters();
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            c.setParameters(params);

        } catch (Exception e) {
            Log.i(TAG, "Device doesn't have a Camera");
        }
        return c; // returns null if camera is unavailable
    }


    //Create a File for saving an image or video. Uses the environment external storage directory.
    //Creates each file using unique timestamp

    //Returns the File Object for the new image, or null if there was some error creating the file.

    /**
     * ---------------------------------------------------
     */

    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        Log.i(TAG, "Entering getOutputMediaFile");

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "My Eventstream");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        // Make sure you have permission to write to the SD Card enabled.
        // in order to do this!!
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.i(TAG, "getOutputMediaFile failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp;
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    /**
     * ---------------------------------------------------
     */

    private byte[] saveScaledPhoto(byte[] data) {

        // Resize photo from camera byte array
        Bitmap eventImage = BitmapFactory.decodeByteArray(data, 0, data.length);
        Bitmap eventImageScaled = Bitmap.createScaledBitmap(eventImage, 200, 200
                * eventImage.getHeight() / eventImage.getWidth(), false);

        // Override Android default landscape orientation and save portrait
        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        Bitmap rotatedScaledEventImage = Bitmap.createBitmap(eventImageScaled, 0, 0, eventImageScaled.getWidth(), eventImageScaled.getHeight(), matrix, true);
        //Bitmap scaledEventImage = Bitmap.createBitmap(eventImageScaled);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        rotatedScaledEventImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);

        byte[] scaledData = bos.toByteArray();

        return scaledData;


    }

    /**---------------------------------------------------*/
}