package com.example.spellingbee.models;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.spellingbee.R;

/**
 * Klasa Music jest odpowiedzialny za ładowanie muzyki, jej granie i zatrzymanie.
 * Wszystkie te operacje są wykonywane z pomocą klasy MediaPlayer.
 * Ta klasa zawiera jedną pisoenkę muzyczną (mainMusic) i cztery efekty dźwiękowe:
 * <ul>
 *     <li>startGame - dla rozpoczęcia gry,</li>
 *     <li>endGame - dla zakończenia gry,</li>
 *     <li>right - dla prawidłowo ułożonego słowa,</li>
 *     <li>wrong - dla nieprawidłowo ułożonego słowa,</li>
 *     <li>tickTock - jak do końca gry zostało mniej niż 10 sekund.</li>
 * </ul>
 *
 * @author      Wiktor Wojtanowski
 * @version     %I%, %G%
 * @see         MediaPlayer
 * @since       1.2
 */
public class Music {
    MediaPlayer mainMusic;
    MediaPlayer startGame;
    MediaPlayer endGame;
    MediaPlayer right;
    MediaPlayer wrong;
    MediaPlayer tickTock;

    public Music(Context context) {
        mainMusic = MediaPlayer.create(context, R.raw.kevin_macleod_silver_blue_light);
        startGame = MediaPlayer.create(context, R.raw.start_game);
        endGame = MediaPlayer.create(context, R.raw.end_game);
        right = MediaPlayer.create(context, R.raw.right);
        wrong = MediaPlayer.create(context, R.raw.wrong);
        tickTock = MediaPlayer.create(context, R.raw.tick_tock);
    }

    public void setMusic (int vol) {
        mainMusic.setVolume(vol/100.0f, vol/100.0f);
    }

    public void playMainMusic(int vol) {
        mainMusic.setVolume(vol/100.0f, vol/100.0f);
        mainMusic.start();
        mainMusic.setLooping(true);
    }

    public void pause() {
        mainMusic.pause();
    }

    public void playStartGame(int vol) {
        startGame.setVolume(vol/100.0f, vol/100.0f);
        startGame.start();
    }

    public void playEndGame(int vol) {
        endGame.setVolume(vol/100.0f, vol/100.0f);
        endGame.start();
    }

    public void playRight(int vol) {
        right.setVolume(vol/100.0f, vol/100.0f);
        right.start();
    }

    public void playWrong(int vol) {
        wrong.setVolume(vol/100.0f, vol/100.0f);
        wrong.start();
    }

    public void playTickTock(int vol) {
        tickTock.setVolume(vol/100.0f, vol/100.0f);
        tickTock.start();
    }
}
