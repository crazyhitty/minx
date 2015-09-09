package com.chdev.ks.minx;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.balysv.materialripple.MaterialRippleLayout;
import com.chdev.ks.minx.DatabaseManager.DataBaseOperations;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

/**
 * Created by Kartik_ch on 9/6/2015.
 */
public class SettingsFragment extends Fragment {

    MaterialRippleLayout rippleResetDbBtn, rippleResetPinBtn;
    MaterialBetterSpinner titleSpinner, bodySpinner;
    String[] title_items, body_items;
    ArrayAdapter<String> arrayAdapterTitle, arrayAdapterBody;
    EditText resetDbPIN, oldPIN, newPIN;
    SettingsPreferences settingsPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        titleSpinner= (MaterialBetterSpinner) getView().findViewById(R.id.titleFontSizeSpinnerMain);
        bodySpinner= (MaterialBetterSpinner) getView().findViewById(R.id.bodyFontSizeSpinnerMain);
        rippleResetDbBtn= (MaterialRippleLayout) getView().findViewById(R.id.rippleResetDbBtn);
        rippleResetPinBtn= (MaterialRippleLayout) getView().findViewById(R.id.rippleResetPinBtn);

        title_items=getActivity().getResources().getStringArray(R.array.splash_title_entries);
        body_items=getActivity().getResources().getStringArray(R.array.splash_body_entries);
        arrayAdapterTitle=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, title_items);
        arrayAdapterBody=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, body_items);
        titleSpinner.setAdapter(arrayAdapterTitle);
        bodySpinner.setAdapter(arrayAdapterBody);

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

        rippleResetDbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptResetDB();
            }
        });

        rippleResetPinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptResetPIN();
            }
        });
    }

    private void setFontSize(String type, String fontSize) {
        if(type.equals("title")){
            SettingsPreferences settingsPreferences=new SettingsPreferences(getActivity(), "title");
            settingsPreferences.setSettingsPreferences(fontSize);
        }else{
            if(type.equals("body")){
                SettingsPreferences settingsPreferences=new SettingsPreferences(getActivity(), "body");
                settingsPreferences.setSettingsPreferences(fontSize);
            }
        }
    }

    private void promptResetDB() {
        if(checkPINEnabled()){
            MaterialDialog materialDialog=new MaterialDialog.Builder(getActivity())
                    .title(R.string.exit_dialog_title)
                    .positiveText(R.string.exit_dialog_yes)
                    .negativeText(R.string.exit_dialog_cancel)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            new DataBaseOperations(getActivity()).deleteAllFromDB();
                        }
                    })
                    .build();
            materialDialog.show();
        }else{
            MaterialDialog materialDialog=new MaterialDialog.Builder(getActivity())
                    .title(R.string.exit_dialog_title)
                    .customView(R.layout.reset_db_dialog, true)
                    .positiveText(R.string.exit_dialog_yes)
                    .negativeText(R.string.exit_dialog_cancel)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            settingsPreferences = new SettingsPreferences(getActivity());
                            if (Integer.parseInt(resetDbPIN.getText().toString()) == settingsPreferences.retrievePINPreferences()) {
                                new DataBaseOperations(getActivity()).deleteAllFromDB();
                            } else {
                                Toast.makeText(getActivity(), "Invalid PIN", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .build();
            resetDbPIN = (EditText) materialDialog.getView().findViewById(R.id.confirm_pin_txt);
            materialDialog.show();
        }
    }

    private void promptResetPIN() {
        if(checkPINEnabled()){
            MaterialDialog materialDialog=new MaterialDialog.Builder(getActivity())
                    .title(R.string.new_pin_dialog_title)
                    .customView(R.layout.new_pin_dialog, true)
                    .positiveText(R.string.exit_dialog_yes)
                    .negativeText(R.string.exit_dialog_cancel)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            settingsPreferences = new SettingsPreferences(getActivity());
                            if (newPIN.getText().toString().length()==4) {
                                settingsPreferences.setSettingsPreferences(Integer.parseInt(newPIN.getText().toString()));
                            } else {
                                Toast.makeText(getActivity(), "Invalid PIN", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .build();
            newPIN = (EditText) materialDialog.getView().findViewById(R.id.new_user_pin_txt);
            materialDialog.show();
        }else {
            MaterialDialog materialDialog = new MaterialDialog.Builder(getActivity())
                    .title(R.string.exit_dialog_title)
                    .customView(R.layout.reset_pin_dialog, true)
                    .positiveText(R.string.exit_dialog_yes)
                    .negativeText(R.string.exit_dialog_cancel)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            settingsPreferences = new SettingsPreferences(getActivity());
                            if (Integer.parseInt(oldPIN.getText().toString()) == settingsPreferences.retrievePINPreferences()) {
                                if (newPIN.getText().toString().length() == 4) {
                                    settingsPreferences.setSettingsPreferences(Integer.parseInt(newPIN.getText().toString()));
                                } else {
                                    Toast.makeText(getActivity(), "Invalid new PIN", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Invalid PIN", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .build();
            oldPIN = (EditText) materialDialog.getView().findViewById(R.id.old_pin_txt);
            newPIN = (EditText) materialDialog.getView().findViewById(R.id.new_pin_txt);
            materialDialog.show();
        }
    }

    private Boolean checkPINEnabled(){
        try{
            SettingsPreferences settingsPreferences = new SettingsPreferences(getActivity());
            if(settingsPreferences.retrievePINPreferences()==-2){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
