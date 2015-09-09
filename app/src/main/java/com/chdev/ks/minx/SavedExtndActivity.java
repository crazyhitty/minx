package com.chdev.ks.minx;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chdev.ks.minx.DatabaseManager.DataBaseOperations;
import com.github.leonardoxh.customfont.FontText;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SavedExtndActivity extends ActionBarActivity {

    @Bind(R.id.saved_extnd_toolbar)
    Toolbar toolbar;
    @Bind(R.id.saved_extnd_title_txt)
    FontText saved_extnd_title_txt;
    @Bind(R.id.saved_extnd_url_txt)
    FontText saved_extnd_url_txt;
    @Bind(R.id.saved_extnd_body_txt)
    FontText saved_extnd_body_txt;
    String title, url, body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_extnd);
        ButterKnife.bind(this);
        setUpToolbar();
        displayInfo();
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        final ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("");
    }

    private void displayInfo() {
        getExtraFromIntent();
        setFontSize();
        saved_extnd_title_txt.setText(title);
        saved_extnd_url_txt.setText(url);
        saved_extnd_body_txt.setText(body);
    }

    private void getExtraFromIntent() {
        Bundle bundle=getIntent().getExtras();
        title=bundle.getString("saved_extnd_title");
        url=bundle.getString("saved_extnd_url");
        body=bundle.getString("saved_extnd_body");
    }

    private void setFontSize() {
        //setting title font
        SettingsPreferences settingsPreferencesTitle=new SettingsPreferences(SavedExtndActivity.this, "title");
        String titleFont=settingsPreferencesTitle.retrieveFontPreferences();
        saved_extnd_title_txt.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(titleFont));
        //setting body font
        SettingsPreferences settingsPreferencesBody=new SettingsPreferences(SavedExtndActivity.this, "body");
        String bodyFont=settingsPreferencesBody.retrieveFontPreferences();
        saved_extnd_body_txt.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(bodyFont));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_saved_extnd, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.homeAsUp) {
            onBackPressed();
            return true;
        }*/

        if(id==R.id.action_delete){
            promptDelete();
            return true;
        }

        if(id==R.id.action_share){
            shareData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void promptDelete() {
        new MaterialDialog.Builder(SavedExtndActivity.this)
                .title(R.string.are_you_sure)
                .positiveText(R.string.exit_dialog_yes)
                .negativeText(R.string.exit_dialog_cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        new DataBaseOperations(SavedExtndActivity.this).deleteFromDB(url);
                        Toast.makeText(SavedExtndActivity.this, "Deleted succesfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SavedExtndActivity.this, MainActivity.class));
                        finish();
                    }
                }).show();
    }

    private void shareData() {
        getExtraFromIntent();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        shareIntent.putExtra(Intent.EXTRA_TEXT, body);
        shareIntent.setType("text/plain");
        startActivity(shareIntent);
    }
}
