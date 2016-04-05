package com.courtyard.afterafx.eventstream;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageViewActivity extends AppCompatActivity {

    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final String TAG = "ImageViewActvity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        final ImageView imageView = (ImageView) findViewById(R.id.imageView);

        final byte[] byteArray = getIntent().getByteArrayExtra("image");
        final Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        imageView.setImageBitmap(bitmap);

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ImageViewActivity.this);

                // set title
                alertDialogBuilder.setTitle("Event Stream");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Save Image?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                    File image = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                                    if (image == null) {
                                        Log.d(TAG, "Error creating media file, check storage permissions: ");
                                        return;
                                    }

                                    try {
                                        FileOutputStream fos = new FileOutputStream(image);
                                        fos.write(byteArray);
                                        fos.close();
                                    } catch (FileNotFoundException e) {
                                        Log.d(TAG, "File not found: " + e.getMessage());
                                    } catch (IOException e) {
                                        Log.d(TAG, "Error accessing file: " + e.getMessage());
                                    }

                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();


                return true;
            }
        });

    }

    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        Log.i("ImageViewActivity", "Entering getOutputMediaFile");

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "My Eventstream");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        // Make sure you have permission to write to the SD Card enabled.
        // in order to do this!!
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.i("ImageViewActivity", "getOutputMediaFile failed to create directory");
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


}