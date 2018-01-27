package com.example.icode.voteme.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.icode.voteme.R;

public class AddCandidatePictureActivity extends AppCompatActivity {

    //identifies the intent on which the operation will be done
    static final int REQUEST_IMAGE_CAPTURE = 1;

    //An instance of the Button View Class
    Button buttonCapture;

    //An instance of the ImageView View Class
    ImageView imageView_Candidate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_candidate_picture);

        buttonCapture = (Button)findViewById(R.id.buttonCapture);
        imageView_Candidate = (ImageView)findViewById(R.id.imageView);

        // Disable button if candidate has no camera on his/her smartphone
        if(!hasCamera()){
            buttonCapture.setEnabled(false);
        }

    }

    //check if the candidate has camera on his/her smartphone
    private boolean hasCamera(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    //Launching the Camera
    public void launchCamera(View view){
        Intent intentImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Take a picture and pass results(Image Captured Details) along to OnActivityResults
        startActivityForResult(intentImage,REQUEST_IMAGE_CAPTURE);
    }

    //Returning the image taken

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            //Get the image
            Bundle extras = data.getExtras();
            Bitmap candidate_picture = (Bitmap) extras.get("data");
            imageView_Candidate.setImageBitmap(candidate_picture);
        }
    }
}
