package com.chdev.ks.minx.SplashFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chdev.ks.minx.R;
import com.chdev.ks.minx.SettingsPreferences;

/**
 * Created by Kartik_ch on 9/5/2015.
 */
public class SplashPinFragment extends Fragment {

    EditText txtPIN;
    Button btnSavePin;
    String pinValue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash_pin, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        txtPIN= (EditText) getView().findViewById(R.id.userPinTxt);
        btnSavePin= (Button) getView().findViewById(R.id.btnSavePin);
        btnSavePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinValue=txtPIN.getText().toString();
                if(pinValue.length()==4){
                    new SettingsPreferences(getActivity()).setSettingsPreferences(Integer.parseInt(pinValue));
                    Toast.makeText(getActivity(), "PIN Saved", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "Invalid Pin", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
