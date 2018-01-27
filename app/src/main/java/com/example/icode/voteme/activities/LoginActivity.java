package com.example.icode.voteme.activities;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.icode.voteme.R;
import com.example.icode.voteme.inputValidation.InputValidationAdminLogin;
import com.example.icode.voteme.inputValidation.InputValidationVoterLogin;

public class LoginActivity extends AppCompatActivity {

    //private ProgressDialog progressDialog;
    TextInputEditText textInputEditTextStudentId;
    TextInputEditText textInputEditTextPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textInputEditTextStudentId = (TextInputEditText)findViewById(R.id.textInputEditTextStudentID);
        textInputEditTextPin = (TextInputEditText)findViewById(R.id.textInputEditTextPin);

    }

    //Method for the On-Button Click event
    public void onLoginButtonClick(View view) {
        //Retrieve the text entered from the textInputEditText Field
        String student_id = textInputEditTextStudentId.getText().toString().trim();
        String pin = textInputEditTextPin.getText().toString().trim();

        if(student_id.equals("") || pin.equals("")){
            Toast.makeText(LoginActivity.this, "Student ID or Pin field cannot be left blank", Toast.LENGTH_LONG).show();
           // textInputEditTextStudentId.setError("Student ID field cannot be left blank");
            return;
        }
       else if(student_id.length() < 8 ) {
            //Toast.makeText(LoginActivity.this, "Student ID must be of at least 8 characters", Toast.LENGTH_LONG).show();
            textInputEditTextStudentId.setError("Student ID must be of at least 8 characters");
            return;
        }
        else if(pin.length() < 5) {
            //Toast.makeText(LoginActivity.this, "Student Pin must be of at least 5 characters", Toast.LENGTH_LONG).show();
            textInputEditTextPin.setError("Student Pin must be of at least 5 characters");
            return;
        }
        else
        {
            //describe the type of operation to be perform. e.g a Login Operation
            String type = "login";
            InputValidationVoterLogin inputValidationLogin = new InputValidationVoterLogin(this);
            inputValidationLogin.execute(type, student_id, pin);

            //Clears the textInputEditTextPin for pin after a successful login
            textInputEditTextPin.setText("");
        }
    }

    //AppCompatTextView Click Listener for Voter Registration Screen
    public void onRegisterTextViewLinkClick(View view) {
        Intent intentRegister = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intentRegister);
    }

    //AppCompatTextView Click Listener for Admin Login Screen
    public void onAdminLoginTextViewLinkClick(View view){
        Intent intentAdminLogin = new Intent(LoginActivity.this, AdminLoginActivity.class);
        startActivity(intentAdminLogin);
    }

    }

