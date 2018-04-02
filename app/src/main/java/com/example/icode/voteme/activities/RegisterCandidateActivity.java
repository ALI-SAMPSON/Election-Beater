package com.example.icode.voteme.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.icode.voteme.R;
import com.example.icode.voteme.models.Candidate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Timer;
import java.util.TimerTask;

public class RegisterCandidateActivity extends AppCompatActivity {

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
    private Button buttonRegister;

    //An instance of the ImageButton View Class
    private ImageButton image;

    //identifies the intent on which the operation will be done
    private final int Gallery_Intent = 1;

    FirebaseDatabase database;
    DatabaseReference candidateRef;
    Candidate candidate;

    StorageReference imagePath;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_candidate);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register Candidate");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left_black);

        //navigates the admin back to the panel
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

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

        buttonRegister = (Button)findViewById(R.id.buttonRegister);
        image = (ImageButton)findViewById(R.id.image);

        database = FirebaseDatabase.getInstance();
        candidateRef = database.getReference("Candidate");
        candidate = new Candidate();


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
            Toast.makeText(RegisterCandidateActivity.this, "Both fields are required!...Please fill them to proceed", Toast.LENGTH_LONG).show();
        }

        else
        {
            textInputEditText_full_name.setError(null);
            textInputEditText_candidate_id.setError(null);

            onRegisterCandidate();

        }
    }

    //method to handle the Onclick ImageButton View
    public void onSelectImage(View view){
        //get the intent of the gallery of the phone
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, Gallery_Intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Gallery_Intent && resultCode == RESULT_OK){
            Uri uri = data.getData();
            image.setImageURI(uri);
            imagePath = FirebaseStorage.getInstance().getReference().child("Profile").child(uri.getLastPathSegment());
            imagePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(RegisterCandidateActivity.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RegisterCandidateActivity.this, "Image not Uploaded...Please Check the Image type", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //Adds the voter details to the database
    public void onRegisterCandidate(){
        progressDialog = ProgressDialog.show(RegisterCandidateActivity.this, "Registering Candidate...", null, true, true);

        progressDialog.show(); //displays the progress dialog

        //get the values from the fields and sets them to that of the values in the database
        candidate.setFull_name(textInputEditText_full_name.getText().toString().trim());
        candidate.setLevel(spinnerLevel.getSelectedItem().toString().trim());
        candidate.setProgramme(spinnerProgramme.getSelectedItem().toString().trim());
        candidate.setProgramme(spinnerProgramme.getSelectedItem().toString().trim());
        candidate.setCandidate_id(textInputEditText_candidate_id.getText().toString().trim());
        candidate.setCandidate_image(imagePath.toString());


        candidateRef.child(candidate.getCandidate_id()).setValue(candidate).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                    candidateRef.child("001");
                    Toast.makeText(RegisterCandidateActivity.this, " You have Successfully Added a New Candidate... ", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(RegisterCandidateActivity.this, " Cannot connect to database, Please try again...! ", Toast.LENGTH_LONG).show();
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
        String msg;
        switch (item.getItemId()) {
            //Home button
            case android.R.id.home:
                Intent homeIntent = new Intent(this, AdminPanel.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                break;
            case R.id.good_day:
                msg = "Good day Admin!";
                Toast.makeText(RegisterCandidateActivity.this, msg, Toast.LENGTH_LONG).show();
                break;
            case R.id.info:
                //open info page or activity
                Intent intent_info = new Intent(RegisterCandidateActivity.this, App_Info.class);
                startActivity(intent_info);
                break;
            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }


}
