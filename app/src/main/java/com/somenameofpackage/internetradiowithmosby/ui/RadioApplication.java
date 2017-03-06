package com.somenameofpackage.internetradiowithmosby.ui;

import android.app.Application;
import android.content.Intent;

import com.somenameofpackage.internetradiowithmosby.AppComponent;
import com.somenameofpackage.internetradiowithmosby.DaggerAppComponent;
import com.somenameofpackage.internetradiowithmosby.model.db.sqliteDB.SqliteModule;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioService;

public class RadioApplication extends Application {
    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.builder().sqliteModule(new SqliteModule(RadioApplication.this)).build();
        getApplicationContext().startService(new Intent(getApplicationContext(), RadioService.class));
    }

    public static AppComponent getComponent() {
        return component;
    }
}
