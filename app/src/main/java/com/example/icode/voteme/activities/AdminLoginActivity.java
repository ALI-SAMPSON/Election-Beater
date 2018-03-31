package com.example.icode.voteme.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.icode.voteme.R;
import com.example.icode.voteme.inputValidation.InputValidationAdminLogin;
import com.example.icode.voteme.models.Admin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class AdminLoginActivity extends AppCompatActivity {

    TextInputEditText textInputEditTextAdminID;
    TextInputEditText textInputEditTextPin;

    ProgressDialog progressDialog;
    FirebaseDatabase database;
    DatabaseReference adminRef;

    Admin admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        textInputEditTextAdminID = (TextInputEditText)findViewById(R.id.textInputEditTextAdminID);
        textInputEditTextPin = (TextInputEditText)findViewById(R.id.textInputEditTextPin);

        admin = new Admin();
        database = FirebaseDatabase.getInstance();
        adminRef = database.getReference().child("Admin");

    }

    public void onAdminLoginButtonClick(View view){
        //Retrieve the text entered from the textInputEditText Field
        final String admin_id = textInputEditTextAdminID.getText().toString().trim();
        final String pin = textInputEditTextPin.getText().toString().trim();


        if(admin_id.equals("") || pin.equals("")){
            Toast.makeText(AdminLoginActivity.this, "Student ID or Pin field cannot be left blank", Toast.LENGTH_LONG).show();
            // textInputEditTextStudentId.setError("Student ID field cannot be left blank");
            return;
        }
        else if(admin_id.length() < 8 ) {
            //Toast.makeText(LoginActivity.this, "Student ID must be of at least 8 characters", Toast.LENGTH_LONG).show();
            textInputEditTextAdminID.setError("Student ID must be of at least 8 characters");
            return;
        }
        else if(pin.length() < 5) {
            //Toast.makeText(LoginActivity.this, "Student Pin must be of at least 5 characters", Toast.LENGTH_LONG).show();
            textInputEditTextPin.setError("Student Pin must be of at least 5 characters");
            return;
        }
        else
        {
            onLoginAdmin();
        }
    }

        //Method to authenticate and login the Administrator
    public void onLoginAdmin(){
        progressDialog = ProgressDialog.show(AdminLoginActivity.this, "Signing Up...", null, true, true);

        progressDialog.show(); //displays the progress dialog

        //get the values from the fields and sets them to that of the values in the database
        admin.setAdmin_id(textInputEditTextAdminID.getText().toString().trim());
        admin.setPin(textInputEditTextPin.getText().toString().trim());

        adminRef.child(admin.getAdmin_id()).setValue(admin).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                    Toast.makeText(AdminLoginActivity.this, "You have Successfully Signed Up...", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(AdminLoginActivity.this, "Cannot connect to database, Please try again...!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //clears text fields
    public void clearTextFields() {
        textInputEditTextAdminID.setText(null);
        textInputEditTextPin.setText(null);
    }

    //takes the voter to the login activity
    public void onVoterLoginTextViewLinkClick(View view){
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
    }
}
