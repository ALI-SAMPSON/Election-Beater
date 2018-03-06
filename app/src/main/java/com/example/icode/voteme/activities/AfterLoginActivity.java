package com.example.icode.voteme.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.icode.voteme.R;

public class AfterLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Vote Starts");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left_black);
       // toolbar.setLogo(R.mipmap.first_icon);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String msg = "";
        switch (item.getItemId()){
            case R.id.good_day:
                msg = "Welcome and enjoy voting!";
                Toast.makeText(AfterLoginActivity.this,msg, Toast.LENGTH_LONG).show();
                break;
            case R.id.info:
                //open info page or activity
                Intent intent_info = new Intent(AfterLoginActivity.this, App_Info.class);
                startActivity(intent_info);
                break;
            //logs the voter out of the system
            case R.id.logout:
                Intent intent_logout = new Intent(AfterLoginActivity.this, LoginActivity.class);
                startActivity(intent_logout);
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
