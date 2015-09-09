package com.chdev.ks.minx;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.chdev.ks.minx.DatabaseManager.DataBaseOperations;
import com.github.leonardoxh.customfont.FontText;

import java.util.ArrayList;

/**
 * Created by Kartik_ch on 8/29/2015.
 */
public class SavedFragment extends Fragment {

    FontText enterPinTxt;
    EditText userArchivePinTxt;
    Button btnRetrieveArchive;
    ListView listViewSaved;
    int pinValue;
    SettingsPreferences settingsPreferences;
    ArrayList<String> titleArrLst = new ArrayList<>();
    ArrayList<String> urlArrLst = new ArrayList<>();
    ArrayList<String> bodyArrLst = new ArrayList<>();
    ArrayList<String> dateArrLst = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saved, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        enterPinTxt = (FontText) getView().findViewById(R.id.enter_pin_txt);
        userArchivePinTxt = (EditText) getView().findViewById(R.id.userArchivePinTxt);
        btnRetrieveArchive = (Button) getView().findViewById(R.id.btnRetrieveArchive);
        listViewSaved = (ListView) getView().findViewById(R.id.saved_list);

        if(checkPINEnabled()){
            enterPinTxt.setVisibility(View.GONE);
            userArchivePinTxt.setVisibility(View.GONE);
            btnRetrieveArchive.setVisibility(View.GONE);
            loadSavedData();
        }else{
            settingsPreferences = new SettingsPreferences(getActivity());
            if(!settingsPreferences.retrievePINTemp()){
                loadPIN();
            }else{
                enterPinTxt.setVisibility(View.GONE);
                userArchivePinTxt.setVisibility(View.GONE);
                btnRetrieveArchive.setVisibility(View.GONE);
                loadSavedData();
            }
        }
    }

    private void loadPIN(){
        btnRetrieveArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userArchivePinTxt.getText().toString().equals("")) {
                    pinValue = Integer.parseInt(userArchivePinTxt.getText().toString());
                } else {
                    pinValue = -1;
                }
                settingsPreferences = new SettingsPreferences(getActivity());
                if (settingsPreferences.retrievePINPreferences() == pinValue) {
                    settingsPreferences.setPINTemp();
                    hideTxt();
                    loadSavedData();
                } else {
                    Toast.makeText(getActivity(), "Invalid PIN", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Boolean checkPINEnabled(){
        try{
            SettingsPreferences settingsPreferences = new SettingsPreferences(getActivity());
            //simply check if pin was entered in tutorial screen
            if(settingsPreferences.retrievePINPreferences()==-2){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private void hideTxt() {
        FadeAnimation fadeAnimation = new FadeAnimation(getActivity());
        fadeAnimation.fadeOutAlpha(enterPinTxt);
        fadeAnimation.fadeOutAlpha(userArchivePinTxt);
        fadeAnimation.fadeOutAlpha(btnRetrieveArchive);
    }

    private void loadSavedData() {
        FadeAnimation fadeAnimation = new FadeAnimation(getActivity());
        fadeAnimation.fadeInAlpha(listViewSaved);

        DataBaseOperations dbOperations = new DataBaseOperations(getActivity().getApplicationContext());

        dbOperations.retrieveFromDB();
        titleArrLst = dbOperations.getTitleArrLst();
        urlArrLst = dbOperations.getUrlArrLst();
        bodyArrLst = dbOperations.getBodyArrLst();
        dateArrLst = dbOperations.getDateArrLst();

        if(urlArrLst.isEmpty()){
            Toast.makeText(getActivity(), "Database is empty", Toast.LENGTH_SHORT).show();
        }

        /*for(int i=0; i<titleArrLst.size(); i++){
            Toast.makeText(getActivity(), titleArrLst.get(i), Toast.LENGTH_SHORT).show();
        }*/

        listViewSaved.setAdapter(new SavedListAdapter(getActivity().getApplicationContext(), titleArrLst, urlArrLst, bodyArrLst, dateArrLst));
    }
}
