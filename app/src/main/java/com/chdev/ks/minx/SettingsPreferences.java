package com.chdev.ks.minx;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by Kartik_ch on 9/5/2015.
 */
public class SettingsPreferences {
    String type, fontSize, pin, sharedPref;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    public SettingsPreferences(Context context, String type) {
        this.context=context;
        this.type = type;
        sharedPref="SETTINGS_PREF";
    }

    public SettingsPreferences(Context context){
        this.context=context;
    }

    public void setSettingsPreferences(String fontSize){
        sharedPreferences=context.getSharedPreferences(sharedPref, Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor.putString(type+"Size", fontSize);
        editor.commit();
    }

    public void setSettingsPreferences(int pin){
        try {
            this.pin = String.valueOf(pin);
            byte[] pinData = this.pin.getBytes("UTF-8");
            String encodedPIN = Base64.encodeToString(pinData, Base64.DEFAULT);
            sharedPreferences=context.getSharedPreferences(sharedPref, Context.MODE_PRIVATE);
            editor=sharedPreferences.edit();
            editor.putString("PIN", encodedPIN);
            editor.commit();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void setPINTemp(){
        sharedPreferences=context.getSharedPreferences(sharedPref, Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor.putBoolean("PIN_entered", true);
        editor.commit();
    }

    public void resetPINTemp(){
        sharedPreferences=context.getSharedPreferences(sharedPref, Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor.putBoolean("PIN_entered", false);
        editor.commit();
    }

    public Boolean retrievePINTemp(){
        sharedPreferences=context.getSharedPreferences(sharedPref, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("PIN_entered", false);
    }

    public String retrieveFontPreferences(){
        try {
            sharedPreferences = context.getSharedPreferences(sharedPref, Context.MODE_PRIVATE);
            fontSize = sharedPreferences.getString(type + "Size", null);
            return fontSize;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "nil";
    }

    public int retrievePINPreferences(){
        try {
            sharedPreferences = context.getSharedPreferences(sharedPref, Context.MODE_PRIVATE);
            pin = sharedPreferences.getString("PIN", null);
            byte[] pinData = Base64.decode(pin, Base64.DEFAULT);
            pin = new String(pinData, "UTF-8");
            return Integer.parseInt(pin);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
