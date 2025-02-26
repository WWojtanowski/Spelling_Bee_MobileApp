package com.example.spellingbee;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.spellingbee.cards.Ranking;
import com.example.spellingbee.models.Music;
import com.example.spellingbee.models.Settings;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Main Activity to główna klasa typu 'Activity', która jest odpalana przy inicjalizacji aplikacji.
 * Ta klasa tworzy główny widok aplikacji i ładuje listę słów (wordLibrary).
 * Widok głównej klasy jest dzielony na trzy fragmenty:
 * <ul>
 *     <li>Fragment główny (Home)</li>
 *     <li>Fragment rankingowy (Ranking)</li>
 *     <li>Fragment ustawień (Settings)</li>
 * </ul>
 * Domyślnym fragmentem przy włączeniu aplikacji, to fragment główny.
 * Klasa zawiera także obsługę pauzy i wznowienia, i może przechowywać listę rankingową i ustawienia.
 *
 * @author      Wiktor Wojtanowski
 * @version     %I%, %G%
 * @see         com.example.spellingbee.fragments.HomeFragment
 * @since       1.2
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Pole rankingList to lista rankingowa aplikacji, która jest dodawana
     *  po zakończeniu GameActivity. Zostaje zapisany w SharedPreferences.
     * @see     Ranking
     * @since   1.2
     */
    public ArrayList<Ranking> rankingList;

    /**
     * Pole settings to instancja klasy Settings, który reprezentuje ustawienia aplikacji.
     * One zostają zapisane w SharedPreferences wraz z listą rankingową.
     * @see     Settings
     * @since   1.2
     */
    public Settings settings;

    /**
     * Pole music to instancja klasy Music, ktory reprezentuje muzykę graną w aplikacji.
     * @see     Music
     * @since   1.2
     */
    public Music music;

    /**
     * Pole statyczne wordLibrary to lista wszystkich polskich słów,
     * która jest generowana przez metodę 'generateLibrary()'.
     * @since       1.0
     */
    public static List<String> wordLibrary;

    /**
     * Pole prywatne gameARL jest odpowiedzialny za odpalanie GameActivity.
     * Po jego zakończeniu to pole spróbuje wykryć zwrócone wartości z tej klasy.
     * @see     ActivityResultLauncher
     * @since   1.2
     */
    private ActivityResultLauncher<Intent> gameARL;

    /**
     * Pole publiczna Difficulty jest typem 'enum', w którym przechowuje wszystkie poziomy trudności.
     * @since   1.2
     */
    public enum Difficulty {BANALNY, ŁATWY, ŚREDNI, TRUDNY, EKSTREMALNY, LOSOWY};

    /**
     * Metoda onCreate pochodząca z klasy 'AppCompactActivity' umożliwia wykonywanie
     * kodu w trakcie tworzenia klasy MainActivity.
     * W metodzie:
     * <ul>
     *     <li>Sprawdzany jest Shared Preferences w celu pobrania zapisanych ustawień i rankingu,</li>
     *     <li>Ustawiany jest główny widok i motyw aplikacji,</li>
     *     <li>Tworzona jest klasa muzyki i odpalona zostanie główna piosenka,</li>
     *     <li>Generowana jest lista słów 'wordLibrary',</li>
     *     <li>Jest ustawiany 'NavController' do przełączenia pomiędzy fragmentami,</li>
     *     <li>Ustawiony jest nasłuchiwanie zwróconych wartości z gameARL. Jeżeli został dokonany zwrot, to:
     *         <ul>
     *             <li>Pobierane są zwrócone wartości z 'result',</li>
     *             <li>Przemieniony zostanie wartość poziomu trudności (np. B -> BANALNY),</li>
     *             <li>Te wartości zostaną dodane do listy rankingowej.</li>
     *         </ul>
     *     </li>
     * </ul>
     *
     * @param savedInstanceState    If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     * @see     AppCompatActivity
     * @see     NavController
     * @see     SharedPreferences
     * @see     Gson
     * @since   1.2
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences shP = getSharedPreferences("shP", MODE_PRIVATE);
        Gson gson = new Gson();
        String settingsJSON = shP.getString("settings", "");
        String rankingJSON = shP.getString("ranking", "");
        if (settingsJSON.equals("")) {
            settings = new Settings();
        } else {
            settings = gson.fromJson(settingsJSON, Settings.class);
        }
        if (rankingJSON.equals("")) {
            rankingList = new ArrayList<>();
            rankingList.add(new Ranking("Nazwa", "Trudność", "Litery", "Punkty"));
        } else {
            Type listType = new TypeToken<ArrayList<Ranking>>() {}.getType();
            rankingList = gson.fromJson(rankingJSON, listType);
        }


        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Black);
        setContentView(R.layout.activity_main);

        music = new Music(this);
        music.playMainMusic(settings.getMusicVol());
        generateLibrary();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        gameARL = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        String ltrs = data.getStringExtra("sequence");
                        String pts = data.getStringExtra("points");
                        String diffic = data.getStringExtra("difficulty");

                        assert ltrs != null;
                        assert pts != null;
                        assert diffic != null;

                        for (Difficulty dName : Difficulty.values()) {
                            if (diffic.charAt(0) == dName.toString().charAt(0)) {
                                diffic = dName.toString();
                                break;
                            }
                        }
                        ltrs = ltrs.toUpperCase();

                        rankingList.add(new Ranking(
                                settings.getUsername(),
                                diffic,
                                ltrs,
                                pts
                        ));
                    }
                });
    }

    /**
     * Przy dowolnym wychodzeniu użytkownika z aplikacji, zostaje wywołana metoda onPause().
     * Metoda wtedy:
     * <ul>
     *     <li>Pauzuje piosenkę główną,</li>
     *     <li>Zamienia instancję klasy w łańcuch tekstowy za pomocą Gson,</li>
     *     <li>Dodaje i aplikuje te przekształcone instancje do SharedPreferences.</li>
     * </ul>
     * @see     Gson
     * @see     SharedPreferences
     * @since   1.2
     */
    @Override
    protected void onPause() {
        super.onPause();
        music.pause();

        Gson gson = new Gson();
        String settingsJSON = gson.toJson(settings);
        String rankingJSON = gson.toJson(rankingList);

        SharedPreferences shP = getSharedPreferences("shP", MODE_PRIVATE);
        SharedPreferences.Editor editor = shP.edit();
        editor.putString("settings", settingsJSON);
        editor.putString("ranking", rankingJSON);
        editor.apply();
    }

    /**
     * Gdy użytkownik wraca do aplikacji, to wywołany jest onResume().
     * Metoda wtedy wznaiwa piosenkę główną.
     * @since   1.2
     */
    @Override
    protected void onResume() {
        super.onResume();
        music.playMainMusic(settings.getMusicVol());
    }

    /**
     * Po naciśnięciu przycisku 'Zagraj' w fragmencie głównym odpalana jest metoda playGame().
     * Ta metoda wkłada do Intent poziom trudności, motyw i głośność efektów dźwiękowych
     *  i uruchamia aktywność GameActivity za pomocą gameARL.
     *
     * @see     GameActivity
     * @see     Intent
     * @since   1.2
     */

    public void playGame(String diffic) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("difficulty", diffic);
        intent.putExtra("theme", settings.getColMotive());
        intent.putExtra("sfx", settings.getSfxVol());
        gameARL.launch(intent);
    }

    /**
     * Metoda generateLibrary jest odpowiedzialny za uzupełnienie listy polskich słów 'wordLibrary'.
     * Ta metoda odczytuje plik 'slowa.txt' z folderu './res/raw' i przepisuje wszystkie słowa
     * zawarte w tym pliku do listy słów 'wordLibrary'.
     *
     * @see     InputStream
     * @see     BufferedReader
     * @since   1.0
     */
    private void generateLibrary() {
        wordLibrary = new ArrayList<>();
        InputStream is = this.getResources().openRawResource(R.raw.slowa);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        try {
            while((line = reader.readLine()) != null)
                if (line.length() <= 12)
                    wordLibrary.add(line);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}