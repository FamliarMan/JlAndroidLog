package com.jianglei.jllog;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * @author jianglei
 */

public class JlBaseActivity extends AppCompatActivity {
    private LinearLayout mainLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState );
        super.setContentView(R.layout.jl_activity_base);
        mainLayout = (LinearLayout) findViewById(R.id.parent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.jl_toolbar_color));

        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.jl_white));
        setSupportActionBar(toolbar);

    }
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View view = LayoutInflater.from(this).inflate(layoutResID, mainLayout, false);
        mainLayout.addView(view);
    }

}
