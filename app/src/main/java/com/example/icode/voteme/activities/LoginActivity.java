package com.example.icode.voteme.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.icode.voteme.R;
import com.example.icode.voteme.inputValidation.InputValidationAdminLogin;
import com.example.icode.voteme.inputValidation.InputValidationVoterLogin;
import com.example.icode.voteme.models.Voter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity {

    //private ProgressDialog progressDialog;
    TextInputEditText textInputEditTextStudentId;
    TextInputEditText textInputEditTextPin;

    FirebaseDatabase database;
    DatabaseReference voterRef;

    ProgressDialog progressDialog;

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
        final String student_id = textInputEditTextStudentId.getText().toString().trim();
        final String pin = textInputEditTextPin.getText().toString().trim();

        progressDialog = ProgressDialog.show(LoginActivity.this, "Logging Up...", null, true, true);

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

            Query query = voterRef.child("voters").orderByChild("student_id").equalTo(textInputEditTextStudentId.getText().toString().trim());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // dataSnapshot is the "issue" node with all children with id 0
                        for (DataSnapshot voterdataSnapshot : dataSnapshot.getChildren()) {
                            // do something with the individual "issues"
                            Voter voter = voterdataSnapshot.getValue(Voter.class);
                            if (voter.getPin().equals(textInputEditTextPin.getText().toString().trim())) {
                                Intent intentLogin = new Intent(LoginActivity.this, AfterLoginActivity.class);
                                startActivity(intentLogin);
                            } else {
                                Toast.makeText(LoginActivity.this, "Pin is incorrect", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "Voter is not found", Toast.LENGTH_LONG).show();
                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //cannot connect to db or internet
                    Toast.makeText(LoginActivity.this, "Cannot connect to database" + databaseError.toException(), Toast.LENGTH_LONG).show();
                }
            });

      /*      //describe the type of operation to be perform. e.g a Login Operation
            String type = "login";
            InputValidationVoterLogin inputValidationLogin = new InputValidationVoterLogin(this);
            inputValidationLogin.execute(type, student_id, pin);

            //Clears the textInputEditTextPin for pin after a successful login
            textInputEditTextPin.setText("");
            */
        }
    }


    public void loginAuthentication(){

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

