package com.example.spellingbee.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.spellingbee.GameActivity;
import com.example.spellingbee.R;
import com.example.spellingbee.models.Game;
import com.example.spellingbee.models.Music;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * GameFragment to klasa typu 'Fragment', która odpowiada za działanie gry do ostatniej sekundy.
 * W każdej metodzie tej klasy występuje komunikacja pomiędzy modelem gry i widokiem gry.
 * Widok fragmentu gry zawierają:
 * <ul>
 *     <li>Pole słowa (typu TextView), który wyświetla ułożone słowo</li>
 *     <li>Siedem przycisków z literami (i ich wartości punktowej) na planszy,</li>
 *     <li>Przyciski 'Cofnij' i 'Sprawdź',</li>
 *     <li>Ostatnio odgadnięte słowo z wartością punktową,</li>
 *     <li>Suma punktów od każdego odgadniętego słowa.</li>
 * </ul>
 *
 * @author      Wiktor Wojtanowski
 * @version     1.2
 * @since       1.0
 */
public class GameFragment extends Fragment {

    /**
     * Pole game to instancja klasy Game pobierana z Activity 'GameActivity'.
     * @see     GameActivity
     * @since   1.0
     */
    Game game = null;

    /**
     * music to instancja klasy Music pobrana z GameActivity.
     * @see     GameActivity
     * @since   1.2
     */
    private Music music;

    /**
     * letterBtns to lista przycisków, które pochodzą z widoku gry.
     * @since   1.0
     */
    List<Button> letterBtns = new ArrayList<>();

    /**
     * word to pole tekstowe typu TextView z widoku gry, który reprezentuje ułożone słowo.
     * @since   1.0
     */
    TextView word;


    /**
     * Metoda onCreateView() wykonuje kod przy tworzeniu widoku.
     * Ta metoda:
     * <ul>
     *     <li>Pobiera instancję gry z GameActivity,</li>
     *     <li>Przypisuje pola do poszczególnych elementów widoku gry,</li>
     *     <li>Nasłuchuje przyciski i kierują do poszczególnych funkcji przy kliknięciu.</li>
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
     * @return  Widok gry
     * @see     Fragment
     * @see     GameActivity
     * @since   1.0
     */
    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        GameActivity gameActivity = (GameActivity) getActivity();
        assert gameActivity != null;
        LayoutInflater themedInflater = LayoutInflater.from(
                new ContextThemeWrapper(getContext(), gameActivity.getTheme()));
        View view = themedInflater.inflate(R.layout.fragment_game, container, false);

        game = gameActivity.game;
        music = gameActivity.music;
        int sfxVolume = gameActivity.sfxVolume;

        word = view.findViewById(R.id.wordTxt);
        word.setText("");

        Button checkBtn = view.findViewById(R.id.checkBtn);
        Button backBtn = view.findViewById(R.id.backBtn);
        letterBtns.addAll(Arrays.asList(
                view.findViewById(R.id.centerBtn),
                view.findViewById(R.id.edgeBtn1),
                view.findViewById(R.id.edgeBtn2),
                view.findViewById(R.id.edgeBtn3),
                view.findViewById(R.id.edgeBtn4),
                view.findViewById(R.id.edgeBtn5),
                view.findViewById(R.id.edgeBtn6)
        ));
        setLtrButtons();

        for (Button btn : letterBtns) {btn.setOnClickListener(v -> addLetter(btn));}
        checkBtn.setOnClickListener(v -> checkWord(view, sfxVolume));
        backBtn.setOnClickListener(v -> delLetter());

        return view;
    }

    /**
     * Metoda addLetter() dodaje na koniec słowa literę, która jest widoczna na przycisku.
     * Ta metoda zostaje wywołana, gdy zostanie naciśnięty jeden z siedmiu przycisków na planszy.
     *
     * @param btn   Naciśnięty przycisk
     * @since       1.0
     */
    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    private void addLetter(Button btn) {
        String wordTxt = (String) word.getText();
        char rawTxt = btn.getText().charAt(0);
        if (wordTxt.length() < 12)
            word.setText(wordTxt+rawTxt);
        word.setBackgroundColor(ContextCompat.getColor(
                getContext(), R.color.grayA));
    }

    /**
     * Metoda checkWord() sprawdza w klasie gry, czy takie słowo istnieje.
     * Jeżeli tak, tło pola 'word' zamienia się na zielone, jak nie, to na czerwone.
     * Przy prawidłowym ułożeniu słowa suma punktowa zostanie dodana i
     * na dole aplikacji będzie pokazane ułożone słowo i jego watość punktowa.
     * Metoda jest wywoływana po naciściędziu przycisku 'Sprawdź'.
     *
     * @param view  Widok gry (używane tylko do pobierania danego elementu)
     * @see         Game
     * @since       1.0
     */
    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    private void checkWord(View view, int sfx) {
        String wordTxtU = (String) word.getText();
        String wordTxt = wordTxtU.toLowerCase(Locale.forLanguageTag("pl-PL"));
        if (game.checkWord(wordTxt)){
            word.setBackgroundColor(ContextCompat.getColor(
                    getContext(), R.color.green));
            music.playRight(sfx);

            TextView lastGuessed = view.findViewById(R.id.lastGuessed);
            TextView sumPoints = view.findViewById(R.id.sumaPkt);

            int points = game.calcPoints(wordTxt);
            lastGuessed.setText(wordTxtU + " -> " + points + " pkt.");
            sumPoints.setText(game.getSumPoints().toString() + " punktów");
        } else {
            word.setBackgroundColor(ContextCompat.getColor(
                    getContext(), R.color.red));
            music.playWrong(sfx);
        }
        word.setText("");
    }

    /**
     * Metoda delLetter() usuwa od końca słowa jedną literę w celu poprawy słowa.
     * Ta metoda jest wywoływana przy naciśnięciu przycisku 'Cofnij'.
     *
     * @since   1.0
     */
    public void delLetter() {
        String wordTxt = (String) word.getText();
        if (wordTxt.length() != 0)
            word.setText(wordTxt.substring(0, wordTxt.length()-1));
    }

    /**
     * Metoda setLtrButtons() służy do ustawiania tekstu przycisków w widoku gry.
     * Ta metoda pobiera wygenerowane przez grę litery i przypisuje je do tekstu danego przycisku.
     * Metoda jest wywoływana tylko przy tworzeniu fragmentu gry.
     * @since   1.1
     */
    @SuppressLint("SetTextI18n")
    private void setLtrButtons() {
        String letters = game.getLetters();
        for (Button btn: letterBtns) {
            char letter = letters.charAt(letterBtns.indexOf(btn));
            btn.setText(String.valueOf(letter).toUpperCase());
        }
    }
}