package com.example.spellingbee;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.example.spellingbee.fragments.GameOverFragment;
import com.example.spellingbee.models.Game;
import com.example.spellingbee.models.Music;

/**
 * GameActivity to klasa typu 'Activity', która jest odpowiedzialna za utworzenie danej instancji gry.
 * Przy utworzeniu gry i ustawieniu widoku gry odliczony jest czas gry (timeLeft), w którym
 * jeżeli jego czas wynosi 0, to klasa gry przełącza się pomiędzy fragmentami: z fragmentu gry
 * do fragmentu końca gry.
 * W trakcie działania tej klasy nie można wyjść do klasy głównej (przycisk Back jest wyłączony).
 *
 * @author      Wiktor Wojtanowski
 * @version     %I%, %G%
 * @see         com.example.spellingbee.fragments.GameFragment
 * @see         GameOverFragment
 * @since       1.0
 */
public class GameActivity extends AppCompatActivity {

    /**
     * Pole game to instancja klasy Game, która jest inicjalizowana w trakcie tworzenia klasy gry.
     * Fragmenty gry i końca gry będą używać tej instancji, więc ono jest publiczne.
     *
     * @see Game
     * @since   1.0
     */
    public Game game;

    /**
     * Pole music to instancja klasy Music, ktory reprezentuje muzykę graną w aplikacji.
     * @see     Music
     * @since   1.2
     */
    public Music music;

    /**
     * Pole numeryczne timeLeft zachowuje liczbę sekund pozostałego do końca gry.
     * Po każdej sekundzie liczba się zmniejsza o 1.
     * @since   1.0
     */
    private int timeLeft = 120;

    /**
     * Pole sfxVolume zachowuje głośność efektów dźwiękowych granych w grze
     * @since   1.2
     */
    public int sfxVolume;

    /**
     * Metoda onCreate() wykonuje kod w trakcie tworzenia klasy gry.
     * W metodzie:
     * <ul>
     *     <li>Pobierane są wszystkie wartości wysyłane przez MainActivity</li>
     *     <li>Ustawiany jest widok i motyw gry,</li>
     *     <li>Tworzona jest nowa instancja gry,</li>
     *     <li>Nowa instancja klasy Music zostaje tworzona,</li>
     *     <li>Uruchamiany jest licznik gry,</li>
     *     <li>Przycisk 'Back' zostaje wyłączony.</li>
     * </ul>
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     * @see     Bundle
     * @see     AppCompatActivity
     * @see     OnBackPressedCallback
     * @since   1.2
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        String difficulty = extras.getString("difficulty", "B");
        int theme = extras.getInt("theme", R.style.Theme_Orange);
        sfxVolume = extras.getInt("sfx", 80);

        super.onCreate(savedInstanceState);
        setTheme(theme);
        setContentView(R.layout.activity_game);

        game = new Game(difficulty);
        music = new Music(this);
        music.playStartGame(sfxVolume);
        refreshTimer();
        tick();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //...
            }
        });
    }

    private final Handler handler = new Handler();
    /**
     * Metoda tick() to licznik, który odlicza czas gry, a zarazem odświeża tekst 'gameTimer' w widoku gry.
     * Jeżeli zostanie tylko 9 sekund czasu, efekt dźwiękowy tykającego zegara zostanie odpalony.
     * Gdy czas gry wyniesie 0, to licznik się zatrzymuje zarazem wywołując metodę gameOver().
     *
     * @see     Handler
     * @since   1.2
     */
    private void tick() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (timeLeft>0) {
                    timeLeft--;
                    refreshTimer();
                    tick();
                    if (timeLeft == 9)
                        music.playTickTock(sfxVolume);
                } else
                    gameOver();
            }
        }, 1000);
    }

    /**
     * Metoda refreshTimer() ma za zadanie odświeżać tekst 'gameTimer' w widoku gry.
     * Metoda oblicza minuty i resztę sekund z timeLeft i ustawia tekst w postaci 'm:ss'.
     * @since   1.0
     */
    private void refreshTimer() {
        TextView timer = findViewById(R.id.gameTimer);
        String minutes = String.valueOf(timeLeft/60);
        String seconds = String.valueOf(timeLeft%60);
        if (timeLeft%60 < 10)
            seconds = "0" + seconds;
        timer.setText(minutes + ":" + seconds);
    }

    /**
     * Metoda gameOver() jest wywoływana, gdy czas gry wyniesie 0.
     * Wtedy fragment gry zostanie usunięty, a w jego miejsce będzie tworzony fragment końca gry.
     *
     * @see     androidx.fragment.app.FragmentManager
     * @since   1.0
     */
    private void gameOver() {
        music.playEndGame(sfxVolume);
        game.setActive(false);
        FragmentContainerView fcv = findViewById(R.id.fragmentContainerView);
        fcv.removeAllViews();
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragmentContainerView, GameOverFragment.class, null)
                .commit();
    }
}