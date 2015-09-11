package com.chdev.ks.minx;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chdev.ks.minx.DatabaseManager.DataBaseOperations;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.net.URI;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends ActionBarActivity {

    Boolean actionItem = true;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.search_txt)
    EditText search_txt;
    @Bind(R.id.fragment_frame)
    FrameLayout frameLayout;
    Drawer result;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    EditText save_title_txt, save_url_txt;
    /*@Bind(R.id.saved_title_txt)
    EditText save_title_txt;
    @Bind(R.id.saved_url_txt)
    EditText save_url_txt;*/
    private String search_str, cancel_str, exit_dialog_title_str, exit_dialog_yes_str, exit_dialog_cancel_str, finTitle, finUrl, finBody;
    private int loadSearchType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ErrorActivity.install(this);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        search_str = getResources().getString(R.string.action_search);
        cancel_str = getResources().getString(R.string.action_cancel);
        exit_dialog_title_str = getResources().getString(R.string.exit_dialog_title);
        exit_dialog_yes_str = getResources().getString(R.string.exit_dialog_yes);
        exit_dialog_cancel_str = getResources().getString(R.string.exit_dialog_cancel);

        clearSharedPrefs();
        getPublicIntent();
        loadFragment(-1);
        setUpToolbar();
        setUpDrawer();

        search_txt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() != KeyEvent.ACTION_DOWN) {
                    //to stop this goddamn function from running twice
                    if (testInput()) {
                        loadFragment(0);
                    }
                    hideKeyboard();
                    return false;
                }
                return false;
            }
        });
    }

    private boolean testInput() {
        String urlValue = search_txt.getText().toString();
        if (!urlValue.contains("://")) {
            //Toast.makeText(MainActivity.this, "Invalid url, please add http:// or https:// before your url", Toast.LENGTH_SHORT).show();
            urlValue = "http://" + urlValue;
        }
        try {
            URI.create(urlValue);
            search_txt.setText(urlValue);
            return true;
        } catch (IllegalArgumentException e) {
            Toast.makeText(MainActivity.this, "Invalid url", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void hideKeyboard() {
        View view = MainActivity.this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
    }

    private void setUpDrawer() {

        AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(MainActivity.this)
                .withHeaderBackground(R.drawable.minx_small)
                .withHeaderBackgroundScaleType(ImageView.ScaleType.FIT_CENTER)
                .withHeightDp(200)
                .build();

        result = new DrawerBuilder()
                .withHeaderDivider(true)
                .withActivity(this)
                .withAccountHeader(accountHeader)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_saved),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_share),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_settings),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_about)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        Toast.makeText(MainActivity.this, "You clicked on item : " + position, Toast.LENGTH_SHORT).show();
                        loadFragment(position);
                        return false;
                    }
                })
                .build();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);

    }

    private void loadFragment(int position) {
        Boolean switchFragment = true;
        Fragment fragment = new MainFragment();
        switch (position) {
            case 0:
                if (search_txt.getText().toString().isEmpty()) {
                    fragment = new MainFragment();
                } else {
                    fragment = new HomeFragment();
                }
                break;
            case 2:
                fragment = new SavedFragment();
                break;
            case 3:
                switchFragment = false;
                shareData();
                break;
            case 4:
                fragment = new SettingsFragment();
                break;
            case 5:
                fragment = new AboutFragment();
                break;
        }
        if (switchFragment) {
            frameLayout.removeAllViews();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragment_frame, fragment).commit();
        }
    }

    private void shareData() {
        getSharedPrefs();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, finTitle);
        shareIntent.putExtra(Intent.EXTRA_TEXT, finBody);
        shareIntent.setType("text/plain");
        startActivity(shareIntent);
    }

    private void hideSearchTxt() {
        //Hide edittext with keyboard
        //search_txt.setVisibility(View.GONE);
        FadeAnimation fadeAnimation = new FadeAnimation(MainActivity.this);
        fadeAnimation.fadeOutRight(search_txt);
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        //search_txt.setFocusable(false);
    }

    private void showSearchTxt() {
        if (loadSearchType == 1) {
            search_txt.setVisibility(View.VISIBLE);
            search_txt.setFocusable(true);
            search_txt.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(search_txt, InputMethodManager.SHOW_IMPLICIT);
        } else {
            //Show edittext with keyboard and request focus on edittext
            FadeAnimation fadeAnimation = new FadeAnimation(MainActivity.this);
            fadeAnimation.fadeInLeft(search_txt);
            //search_txt.setVisibility(View.VISIBLE);
            //search_txt.setFocusable(true);
            search_txt.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(search_txt, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_item) {
            if (actionItem) {
                showSearchTxt();
                item.setIcon(R.drawable.ic_action_navigation_close);
                item.setTitle(cancel_str);
                actionItem = false;
            } else {
                hideSearchTxt();
                item.setIcon(R.drawable.ic_action_action_search);
                item.setTitle(search_str);
                actionItem = true;
            }
            return true;
        }

        if (id == R.id.action_save) {
            if (sharedPrefsStatus()) {
                promptSaveData();
                Toast.makeText(MainActivity.this, "save", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "No webpage loaded", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void promptSaveData() {
        getSharedPrefs();
        MaterialDialog materialDialog = new MaterialDialog.Builder(MainActivity.this)
                .title(R.string.dialog_save_title)
                .customView(R.layout.save_dialog, true)
                .positiveText(R.string.dialog_save)
                .negativeText(R.string.exit_dialog_cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        new DataBaseOperations(MainActivity.this).saveDataInDB(save_title_txt.getText().toString(), save_url_txt.getText().toString(), finBody);
                        Toast.makeText(MainActivity.this, save_title_txt.getText().toString() + "\n" + save_url_txt.getText().toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                    }
                }).build();
        save_title_txt = (EditText) materialDialog.getView().findViewById(R.id.saved_title_txt);
        save_url_txt = (EditText) materialDialog.getView().findViewById(R.id.saved_url_txt);
        save_title_txt.setText(finTitle);
        save_url_txt.setText(finUrl);
        materialDialog.show();
    }

    private Boolean sharedPrefsStatus() {
        try {
            sharedPreferences = getSharedPreferences(getResources().getString(R.string.sharedPrefs), Context.MODE_PRIVATE);
            if (sharedPreferences.getString("url", null).equals("")) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void getSharedPrefs() {
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.sharedPrefs), Context.MODE_PRIVATE);
        finTitle = sharedPreferences.getString("title", null);
        finUrl = sharedPreferences.getString("url", null);
        finBody = sharedPreferences.getString("body", null);
    }

    private void clearSharedPrefs() {
        try {
            sharedPreferences = getSharedPreferences(getResources().getString(R.string.sharedPrefs), Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.clear().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getSearchTxt() {
        return search_txt.getText().toString();
    }

    //For some reason this reloads the fragment again and again, will fix this later
    /*@Override
    public void onBackPressed() {
        //Don't use super.onBackPressed() if you want to override the default back button(h/w) completely
        //super.onBackPressed();
        showExitPrompt();
    }

    private void showExitPrompt() {

    }*/

    public void getPublicIntent() {
        String url = null;
        Intent intent = getIntent();
        try {
            if (intent != null) {
                Uri data = intent.getData();
                String scheme = data.getScheme();
                String fullPath = data.getEncodedSchemeSpecificPart();
                fullPath = fullPath.replace("//", "");
                url = scheme + "://" + fullPath;
            }
            if (url != null) {
                loadSearchType = 1;
                search_txt.setText(url);
                showSearchTxt();
            } else {
                Toast.makeText(MainActivity.this, "Invalid Url", Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public String getTextFromIntent(Intent intent) {
        return intent.getStringExtra(Intent.EXTRA_TEXT);
    }
}
