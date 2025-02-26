package com.example.spellingbee.fragments;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.spellingbee.GameActivity;
import com.example.spellingbee.R;
import com.example.spellingbee.adapters.GuessedWordAdapter;
import com.example.spellingbee.models.Game;

/**
 * GameOverFragment to klasa typu 'Fragment', która odpowiada za wyświetlenie wyników po zakończonej grze.
 * Klasa wyświetla:
 * <ul>
 *     <li>Końcową sumę punktów</li>
 *     <li>Listę odgadniętych słów</li>
 *     <li>Listę możliwych słów do odgadnięcia</li>
 * </ul>
 * Wszystkie listy będą sortowane od najwyższej liczby punktów do najmniejszej.
 * Poza wyświetleniem wyników w widoku końca gry znajdują się dwa przyciski wyjścia z GameActivity:
 * jedna zapisuje wynik gry do rankingu, druga nie zapisuje.
 *
 * @author      Wiktor Wojtanowski
 * @version     1.2
 * @since       1.0
 */
public class GameOverFragment extends Fragment {

    /**
     * Pole game to instancja klasy Game pobierana z Activity 'GameActivity'.
     * @see     GameActivity
     * @since   1.0
     */
    Game game = null;

    /**
     * Metoda onCreateView() wykonuje kod przy tworzeniu widoku.
     * Metoda pobiera instancję zakończonej gry i wyświetla sumę punktów w polu TextView i
     * dwie listy (guessedWords i possibleWords) w RecyclerView.
     * Jest możliwość wyjścia z GameActivity z albo bez zapisu do rankingu.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     * @return      Widok końca gry
     * @see         Fragment
     * @see         GameActivity
     * @see         RecyclerView
     * @since       1.0
     */
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        GameActivity gameActivity = (GameActivity) getActivity();
        assert gameActivity != null;
        LayoutInflater themedInflater = LayoutInflater.from(
                new ContextThemeWrapper(getContext(), gameActivity.getTheme()));
        View view = themedInflater.inflate(R.layout.fragment_game_over, container, false);

        game = gameActivity.game;

        TextView endSum = view.findViewById(R.id.endSumaPkt);
        endSum.setText("Suma punktów: " + game.getSumPoints() + " pkt.");

        RecyclerView gwRecyclerView = view.findViewById(R.id.gwRecycler);
        gwRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager gwLayoutManager = new LinearLayoutManager(getContext());
        gwRecyclerView.setLayoutManager(gwLayoutManager);

        RecyclerView pwRecyclerView = view.findViewById(R.id.pwRecycler);
        pwRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager pwLayoutManager = new LinearLayoutManager(getContext());
        pwRecyclerView.setLayoutManager(pwLayoutManager);

        RecyclerView.Adapter gwAdapter = new GuessedWordAdapter(game.getGuessedWords());
        gwRecyclerView.setAdapter(gwAdapter);

        RecyclerView.Adapter pwAdapter = new GuessedWordAdapter(game.getPossibleWords());
        pwRecyclerView.setAdapter(pwAdapter);

        Button rankSaveBtn = view.findViewById(R.id.rankSaveBtn);
        Button exitGameBtn = view.findViewById(R.id.exitGameBtn);
        rankSaveBtn.setOnClickListener(V -> exitWithRank(gameActivity));
        exitGameBtn.setOnClickListener(v -> exitGame(gameActivity));

        return view;
    }

    /**
     * Metoda exitGame() powoduje zakończenie klasy GameActivity bez zwrotu wyniku gry.
     * Metoda jest wywoływana poprzez naciśnięcie przycisku 'Wyjdź z gry'.
     * @since   1.0
     */
    private void exitGame(GameActivity gameActivity) {
        gameActivity.finish();
    }

    /**
     * Metoda exitWithRank() powoduje zakończenie klasy GameActivity z zwrotem wyniku gry.
     * Zostają zwrócone: poziom trudności, sekwencja liter i końcowa suma punktowa.
     * Metoda jest wywoływana poprzez naciśnięcie przycisku 'Zapisz wynik do rankingu'.
     * @since   1.2
     */
    private void exitWithRank(GameActivity gameActivity) {
        Intent intent = new Intent();
        intent.putExtra("sequence", game.getLetters());
        intent.putExtra("points", game.getSumPoints() + " pkt.");
        intent.putExtra("difficulty", game.getDifficulty());
        gameActivity.setResult(RESULT_OK, intent);
        gameActivity.finish();
    }
}