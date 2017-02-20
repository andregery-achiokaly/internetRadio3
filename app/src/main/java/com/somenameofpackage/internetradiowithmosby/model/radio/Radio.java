package com.somenameofpackage.internetradiowithmosby.model.radio;

import android.media.AudioManager;
import android.media.MediaPlayer;

import com.somenameofpackage.internetradiowithmosby.model.db.Station;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.Status;

import java.io.IOException;

import rx.Subscriber;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;

public class Radio implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    private MediaPlayer mediaPlayer;
    private String currentSource = "";
    private PublishSubject<Status> statusObserver = PublishSubject.create();
    private ReplaySubject<Integer> radioIdObserver = ReplaySubject.createWithSize(1);

    PublishSubject<Status> getRadioModelStatusObservable() {
        return statusObserver;
    }

    ReplaySubject<Integer> getRadioIdObservable() {
        return radioIdObserver;
    }

    private void initPlayer(String source) {
        currentSource = source;
        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(source);
        } catch (IOException | NullPointerException e) {
            currentSource = "";
            closeMediaPlayer();
            statusObserver.onNext(Status.Error);
        }

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.prepareAsync();
        statusObserver.onNext(Status.isPlay);
        radioIdObserver.onNext(mediaPlayer.getAudioSessionId());
    }

    private void changeSource(String source) {
        currentSource = source;
        mediaPlayer.stop();
        mediaPlayer.reset();

        try {
            mediaPlayer.setDataSource(source);
        } catch (IOException e) {
            e.printStackTrace();
            closeMediaPlayer();
            statusObserver.onNext(Status.Error);
        }

        mediaPlayer.prepareAsync();
        statusObserver.onNext(Status.isPlay);
    }

    void setChangePlaySubject(PublishSubject<Station> changeStateSubject) {
        changeStateSubject
                .filter(station -> station.isLoaded() && station.isValid())
                .subscribe(new Subscriber<Station>() {
                               @Override
                               public void onCompleted() {
                               }

                               @Override
                               public void onError(Throwable e) {
                                   statusObserver.onNext(Status.Error);
                               }

                               @Override
                               public void onNext(Station station) {
                                   String source = station.getSource();
                                   statusObserver.onNext(Status.Wait);

                                   if (mediaPlayer == null) {
                                       initPlayer(source);
                                   } else {
                                       if (!station.getSource().equals(currentSource)) {
                                           changeSource(source);
                                       } else {
                                           if (mediaPlayer.isPlaying()) {
                                               statusObserver.onNext(Status.isStop);
                                               mediaPlayer.pause();
                                           } else {
                                               statusObserver.onNext(Status.isPlay);
                                               mediaPlayer.start();
                                           }
                                       }
                                   }
                               }
                           }
                );
    }

    void closeMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            statusObserver.onNext(Status.isStop);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        statusObserver.onNext(Status.isPlay);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        statusObserver.onNext(Status.Error);
        return true;
    }
}
