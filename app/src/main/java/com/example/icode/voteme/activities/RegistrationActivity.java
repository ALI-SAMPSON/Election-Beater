package com.example.icode.voteme.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.icode.voteme.models.VoterModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


import com.example.icode.voteme.R;
import com.example.icode.voteme.inputValidation.InputValidationVoterRegister;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    private TextInputEditText textInputEditText_full_name;
    private TextInputEditText textInputEditText_student_id;
    private TextInputEditText textInputEditText_pin;
    private TextInputEditText textInputEditText_confirm_pin;

    private ProgressBar progressBar;
    private FirebaseAuth auth;

    private DatabaseReference db;

    private VoterModel votermodel;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_arrow_left_black);


        //Get firebase Auth instance
        auth = FirebaseAuth.getInstance();

        db = FirebaseDatabase.getInstance().getReference("VoterModel");

        //Get ProgressBar instance
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

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

    }

    public void OnRegisterButtonClick(View view) {

        //getting text from the textInputEditText field and Spinner View
        String str_full_name = textInputEditText_full_name.getText().toString().trim();
        String str_student_id = textInputEditText_student_id.getText().toString().trim();
        String str_pin = textInputEditText_pin.getText().toString().trim();
        String str_confirm_pin = textInputEditText_confirm_pin.getText().toString().trim();

        String str_level = spinnerLevel.getSelectedItem().toString().trim();
        String str_gender = spinnerGender.getSelectedItem().toString().trim();
        String str_programme = spinnerProgramme.getSelectedItem().toString().trim();

        String error_fill_text = "This field cannot be left blank";

        if (TextUtils.isEmpty(str_full_name)) {
            Toast.makeText(getApplicationContext(), error_fill_text, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(str_student_id)) {
            Toast.makeText(getApplicationContext(), error_fill_text, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(str_pin)) {
            Toast.makeText(getApplicationContext(), error_fill_text, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(str_confirm_pin)) {
            Toast.makeText(getApplicationContext(), error_fill_text, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(str_level)) {
            Toast.makeText(getApplicationContext(), error_fill_text, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(str_gender)) {
            Toast.makeText(getApplicationContext(), error_fill_text, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(str_programme)) {
            Toast.makeText(getApplicationContext(), error_fill_text, Toast.LENGTH_SHORT).show();
            return;
        }

        //defines the type of operation to be performed
        String type = "register";

        //Creates an object of the InputValidationVoterRegister Class in this context
        votermodel = new VoterModel(this);
        //Executes the object of the InputValidationVoterRegister Class using the String variables
        votermodel = new VoterModel(type, str_full_name, str_level, str_gender, str_programme,
                str_student_id, str_pin, str_confirm_pin);

        progressBar.setVisibility(View.VISIBLE);
        //create user
        auth.createUserWithEmailAndPassword(str_full_name, str_level)
                .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //defines the type of operation to be performed
                        String type = "register";
                        Toast.makeText(RegistrationActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegistrationActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                            clearTextFields();      //call to the clearTextFields
                        } else {
                            startActivity(new Intent(RegistrationActivity.this, AfterLoginActivity.class));
                            finish();
                            clearTextFields();      //call to the clearTextFields
                        }
                    }
                });

           }
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

        /* InputValidation for the various textFields that is, tests to see if the
         * textInputEditTextFields are fill with relevant data
        */


       /*
        if (textInputEditText_student_id.getText().toString().trim().equalsIgnoreCase(""))
        {
            textInputEditText_student_id.setError(error_fill_text);
        }

        else if (textInputEditText_pin.getText().toString().trim().equalsIgnoreCase(""))
        {
            textInputEditText_pin.setError(error_fill_text);
        }

        else if (textInputEditText_confirm_pin.getText().toString().trim().equalsIgnoreCase("") &&
                !textInputEditText_confirm_pin.getText().toString().trim().equals(textInputEditText_pin.getText().toString().trim()))
        {
            textInputEditText_confirm_pin.setError("Your Pin must match and must be at least 5 characters in length");  //Displays a  warning about password not matching
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

            textInputEditText_student_id.setError(null);
            textInputEditText_pin.setError(null);
            textInputEditText_confirm_pin.setError(null);

            //defines the type of operation to be performed
            String type = "register";

            //Creates an object of the InputValidationVoterRegister Class in this context
            InputValidationVoterRegister inputValidationVoterRegister = new InputValidationVoterRegister(this);
            //Executes the object of the InputValidationVoterRegister Class using the String variables
            inputValidationVoterRegister.execute(type, str_full_name, str_level, str_gender, str_programme,
                    str_student_id, str_pin, str_confirm_pin);

            clearTextFields();      //call to the clearTextFields
        }*/

    //}

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
