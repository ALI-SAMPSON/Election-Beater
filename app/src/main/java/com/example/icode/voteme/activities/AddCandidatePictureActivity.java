package com.example.icode.voteme.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.icode.voteme.R;

import java.io.IOException;

public class AddCandidatePictureActivity extends AppCompatActivity {

    //An instance of the Button View Class
    private Button buttonSelectImage;

    //An instance of the Button View Class
    private Button buttonRegister;

    //An instance of the ImageView View Class
    private ImageView imageView_Candidate;

    private Bitmap bitmap;

    //identifies the intent on which the operation will be done
    private final int REQUEST_IMAGE_SELECT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_candidate_picture);


        buttonSelectImage = (Button)findViewById(R.id.buttonSelectImage);
        buttonRegister = (Button)findViewById(R.id.buttonRegister);
        imageView_Candidate = (ImageView)findViewById(R.id.imageView);

    }

    //select picture from gallery
    public void onSelectPicture(View view){
        selectPicture();
    }

    //selects picture from gallery
    private void selectPicture(){
        Intent intentPicture = new Intent();
        intentPicture.setType("images/*");
        intentPicture.setAction(intentPicture.ACTION_GET_CONTENT);

        startActivityForResult(intentPicture, REQUEST_IMAGE_SELECT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_SELECT && resultCode == RESULT_OK && data!=null){
            Uri path = data.getData();
            try {
                bitmap  = MediaStore.Images.Media.getBitmap(getContentResolver(),path);  //get Bitmap image from gallery
                imageView_Candidate.setImageBitmap(bitmap);
                imageView_Candidate.setVisibility(View.VISIBLE );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //Registers candidate when clicked
    public void onRegisterCandidate(View view){
        
    }

}
