package com.somenameofpackage.internetradiowithmosby.model.db;

import dagger.Module;
import dagger.Provides;

@Module
public class RadioStationModule {
    @Provides
    RadioStations provideRadioStations() {
        return new RadioStations();
    }
}
