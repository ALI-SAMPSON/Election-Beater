package com.example.icode.voteme.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.icode.voteme.R;
import com.example.icode.voteme.inputValidation.InputValidationAddCandidate;
import com.example.icode.voteme.inputValidation.InputValidationVoterRegister;
import com.example.icode.voteme.models.Candidate;
import com.example.icode.voteme.models.Voter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class AddCandidateActivity extends AppCompatActivity {

        //Instance variables for the TextFields declared in the xml file
    TextInputEditText textInputEditText_full_name;
    TextInputEditText textInputEditText_candidate_id;


    //AppCompatSpinner object and ArrayAdapter object for Voter's Level
    AppCompatSpinner spinnerLevel;
    ArrayAdapter<CharSequence> adapterLevel;

    //AppCompatSpinner object and ArrayAdapter object for Voter's Programme
    AppCompatSpinner spinnerProgramme;
    ArrayAdapter<CharSequence> adapterProgramme;

    //AppCompatSpinner object and ArrayAdapter object for Voter's Portfolio
    AppCompatSpinner spinnerPortfolio;
    ArrayAdapter<CharSequence> adapterPortfolio;

    //An instance of the Button View Class
    private Button buttonSelectImage;

    //An instance of the Button View Class
    private Button buttonRegister;

    //An instance of the ImageView View Class
    private ImageView imageView_Candidate;

    private Bitmap bitmap;

    //identifies the intent on which the operation will be done
    private final int REQUEST_IMAGE_SELECT = 1;

    FirebaseDatabase database;
    DatabaseReference voterRef;
    Candidate candidate;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_candidate);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Vote Starts");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left_black);

        //navigates the admin back to the panel
       if(getSupportActionBar() != null){
           getSupportActionBar().setDisplayShowHomeEnabled(true);
           getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       }
        // toolbar.setLogo(R.mipmap.first_icon);

        textInputEditText_full_name = (TextInputEditText)findViewById(R.id.textInputEditTextFullName);
        textInputEditText_candidate_id = (TextInputEditText)findViewById(R.id.textInputEditTextCandidateID);

        //spinner object initialization for Candidate's Level and Adapter implementation on the spinner object
        spinnerLevel = (AppCompatSpinner)findViewById(R.id.spinnerLevel);
        adapterLevel = ArrayAdapter.createFromResource(this,R.array.candidate_level,android.R.layout.simple_spinner_item);
        adapterLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLevel.setAdapter(adapterLevel);

        //spinner object initialization for Candidate's Programme and Adapter implementation on the spinner object
        spinnerProgramme = (AppCompatSpinner)findViewById(R.id.spinnerProgramme);
        adapterProgramme = ArrayAdapter.createFromResource(this,R.array.candidate_programme,android.R.layout.simple_spinner_item);
        adapterProgramme.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProgramme.setAdapter(adapterProgramme);

        //spinner object initialization for Candidate's Portfolio and Adapter implementation on the spinner object
        spinnerPortfolio = (AppCompatSpinner)findViewById(R.id.spinnerPortfolio);
        adapterPortfolio = ArrayAdapter.createFromResource(this,R.array.candidate_portfolio,android.R.layout.simple_spinner_item);
        adapterPortfolio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPortfolio.setAdapter(adapterPortfolio);

        buttonSelectImage = (Button)findViewById(R.id.buttonSelectImage);
        buttonRegister = (Button)findViewById(R.id.buttonRegister);
        imageView_Candidate = (ImageView)findViewById(R.id.imageView);

    }

    //select picture from gallery
    public void onSelectPicture(View view){
        selectPicture();
    }

    //method to handle the selection of picture from gallery
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

    //Converts image into string
    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);
    }

    //Registers candidate when clicked...
    public void onRegisterCandidate(View view){

        String error_fill_text = "This field cannot be left blank";

        /* InputValidation for the various textFields that is, tests to see if the
         * textInputEditTextFields are filled or not
        */

        if (textInputEditText_full_name.getText().toString().trim().equalsIgnoreCase(""))
        {
            textInputEditText_full_name.setError(error_fill_text);
        }

        else if (textInputEditText_candidate_id.getText().toString().trim().equalsIgnoreCase(""))
        {
            textInputEditText_candidate_id.setError(error_fill_text);
        }

        else if(textInputEditText_full_name.getText().toString().trim().equalsIgnoreCase("")
                && textInputEditText_candidate_id.getText().toString().trim().equalsIgnoreCase(""))
        {
            Toast.makeText(AddCandidateActivity.this, "Both fields are required!...Please fill them to proceed", Toast.LENGTH_LONG).show();
        }

        else
        {
            textInputEditText_full_name.setError(null);
            textInputEditText_candidate_id.setError(null);

            onRegisterCandidate();

        }
    }

    //Adds the voter details to the database
    public void onRegisterCandidate(){
        progressDialog = ProgressDialog.show(AddCandidateActivity.this, "Signing Up...", null, true, true);

        progressDialog.show(); //displays the progress dialog

        //get the values from the fields and sets them to that of the values in the database
        candidate.setFull_name(textInputEditText_full_name.getText().toString().trim());
        candidate.setLevel(spinnerLevel.getSelectedItem().toString().trim());
        candidate.setProgramme(spinnerProgramme.getSelectedItem().toString().trim());
        candidate.setProgramme(spinnerProgramme.getSelectedItem().toString().trim());
        candidate.setCandidate_id(textInputEditText_candidate_id.getText().toString().trim());
        //candidate.setCandidate_image(imageView_Candidate.setImageBitmap(bitmap).);


        voterRef.child(candidate.getCandidate_id()).setValue(candidate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    final Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        public void run() {
                            progressDialog.dismiss();    //dismisses the alertDialog
                            timer.cancel();     //this will cancel the timer of the system
                        }
                    }, 4000);   // the time r will count 4 seconds....
                    clearTextFields();
                    clearSpinnerValues();
                    Toast.makeText(AddCandidateActivity.this, " You have Successfully Added a New Candidate... ", Toast.LENGTH_LONG).show();
                }
                else
                {
                    final Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        public void run() {
                            progressDialog.dismiss();    //dismisses the alertDialog
                            timer.cancel();     //this will cancel the timer of the system
                        }
                    }, 4000);   // the timer will count 4 seconds....
                    clearTextFields(); //call to the clearTextFields
                    Toast.makeText(AddCandidateActivity.this, " Cannot connect to database, Please try again...! ", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //Resets values in the spinner views
    public void clearSpinnerValues(){
        //Resets adapter and spinner values for Candidate Level to Default
        adapterLevel = ArrayAdapter.createFromResource(this,R.array.candidate_level,android.R.layout.simple_spinner_item);
        adapterLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLevel.setAdapter(adapterLevel);

        //Resets adapter and spinner values for Candidate Program to Default
        adapterProgramme = ArrayAdapter.createFromResource(this,R.array.candidate_programme,android.R.layout.simple_spinner_item);
        adapterProgramme.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProgramme.setAdapter(adapterProgramme);

        //Resets adapter and spinner values for Candidate Portfolio to Default
        adapterPortfolio = ArrayAdapter.createFromResource(this,R.array.candidate_portfolio,android.R.layout.simple_spinner_item);
        adapterPortfolio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPortfolio.setAdapter(adapterLevel);
    }

    //Method for Clearing all textfields after Login Button is Clicked
    public void clearTextFields(){
            //Clears all text from the EditText
        textInputEditText_full_name.setText("");
        textInputEditText_candidate_id.setText("");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String msg = "";
        switch (item.getItemId()) {
            case R.id.home:
                Intent intentBack = new Intent(AddCandidateActivity.this, AdminPanel.class);
                startActivity(intentBack);
                break;
            case R.id.good_day:
                msg = "Good day Admin!";
                Toast.makeText(AddCandidateActivity.this, msg, Toast.LENGTH_LONG).show();
                break;
            case R.id.info:
                //open info page or activity
                Intent intent_info = new Intent(AddCandidateActivity.this, App_Info.class);
                startActivity(intent_info);
                break;
            //logs the voter out of the system
            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

}
