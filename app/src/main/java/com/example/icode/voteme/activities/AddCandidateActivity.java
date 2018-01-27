package com.example.icode.voteme.activities;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.icode.voteme.R;
import com.example.icode.voteme.inputValidation.InputValidationAddCandidate;
import com.example.icode.voteme.inputValidation.InputValidationVoterRegister;

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

        //ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_candidate);

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

            // imageView = (ImageView)findViewById(R.id.imageViewCandidate);

    }

    //Method to add a new candidate when button is clicked...
  /*  public void onAddCandidateButtonClick(View view){
        //getting text from the textInputEditText field and Spinner View
        String full_name = textInputEditText_full_name.getText().toString().trim();
        String level = spinnerLevel.getSelectedItem().toString().trim();
        String programme = spinnerProgramme.getSelectedItem().toString().trim();
        String portfolio = spinnerPortfolio.getSelectedItem().toString().trim();
        String candidate_id = textInputEditText_candidate_id.getText().toString().trim();


        String error_fill_text = "This field cannot be left blank";

        //InputValidation for the various textFields that is, tests to see if the textInputEditTextFields are fill with relevant data
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

            String type = "register_candidate";     //defines the type of operation to be performed

            //Creates an object of the InputValidationAddCandidate Class in this context
            InputValidationAddCandidate inputValidationAddCandidate = new InputValidationAddCandidate(this);
            //Executes the object of the InputValidationVoterRegister Class using the String variables
            inputValidationAddCandidate.execute(type, full_name, level, programme, portfolio, candidate_id);

            clearTextFields();      //call to the clearTextFields
        }

    }*/

    //Moves Admin to next activity to continue with the registration process
    public void onNextButtonClick(View view){
        Intent intentNext = new Intent(AddCandidateActivity.this,AddCandidatePictureActivity.class);
        startActivity(intentNext);
    }

    //Clear textfields when clicked
    public void onCancelButtonClick(View view){
        textInputEditText_full_name.setText("");
        textInputEditText_candidate_id.setText("");
        //call to Method
        clearSpinnerValues();

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

}
