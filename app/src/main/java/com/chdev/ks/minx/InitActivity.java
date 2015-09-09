package com.chdev.ks.minx;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class InitActivity extends Activity {

    private SharedPreferences mSharedPreferences;
    private String mSharedPref="APP_INIT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        if(checkIfNewInstall()){
            runIntent(SplashActivity.class);
        }else{
            new SettingsPreferences(InitActivity.this).resetPINTemp();
            runIntent(MainActivity.class);
            //removed this because its irritating
            /*Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    new SettingsPreferences(InitActivity.this).resetPINTemp();
                    runIntent(MainActivity.class);
                }
            }, 1000);*/
        }
    }

    private void runIntent(Class activityClass) {
        Intent intent=new Intent(InitActivity.this, activityClass);
        startActivity(intent);
        finish();
    }

    private boolean checkIfNewInstall() {
        try {
            mSharedPreferences = getSharedPreferences(mSharedPref, Context.MODE_PRIVATE);
            if(mSharedPreferences.getString("install_success", null).equals("true")){
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }
}
