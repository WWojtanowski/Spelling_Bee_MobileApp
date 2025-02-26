package com.example.spellingbee.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.spellingbee.MainActivity;
import com.example.spellingbee.R;
import com.example.spellingbee.models.Settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * HomeFragment to klasa typu 'Fragment', która wyświetla widok główny.
 * Na środku tego widoku widnieją siedem przycisków:
 * <ul>
 *     <li>Środkowy przycisk jest odpowiedzialny za uruchomienie gry Spelling Bee,</li>
 *     <li>Sześć innych przycisków ustaiwają dany poziom trudności gry.</li>
 * </ul>
 * Poza przyciskami jest także główna nazwa aplikacji i pole TextView z aktualnie ustawionym poziomem trudności.
 *
 * @author      Wiktor Wojtanowski
 * @version     1.2
 * @since       1.0
 */
public class HomeFragment extends Fragment {

    /**
     * difficBtns to lista przycisków poziomu trudności z widoku głównego.
     * @since   1.2
     */
    List<Button> difficBtns;

    /**
     * currDiffic przechowuje wartość aktualnie ustawionego poziomu trudności.
     * @since   1.2
     */
    String currDiffic;

    /**
     * difficTxt to pole tekstowe typu TextView, który pokazuje aktualnie ustawiony poziom trudnosci.
     * @since   1.2
     */
    TextView difficTxt;

    /**
     * Metoda onCreateView() pochodząca z klasy 'Fragment' wykonuje kod przy tworzeniu widoku.
     * Ta metoda:
     * <ul>
     *     <li>Pobiera ustawienia z głównego activity,</li>
     *     <li>Ustawia motyw i widok fragmentu przy użyciu klasy LayoutInflater,</li>
     *     <li>Nasłuchuje wszystkie przyciski w widoku.</li>
     * </ul>
     * Kliknięcie środkowego przycisku powoduje wywołanie funkcji playGame() w MainActivity,
     * a kliknięcie zewnętrznych przycisków wywołuje lokalną metodę setDifficulty().
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return  Widok główny
     * @see     Fragment
     * @see     MainActivity
     * @see     LayoutInflater
     * @since   1.0
     */
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MainActivity mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        Settings settings = mainActivity.settings;

        LayoutInflater themedInflater = LayoutInflater.from(
                new ContextThemeWrapper(getContext(), settings.getColMotive()));
        View view = themedInflater.inflate(R.layout.fragment_home, container, false);

        difficTxt = view.findViewById(R.id.difficTxt);
        difficTxt.setText("BANALNY");
        currDiffic = "B";
        difficBtns = new ArrayList<>();

        Button playBtn = view.findViewById(R.id.playBtn);
        difficBtns.addAll(Arrays.asList(
                view.findViewById(R.id.diffBnBtn),
                view.findViewById(R.id.diffLtBtn),
                view.findViewById(R.id.diffSrBtn),
                view.findViewById(R.id.diffTrBtn),
                view.findViewById(R.id.diffEkBtn),
                view.findViewById(R.id.diffLBtn)
        ));

        for (Button btn : difficBtns) {btn.setOnClickListener(v -> setDifficulty(btn));}
        playBtn.setOnClickListener((clickView) -> mainActivity.playGame(currDiffic));

        return view;
    }

    /**
     * Metoda setDifficulty() ustawia poziom trudności i wstawia w widoku
     * pole tekstowe poziomu trudności pod nazwą aplikacji.
     * @param btn   Naciśnięty przycisk poziomu trudności
     * @see         com.example.spellingbee.MainActivity.Difficulty
     * @since       1.2
     */
    private void setDifficulty(Button btn) {
        String newDiffic = MainActivity.Difficulty.values()[difficBtns.indexOf(btn)].toString();
        difficTxt.setText(newDiffic);
        currDiffic = newDiffic.substring(0,1);
    }
}