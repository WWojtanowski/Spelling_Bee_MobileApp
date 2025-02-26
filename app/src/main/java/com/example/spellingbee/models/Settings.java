package com.example.spellingbee.models;

import com.example.spellingbee.R;

/**
 * Klasa Settings reprezentuje ustawienia tej aplikacji.
 * Użytkownik może zmieniać następujace ustawienia:
 * <ul>
 *     <li>Nazwa gracza,</li>
 *     <li>Głośność muzyki,</li>
 *     <li>Głośność efektów specjalnych,</li>
 *     <li>Motyw kolorystyczny aplikacji.</li>
 * </ul>
 * Te ustawienia mogą być zmieniane w fragmencie SettingsFragment
 *
 * @author      Wiktor Wojtakowski
 * @version     %I%, %G%
 * @see         com.example.spellingbee.fragments.SettingsFragment
 * @since       1.2
 */
public class Settings {
    private String username;
    private Integer musicVol;
    private Integer sfxVol;
    private Integer colMotive;

    public Settings() {
        username = "Gracz";
        musicVol = 80;
        sfxVol = 80;
        colMotive = R.style.Theme_Orange;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getMusicVol() {
        return musicVol;
    }

    public void setMusicVol(Integer musicVol) {
        this.musicVol = musicVol;
    }

    public Integer getSfxVol() {
        return sfxVol;
    }

    public void setSfxVol(Integer sfxVol) {
        this.sfxVol = sfxVol;
    }

    public Integer getColMotive() {
        return colMotive;
    }

    public void setColMotive(Integer colMotive) {
        this.colMotive = colMotive;
    }
}
