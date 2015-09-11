package com.chdev.ks.minx.SplashFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.chdev.ks.minx.R;
import com.chdev.ks.minx.SettingsPreferences;
import com.github.leonardoxh.customfont.FontText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

/**
 * Created by Kartik_ch on 9/4/2015.
 */
public class SplashSettingsFragment extends Fragment {

    FontText title, body;
    MaterialBetterSpinner titleSpinner, bodySpinner;
    String[] title_items, body_items;
    ArrayAdapter<String> arrayAdapterTitle, arrayAdapterBody;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash_settings, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        title= (FontText) getView().findViewById(R.id.tutorial_splash_title);
        body= (FontText) getView().findViewById(R.id.tutorial_splash_body);
        titleSpinner= (MaterialBetterSpinner) getView().findViewById(R.id.titleFontSizeSpinner);
        bodySpinner= (MaterialBetterSpinner) getView().findViewById(R.id.bodyFontSizeSpinner);
        title_items=getActivity().getResources().getStringArray(R.array.splash_title_entries);
        body_items=getActivity().getResources().getStringArray(R.array.splash_body_entries);
        arrayAdapterTitle=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, title_items);
        arrayAdapterBody=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, body_items);
        titleSpinner.setAdapter(arrayAdapterTitle);
        bodySpinner.setAdapter(arrayAdapterBody);
        setDefaultValues();
        titleSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setFontSize("title", title_items[position]);
            }
        });
        bodySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setFontSize("body", body_items[position]);
            }
        });
    }

    private void setDefaultValues() {
        new SettingsPreferences(getActivity(), "title").setSettingsPreferences("30");
        new SettingsPreferences(getActivity(), "body").setSettingsPreferences("20");
        new SettingsPreferences(getActivity()).setSettingsPreferences(-2);
        new SettingsPreferences(getActivity()).resetPINTemp();
    }

    private void setFontSize(String type, String fontSize){
        if(type.equals("title")){
            SettingsPreferences settingsPreferences=new SettingsPreferences(getActivity(), "title");
            settingsPreferences.setSettingsPreferences(fontSize);
            title.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(fontSize));
        }else{
            if(type.equals("body")){
                SettingsPreferences settingsPreferences=new SettingsPreferences(getActivity(), "body");
                settingsPreferences.setSettingsPreferences(fontSize);
                body.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(fontSize));
            }
        }
    }
}
