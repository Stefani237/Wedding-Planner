package com.example.stefani.weddingplanner.Helpers;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Base64;

import com.example.stefani.weddingplanner.Fragments.TakePictureInterface;
import com.example.stefani.weddingplanner.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class ImageChooseHelper {

    private Context mContext;
    public String mUserChosenSupplier;
    private TakePictureInterface mTakePictureInterface;
    private Intent mMediaScanIntent;
    public Uri mContentUri;

    public ImageChooseHelper(Context context, TakePictureInterface takePictureInterface) {
        mContext = context;
        mTakePictureInterface = takePictureInterface;
    }


    public void selectImage() { // show menu dialog
        final CharSequence[] items = {"Take Photo", "Choose From Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.add_photo_title);
        builder.setItems(items, new DialogInterface.OnClickListener() { // menu options:
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    mUserChosenSupplier = "Take Photo";
                    cameraIntent();
                } else if (items[item].equals("Choose From Gallery")) {
                    mUserChosenSupplier = "Choose From Gallery";
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss(); // close dialog
                }
            }
        });
        builder.show();
    }

    public void cameraIntent() { // camera is selected
        // standard Intent action that sent to have the camera application capture an image and return it
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mTakePictureInterface.createCameraIntent(intent);
    }

    public void galleryIntent() { // gallery is selected
        Intent intent = new Intent();
        intent.setType("image/*"); // type of the intent data - only images
        intent.setAction(Intent.ACTION_GET_CONTENT); // display all intent data, allowing the user to pick one of them and returning the resulting URI to the caller
        mTakePictureInterface.createGalleryIntent(intent);
    }


    public Bitmap onSelectFromGalleryResult(Intent data) { // retrieves a bitmap out of the intent
        Bitmap bm = null;
        if (data != null) {
            try {
                // getBitmap(ContentResolver cr, Uri url)
                // content resolver defines read write permission for URI.
                bm = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bm;
    }


    public Intent getPictureTakenDestination() {
        return mMediaScanIntent;
    }

    public Bitmap onCaptureImageResult(Intent data) { // data is the returned image
        mMediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE); // get image from camera

        Bitmap extras = (Bitmap) data.getExtras().get("data"); // retrieve the intent's extras (= image) under key name data
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        extras.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        // file object to store our image - the directory to save the file and it's name
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".png");
        mContentUri = Uri.fromFile(destination); // create a uri from file

        mMediaScanIntent.setData(mContentUri); // update scanner with data

        FileOutputStream fo;
        try { // burn bites into OutputStream
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return extras;
    }


    public String encodeBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); // a place to temporarily store our data while working with it
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        }
        // encode baos's byteArray into a long base64 string
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

        return imageEncoded;

    }


}
