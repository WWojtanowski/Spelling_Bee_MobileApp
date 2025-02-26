package com.example.spellingbee.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.spellingbee.MainActivity;
import com.example.spellingbee.R;
import com.example.spellingbee.models.Music;
import com.example.spellingbee.models.Settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SettingsFragment to klasa typu 'Fragment', która wyświetla widok ustawień.
 * W tym fragmencie użytkownik może zmienić:
 * <ul>
 *     <li>Nazwę gracza,</li>
 *     <li>Głośność muzyki,</li>
 *     <li>Głośność efektów dźwiękowych,</li>
 *     <li>Motywu kolorystycznego gry.</li>
 * </ul>
 * Przy zmianie ustawień one automatycznie zostają zapisane w instancji settings w MainActivity.
 *
 * @author      Wiktor Wojtanowski
 * @see         Settings
 * @see         MainActivity
 * @version     1.2
 * @since       1.2
 */
public class SettingsFragment extends Fragment {

    /**
     * Pole settings to instancja klasy Settings, który reprezentuje ustawienia aplikacji.
     * @see     Settings
     * @since   1.2
     */
    private Settings settings = null;

    /**
     * Pole music to instancja klasy Music, ktory reprezentuje muzykę graną w aplikacji.
     * @see     Music
     * @since   1.2
     */
    private Music music = null;

    /**
     * name to zmienialne pole tekstowe typu EditText, w którym użytkownik może zmienić nazwę gracza.
     * @since   1.2
     */
    private EditText name;

    /**
     * musicBar to obiekt typu SeekBar, którego można przesunąć w celu zmiany głośności muzyki.
     * @since   1.2
     */
    private SeekBar musicBar;

    /**
     * sfxBar to obiekt typu SeekBar, którego można przesunąć w celu zmiany głośności efektów dźwiękowych.
     * @since   1.2
     */
    private SeekBar sfxBar;

    /**
     * colorBtns to lista przycisków motywu kolorystycznego:
     * <ul>
     *     <li>Pomarańczowy,</li>
     *     <li>Zielony,</li>
     *     <li>Purpurowy,</li>
     *     <li>Czerwony,</li>
     *     <li>Niebieski.</li>
     * </ul>
     * @since   1.2
     */
    private List<Button> colorBtns = new ArrayList<>();

    /**
     * motiveTxt to pole tekstowe typu TextView, który pokazuje nazwę aktualnego motywu kolorystycznego gry.
     * @since   1.2
     */
    private TextView motiveTxt;

    /**
     * Metoda onCreateView() pochodząca z klasy 'Fragment' wykonuje kod przy tworzeniu widoku ustawień.
     * Ta metoda:
     * <ul>
     *     <li>Pobiera instancje klas Music i Settings z MainActivity,</li>
     *     <li>Nasłuchuje w tym widoku:
     *         <ul>
     *             <li>name - za każdą zmaną w tym polu zamienia w settings nazwę gracza,</li>
     *             <li>musicBar i sfxBar - jak SeekBar jest przesunięty, to muzyka się podgłaśnia albo scisza,</li>
     *             <li>colorBtns - jak naciśnięty zostanie jeden z przycisków motywu, to motyw kolorystyczny gry się zmienia.</li>
     *         </ul>
     *     </li>
     * </ul>
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return  Widok ustawień
     * @see     MainActivity
     * @see     SeekBar
     * @since   1.2
     */
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        settings = mainActivity.settings;
        music = mainActivity.music;

        name = view.findViewById(R.id.nameEdit);
        musicBar = view.findViewById(R.id.musicBar);
        sfxBar = view.findViewById(R.id.sfxBar);
        colorBtns.addAll(Arrays.asList(
                view.findViewById(R.id.btnOrange),
                view.findViewById(R.id.btnGreen),
                view.findViewById(R.id.btnPurple),
                view.findViewById(R.id.btnRed),
                view.findViewById(R.id.btnBlue)
        ));
        motiveTxt = view.findViewById(R.id.motiveTxt);
        setMotiveTxt();

        name.setOnKeyListener((v, keyCode, event) -> {
            settings.setUsername(name.getText().toString());
            return true;
        });

        musicBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                settings.setMusicVol(progress);
                music.setMusic(progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        sfxBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                settings.setSfxVol(progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        for (Button btn : colorBtns) {btn.setOnClickListener(v -> setMotive(btn));}

        setSettingsValues();
        return view;
    }

    /**
     * Metoda setMotive() zmienia motyw kolorystyczny aplikacji klikając na dany przycisk motywu kolorystycznego.
     * @param btn   Naciśnięty przycisk motywu kolorystycznego
     * @since       1.2
     */
    private void setMotive(Button btn) {
        int btnIdx = colorBtns.indexOf(btn);
        int theme = R.style.Theme_Orange;
        switch(btnIdx) {
            case 1:
                theme = R.style.Theme_Green; break;
            case 2:
                theme = R.style.Theme_Purple; break;
            case 3:
                theme = R.style.Theme_Red; break;
            case 4:
                theme = R.style.Theme_Blue; break;
        }
        settings.setColMotive(theme);
        setMotiveTxt();
    }

    /**
     * Metoda setMotiveTxt() ustawia pole TextView 'motiveTxt', który pokazuje nazwę motywu kolorystycznego.
     * @since   1.2
     */
    private void setMotiveTxt() {
        Integer motive = settings.getColMotive();
        String motTxt = "";

        if (motive == R.style.Theme_Orange)
            motTxt = "POMARAŃCZOWY";
        else if (motive == R.style.Theme_Green)
            motTxt = "ZIELONY";
        else if (motive == R.style.Theme_Purple)
            motTxt = "PURPUROWY";
        else if (motive == R.style.Theme_Red)
            motTxt = "CZERWONY";
        else if (motive == R.style.Theme_Blue)
            motTxt = "NIEBIESKI";

        motiveTxt.setText(motTxt);
    }

    /**
     * Metoda setSettingsValues() ustawia wszystkie obiekty w widoku ustawień zgodnie z wartościami w instacji ustawień.
     * @since   1.2
     */
    private void setSettingsValues() {
        name.setText(settings.getUsername());
        musicBar.setProgress(settings.getMusicVol());
        sfxBar.setProgress(settings.getSfxVol());
    }
}