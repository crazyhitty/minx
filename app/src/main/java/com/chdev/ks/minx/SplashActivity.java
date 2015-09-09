package com.chdev.ks.minx;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.balysv.materialripple.MaterialRippleLayout;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SplashActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    //Extending this class with FragmentActivity because Fullscreen(in manifest theme) doesn't work with ActionBarActivity/AppCompat
    @Bind(R.id.splashViewPager)
    ViewPager splashViewPager;
    @Bind(R.id.ripplePrev)
    MaterialRippleLayout ripplePrev;
    @Bind(R.id.rippleNext)
    MaterialRippleLayout rippleNext;
    @Bind(R.id.rippleOk)
    MaterialRippleLayout rippleOk;
    @Bind(R.id.indicator)
    ImageView indicator;
    private int mPosition = 0;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private String mSharedPref="APP_INIT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        splashViewPager.setAdapter(new SplashViewPagerAdapter(getSupportFragmentManager()));
        splashViewPager.setOnPageChangeListener(this);
        splashViewPager.setOffscreenPageLimit(4);

        ripplePrev.setVisibility(View.GONE);
        rippleNext.setVisibility(View.VISIBLE);

        rippleNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                splashViewPager.setCurrentItem(mPosition + 1, true);
            }
        });
        ripplePrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                splashViewPager.setCurrentItem(mPosition - 1, true);
            }
        });
        rippleOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSharedPreferences = getSharedPreferences(mSharedPref, Context.MODE_PRIVATE);
                mEditor=mSharedPreferences.edit();
                mEditor.putString("install_success", "true");
                mEditor.commit();
                Intent intent=new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        try {
            mPosition = position;
            switch (position){
                case 0:indicator.setImageResource(R.drawable.indicator_1);
                    ripplePrev.setVisibility(View.GONE);
                    rippleNext.setVisibility(View.VISIBLE);
                    rippleOk.setVisibility(View.GONE);
                    break;
                case 1:indicator.setImageResource(R.drawable.indicator_2);
                    ripplePrev.setVisibility(View.VISIBLE);
                    rippleNext.setVisibility(View.VISIBLE);
                    rippleOk.setVisibility(View.GONE);
                    break;
                case 2:indicator.setImageResource(R.drawable.indicator_3);
                    ripplePrev.setVisibility(View.VISIBLE);
                    rippleNext.setVisibility(View.VISIBLE);
                    rippleOk.setVisibility(View.GONE);
                    break;
                case 3:indicator.setImageResource(R.drawable.indicator_4);
                    ripplePrev.setVisibility(View.VISIBLE);
                    rippleNext.setVisibility(View.GONE);
                    rippleOk.setVisibility(View.VISIBLE);
                    break;
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
