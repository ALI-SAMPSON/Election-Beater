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

        //Class for handling the Validation of Voter details
public class InputValidationVoterLogin extends AsyncTask<String,Void,String> {

    //Object of the ProgressDialog Class
    ProgressDialog progressDialog;

    //Object of the Context Class
    Context context;

    //Constructor for the InputValidationVoterLogin Class
    public InputValidationVoterLogin(Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {

        String type = params[0];
        String login_url = "http://10.21.43.22/my_project/login.php";     //URL to the Php Script for voter login

        if (type.equals("login")) {
            try {
                    //String variables for storing index of params
                String student_id = params[1];
                String pin = params[2];
                URL url = new URL(login_url);   //creates a new object of the URL class and passes the url defined above to it
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();   //new object of the HttpURLConnection Class
                httpURLConnection.setRequestMethod("POST");     //sets a Request Method on the  httpURLConnection
                httpURLConnection.setDoOutput(true);     //sets a DoOutput operation  on the  httpURLConnection to true
                httpURLConnection.setDoInput(true);     //sets a DoInput operation  on the  httpURLConnection to true

                 /** Code for validating the column values against the columns in the database
                   * and for insert the data into the database successfully
                 */
                OutputStream outputStream = httpURLConnection.getOutputStream();    //new object of the OutputStream Class
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                    //Encode the Text of the these fields for writing
                String post_data = URLEncoder.encode("student_id", "UTF-8")+"="+URLEncoder.encode(student_id, "UTF-8")+"&"
                        +URLEncoder.encode("pin", "UTF-8")+"="+URLEncoder.encode(pin, "UTF-8");
                bufferedWriter.write(post_data);    //writes the text from the TextInputEditText fields into the database
                bufferedWriter.flush();   //pushes everything out of the bufferedWriter
                bufferedWriter.close();  //closes the bufferedWriter
                outputStream.close();   //closes the outputStream

                    //Reading response from the server
                InputStream inputStream = httpURLConnection.getInputStream();   //new object of the InputStream Class
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();      //closes the bufferedReader
                inputStream.close();        //closes the inputStream
                httpURLConnection.disconnect();     //disconnects the  httpURLConnection
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
        progressDialog = ProgressDialog.show(context,"Logging in....",null,true,true);
       // alertDialog = new AlertDialog.Builder(context).create();    //creates an alertDialog in this context
       // alertDialog.setTitle("Login Status:");        //sets the Title Section of the AlertDialog as "Login Status"
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

            final Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    progressDialog.dismiss();
                    timer.cancel();     //this will cancel the timer of the system
                }
            }, 5000);   // the timer will count 5 seconds....
            Toast.makeText(context,result,Toast.LENGTH_LONG).show();

        if(result.equalsIgnoreCase("Successfully Logged In"))
        {
            Intent myIntent = new Intent(context, AfterLoginActivity.class);
            //myIntent.putExtra("user", student_id);
            context.startActivity(myIntent);    //starts the intent in this context;
        }
        else
        {
                 //displays to Voter that he/she has entered an Invalid Email or Password
             progressDialog.dismiss();  //dismisses the progressDialog
            //Calls the Toast View and Passes the result to be displayed on it
             Toast.makeText(context,result, Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}
