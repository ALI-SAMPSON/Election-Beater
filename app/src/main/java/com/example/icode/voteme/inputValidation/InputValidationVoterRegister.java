package com.example.icode.voteme.inputValidation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.icode.voteme.activities.AfterLoginActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by iCODE on 1/7/2018.
 */

//Class for handling the Registration of Voter details
public class InputValidationVoterRegister extends AsyncTask<String,Void,String> {

    //Object of the ProgressDialog Class
    ProgressDialog progressDialog;

    //Object of the Context Class
    Context context;

    //Constructor for the InputValidationVoterRegister Class
    public InputValidationVoterRegister(Context ctx){
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String register_url = "http://10.0.2.2/my_project/register.php";       //URL to the Php Script for voter Registration
        if(type.equals("register"))
        {
                 //String variables for storing index of params
            String full_name = params[1];
            String level = params[2];
            String gender = params[3];
            String programme = params[4];
            String student_id = params[5];
            String pin = params[6];
            String confirm_pin = params[7];

            try {
                URL url = new URL(register_url);    //creates a new object of the URL class and passes the url defined above to it
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");     //sets a Request Method on the  httpURLConnection
                httpURLConnection.setDoOutput(true);     //sets a DoOutput operation  on the  httpURLConnection to true
                httpURLConnection.setDoInput(true);     //sets a DoInput operation  on the  httpURLConnection to true

                /** Code for validating the column values against the columns in the database
                 * and for insert the data into the database successfully
                 */
                OutputStream outputStream = httpURLConnection.getOutputStream();    //new object of the OutputStream Class
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                     //Encode the Text of the these fields for writing
                String post_data = URLEncoder.encode("full_name", "UTF-8")+"="+URLEncoder.encode(full_name, "UTF-8")+"&"
                        +URLEncoder.encode("level", "UTF-8")+"="+URLEncoder.encode(level, "UTF-8")+"&"
                        +URLEncoder.encode("gender", "UTF-8")+"="+URLEncoder.encode(gender, "UTF-8")+"&"
                        +URLEncoder.encode("programme", "UTF-8")+"="+URLEncoder.encode(programme, "UTF-8")+"&"
                        +URLEncoder.encode("student_id", "UTF-8")+"="+URLEncoder.encode(student_id, "UTF-8")+"&"
                        +URLEncoder.encode("pin", "UTF-8")+"="+URLEncoder.encode(pin, "UTF-8")+"&"
                        +URLEncoder.encode("confirm_pin" , "UTF-8")+"="+URLEncoder.encode(confirm_pin, "UTF-8");
                bufferedWriter.write(post_data);    //writes the text from the TextInputEditText fields into the database
                bufferedWriter.flush();      //pushes everything out of the bufferedWriter
                bufferedWriter.close();     //closes the bufferedWriter
                outputStream.close();      //closes the outputStream

                    //Reading response from the server
                InputStream inputStream = httpURLConnection.getInputStream();   //new object of the InputStream Class
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String line = "";
                String result = "";
                while((line = bufferedReader.readLine())!= null){
                    result += line;
                }
                bufferedReader.close();      //closes the bufferedReader object
                inputStream.close();        //closes the  inputStream object
                httpURLConnection.disconnect();     // disconnects the httpURLConnection
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context, "Signing Up...", null, true, true);
    }

    @Override
    protected void onPostExecute(String result) {

      //  if(result.equals("Sign Up Successful"))
      //  {
            final Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    progressDialog.dismiss();    //dismisses the alertDialog
                    timer.cancel();     //this will cancel the timer of the system
                }
            }, 4000);   // the timer will count 4 seconds....

           Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                //Instance of the Intent Class
            Intent myIntent = new Intent(context, AfterLoginActivity.class);
          //  myIntent.putExtra("user", full_name.getText().toString());
            context.startActivity(myIntent);    //starts the intent in this context;
      // }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
