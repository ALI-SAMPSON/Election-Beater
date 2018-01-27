package com.example.icode.voteme.activities;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.icode.voteme.R;
import com.example.icode.voteme.inputValidation.InputValidationAdminLogin;

import org.w3c.dom.Text;

public class AdminLoginActivity extends AppCompatActivity {

    TextInputEditText textInputEditTextAdminID;
    TextInputEditText textInputEditTextPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        textInputEditTextAdminID = (TextInputEditText)findViewById(R.id.textInputEditTextAdminID);
        textInputEditTextPin = (TextInputEditText)findViewById(R.id.textInputEditTextPin);

    }

    public void onAdminLoginButtonClick(View view){
        //Retrieve the text entered from the textInputEditText Field
        String admin_id = textInputEditTextAdminID.getText().toString().trim();
        String pin = textInputEditTextPin.getText().toString().trim();

        //describe the type of operation to be perform. e.g a Login Operation
        String type = "login";
        InputValidationAdminLogin inputValidationAdminLogin = new InputValidationAdminLogin(this);
        inputValidationAdminLogin.execute(type, admin_id,  pin);

        //Clears the textInputEditTextPin for pin after a successful login
        textInputEditTextPin.setText("");
    }

    public void onVoterLoginTextViewLinkClick(View view){
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
    }
}
