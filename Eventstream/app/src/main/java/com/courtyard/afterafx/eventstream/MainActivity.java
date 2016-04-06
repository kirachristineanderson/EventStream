package com.courtyard.afterafx.eventstream;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.parse.ParseFile;


public class MainActivity extends Activity {

    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final int REQUEST_CAMERA = 0;
    private final static String TAG = "MainActivity";
    public static byte[] scaledPhoto;
    public String currentEvent;
    private Camera mCamera;
    private CameraPreview mPreview;
    private ParseFile image;
    private static final int REQUEST_CONTACTS = 0;
    private static String[] PERMISSIONS_CONTACT = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS};
    private ParseFile thumbnail;
    private static int cameraID = Camera.CameraInfo.CAMERA_FACING_BACK;

    //Returns true if our app is sent to the background. Returns false otherwise.
    public static boolean isApplicationSentToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }

        return false;
    }


    public static byte[] getScaledPhoto() {
        return scaledPhoto;
    }



    public void setScaledPhoto(byte[] data) {
        scaledPhoto = saveScaledPhoto(data);
    }




    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            Log.d(TAG, "Open Camera");
            c = Camera.open(cameraID); // attempt to get a Camera instance
            Camera.Parameters params = c.getParameters();
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            c.setParameters(params);

        } catch (Exception e) {
            Log.i(TAG, "Device doesn't have a Camera");
        }
        return c; // returns null if camera is unavailable
    }

    private static int getFrontCameraId(){
        int camId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo ci = new Camera.CameraInfo();

        for(int i = 0;i < numberOfCameras;i++){
            Camera.getCameraInfo(i,ci);
            if(ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
                camId = i;
            }
        }

        return camId;
    }

    private static int getBackCameraId(){
        int camId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo ci = new Camera.CameraInfo();

        for(int i = 0;i < numberOfCameras;i++){
            Camera.getCameraInfo(i,ci);
            if(ci.facing == Camera.CameraInfo.CAMERA_FACING_BACK){
                camId = i;
            }
        }

        return camId;
    }


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



    //onCreate
    //Called when the app is opened! Creates and sets variables
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the content view to be the activity_main.xml file
        setContentView(R.layout.activity_main);

        //Request Camera Permissions
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            // Do nothing.
        } else
        {
            //Request Camera Permission
            requestCameraPermission();
        }


       //requestContactsPermissions();


        //Create the capture button by finding the element with "button_capture" id in the xml
        Button captureButton = (Button) findViewById(R.id.button_capture);
        Button profileButton = (Button) findViewById(R.id.button_profile);
        Button eventsButton = (Button) findViewById(R.id.button_events);
        Button createButton = (Button) findViewById(R.id.button_create);
        Button switchCameraButton = (Button) findViewById(R.id.button_switchCamera);

        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        final FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        switchCameraButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        if(cameraID == Camera.CameraInfo.CAMERA_FACING_FRONT){
                            cameraID = getBackCameraId();
                        }
                        else if(cameraID == Camera.CameraInfo.CAMERA_FACING_BACK){
                            cameraID = getFrontCameraId();
                        }
                        recreate();
                    }
                }
        );

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



                        //After you take a picture hide the buttons.

                        View b = findViewById(R.id.button_capture);
                        b.setVisibility(View.GONE);

                        View n = findViewById(R.id.button_profile);
                        n.setVisibility(View.GONE);

                        View m = findViewById(R.id.button_events);
                        m.setVisibility(View.GONE);

                        View k = findViewById(R.id.button_create);
                        k.setVisibility(View.GONE);



                        //After taking a picture, open the CameraFragment
                        FragmentManager manager = getFragmentManager();
                        Fragment fragment = manager.findFragmentById(R.id.photo_preview);


                        if (fragment == null) {
                            fragment = new CameraFragment();
                            //add id of the FrameLayout to fill, and the fragment that will hold
                            manager.beginTransaction().add(R.id.main_Layout, fragment).commit();
                        }


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


    //onPause
    //Called when the app is paused by either pressing the BACK or HOME key
    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");

        if(mCamera != null) {
            mCamera.stopPreview();
            mPreview.setCamera(null);
            mCamera.release();
            mCamera = null;
        }

        super.onPause();

    }




    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");

        int numCams = Camera.getNumberOfCameras();
        if(numCams > 0){

            try {
                mCamera = Camera.open(cameraID);
                mCamera.startPreview();
                mPreview.setCamera(mCamera);

            } catch(RuntimeException e){

            }
        }
    }





    public ParseFile getImageFile() {

        return image;
    }



    //Create a File for saving an image or video. Uses the environment external storage directory.
    //Creates each file using unique timestamp
    //Returns the File Object for the new image, or null if there was some error creating the file.

    public ParseFile getThumbnailFile() {
        return thumbnail;
    }




    private byte[] saveScaledPhoto(byte[] data) {

        // Resize photo from camera byte array
        Bitmap eventImage = BitmapFactory.decodeByteArray(data, 0, data.length);
        Bitmap eventImageScaled = Bitmap.createScaledBitmap(eventImage, 800, 800
                * eventImage.getHeight() / eventImage.getWidth(), false);

        // Override Android default landscape orientation and save portrait
        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        Bitmap rotatedScaledEventImage = Bitmap.createBitmap(eventImageScaled, 0, 0, eventImageScaled.getWidth(), eventImageScaled.getHeight(), matrix, true);
        //Bitmap scaledEventImage = Bitmap.createBitmap(eventImageScaled);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        rotatedScaledEventImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);

        return bos.toByteArray();


    }



    private void requestCameraPermission(){
        Log.i(TAG, "Camera permission has NOT been granted. Requesting permission.");

        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)){

            //Provide an additional rationale to the user if the permission was not granted
            //and the user would benefit from additional context for the use of the permission.
            Log.i(TAG, "Displaying camera permission rationale to provide additional context.");
            Snackbar.make(mPreview, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE).setAction(R.string.permission_rationale, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
                }
            })
                    .show();
        } else {

            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);

        }
    }


    private void requestContactsPermissions() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_CONTACTS) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CONTACTS)) {

            Log.i(TAG, "Displaying contacts permission rationale to pervide additional context.");

            //Display a SnackBar with an explanation and a button to trigger the request.
            Snackbar.make(mPreview, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE).setAction(R.string.permission_rationale, new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_CONTACT, REQUEST_CONTACTS);
                }
            })
                    .show();
        } else {

            //Contact permissions have not been granted yet. Request them directly.
            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_CONTACT, REQUEST_CONTACTS);

        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA) {

            // Received permission result for camera permission
            Log.i(TAG, "Received response for Camera permission request.");

            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Camera permission has been granted, preview can be displayed
                Log.i(TAG, "CAMERA permission has now been granted. Showing preview.");
                Snackbar.make(mPreview, R.string.permission_rationale, Snackbar.LENGTH_SHORT).show();

            } else {

                Log.i(TAG, "CAMERA permission was NOT granted.");
                Snackbar.make(mPreview, R.string.permission_rationale, Snackbar.LENGTH_SHORT).show();

            }
        } else {

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    
}