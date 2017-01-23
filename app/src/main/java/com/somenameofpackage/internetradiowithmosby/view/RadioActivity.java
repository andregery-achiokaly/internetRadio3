package com.somenameofpackage.internetradiowithmosby.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.somenameofpackage.internetradiowithmosby.R;

import butterknife.ButterKnife;

public class RadioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new RadioFragment())
                    .commit();
        }

        ButterKnife.bind(this);
    }
}