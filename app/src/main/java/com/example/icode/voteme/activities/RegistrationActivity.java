package com.example.icode.voteme.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.icode.voteme.models.Voter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


import com.example.icode.voteme.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class RegistrationActivity extends AppCompatActivity {

    private TextInputEditText textInputEditText_full_name;
    private TextInputEditText textInputEditText_student_id;
    private TextInputEditText textInputEditText_pin;
    private TextInputEditText textInputEditText_confirm_pin;

    FirebaseDatabase database;
    DatabaseReference voterRef;
    Voter voter;

    private ProgressDialog progressDialog;


    //AppCompatSpinner object and ArrayAdapter object for Voter's Level
    AppCompatSpinner spinnerLevel;
    ArrayAdapter<CharSequence> adapterLevel;

    //AppCompatSpinner object and ArrayAdapter object for Voter's Level
    AppCompatSpinner spinnerGender;
    ArrayAdapter<CharSequence> adapterGender;

    //AppCompatSpinner object and ArrayAdapter object for Voter's Programme
    AppCompatSpinner spinnerProgramme;
    ArrayAdapter<CharSequence> adapterProgramme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

       // ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setDisplayShowHomeEnabled(true);

        //actionBar.setLogo(R.drawable.ic_arrow_back_white_24dp);

        textInputEditText_full_name = (TextInputEditText) findViewById(R.id.textInputEditTextFullName);
        textInputEditText_student_id = (TextInputEditText) findViewById(R.id.textInputEditTextStudentID);
        textInputEditText_pin = (TextInputEditText) findViewById(R.id.textInputEditTextPin);
        textInputEditText_confirm_pin = (TextInputEditText) findViewById(R.id.textInputEditTextConfirmPin);

        //spinner object initialization for Voter's Level and Adapter implementation on the spinner object
        spinnerLevel = (AppCompatSpinner) findViewById(R.id.spinnerLevel);
        adapterLevel = ArrayAdapter.createFromResource(this, R.array.level, android.R.layout.simple_spinner_item);
        adapterLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLevel.setAdapter(adapterLevel);

        //Set the text color of the Spinner's selected view (not a drop down list view)
       /* spinnerLevel.setSelection(0, true);
        View v = spinnerLevel.getSelectedView();
        ((TextView)v).setTextColor(Color.BLUE); */

        //spinner object initialization for Voter's Gender and Adapter implementation on the spinner object
        spinnerGender = (AppCompatSpinner) findViewById(R.id.spinnerGender);
        adapterGender = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapterGender);

        //spinner object initialization for Voter's Programme and Adapter implementation on the spinner object
        spinnerProgramme = (AppCompatSpinner) findViewById(R.id.spinnerProgramme);
        adapterProgramme = ArrayAdapter.createFromResource(this, R.array.programme, android.R.layout.simple_spinner_item);
        adapterProgramme.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProgramme.setAdapter(adapterProgramme);

        voter = new Voter();
        database = FirebaseDatabase.getInstance();
        voterRef = database.getReference().child("Voter");

    }

    //method for registering a new voter
    public void OnRegisterButtonClick(View view) {

        String error_fill_text = "This field cannot be left blank";


        /* InputValidation for the various textFields that is, tests to see if the
         * textInputEditTextFields are filled or not
        */

        if (textInputEditText_student_id.getText().toString().trim().equalsIgnoreCase(""))
        {
            textInputEditText_student_id.setError(error_fill_text);
            Toast.makeText(RegistrationActivity.this, "Student ID is a required field!...", Toast.LENGTH_LONG).show();
        }

        else if (textInputEditText_pin.getText().toString().trim().equalsIgnoreCase(""))
        {
            textInputEditText_pin.setError(error_fill_text);
            Toast.makeText(RegistrationActivity.this, "Pin is a required field!...", Toast.LENGTH_LONG).show();
        }

        else if (textInputEditText_confirm_pin.getText().toString().trim().equalsIgnoreCase("") &&
                !textInputEditText_confirm_pin.getText().toString().trim().equals(textInputEditText_pin.getText().toString().trim()))
        {
            textInputEditText_confirm_pin.setError("Your Pin must match and must be at least 5 characters in length");  //Displays a  warning about password not matching
            Toast.makeText(RegistrationActivity.this, "Password does not match!...", Toast.LENGTH_LONG).show();
        }

        else if(textInputEditText_full_name.getText().toString().trim().equalsIgnoreCase("")
                && textInputEditText_student_id.getText().toString().trim().equalsIgnoreCase("")
                && textInputEditText_confirm_pin.getText().toString().trim().equalsIgnoreCase("")
                && textInputEditText_pin.getText().toString().trim().equalsIgnoreCase(""))
        {
            Toast.makeText(RegistrationActivity.this, "All fields are required!...Please fill them to proceed", Toast.LENGTH_LONG).show();
        }
        else
        {
            onRegisterVoter();
        }
    }

    //Adds the voter details to the database
    public void onRegisterVoter(){
        progressDialog = ProgressDialog.show(RegistrationActivity.this, "Signing Up...", null, true, true);

        progressDialog.show(); //displays the progress dialog

        //get the values from the fields and sets them to that of the values in the database
        voter.setFull_name(textInputEditText_full_name.getText().toString().trim());
        voter.setStudent_id(textInputEditText_student_id.getText().toString().trim());
        voter.setPin(textInputEditText_pin.getText().toString().trim());
        voter.setConfirm_pin(textInputEditText_confirm_pin.getText().toString().trim());
        voter.setLevel(spinnerLevel.getSelectedItem().toString().trim());
        voter.setGender(spinnerGender.getSelectedItem().toString().trim());
        voter.setProgramme(spinnerProgramme.getSelectedItem().toString().trim());


        voterRef.child(voter.getStudent_id()).setValue(voter).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                    Toast.makeText(RegistrationActivity.this, " You have Successfully Signed Up...", Toast.LENGTH_LONG).show();
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
                    clearTextFields();
                    Toast.makeText(RegistrationActivity.this, "Cannot connect to database, Please try again...!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

         //Method for Clearing all textfields after Login Button is Clicked
    public void clearTextFields(){
                //Clears all text from the EditText
        textInputEditText_full_name.setText("");
        textInputEditText_student_id.setText("");
        textInputEditText_pin.setText("");
        textInputEditText_confirm_pin.setText("");
    }

    //Link to Login Activity if voter is already a member
    public void onLoginActivityLinkClick(View view){
        //creates a new object of the Intent class
        Intent intentLogin = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(intentLogin);  //starts the intent
    }

}
