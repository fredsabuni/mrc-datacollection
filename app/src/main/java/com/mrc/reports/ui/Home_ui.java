package com.mrc.reports.ui;


import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mrc.reports.R;
import com.mrc.reports.utils.AccountUtils;

public class Home_ui extends AppCompatActivity {

    RelativeLayout mReportsBtn;
    TextView mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_ui);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        mUsername = findViewById(R.id.user_name);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home_ui.this, Settings_ui.class));
            }
        });

        mReportsBtn = findViewById(R.id.reports_btn);

        mReportsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home_ui.this, Reports_ui.class));
            }
        });

        mUsername.setText(AccountUtils.getUser(this));

    }

}
