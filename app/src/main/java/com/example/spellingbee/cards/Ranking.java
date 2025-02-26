package com.example.spellingbee.cards;

/**
 * Klasa Ranking służy do zachowania wyniku gry. Ta klasa przechowuje:
 * <ul>
 *     <li>Nazwa gracza tej gry,</li>
 *     <li>Poziom trudności,</li>
 *     <li>Sekwencję liter danej gry,</li>
 *     <li>Końcową sumę puktów.</li>
 * </ul>
 * Klasa jest uzyta do stworzenia adaptera używaną przez dany RecyclerView.
 *
 * @author      Wiktor Wojtanowski
 * @version     %I%, %G%
 * @see         com.example.spellingbee.adapters.RankingAdapter
 * @since       1.2
 */

public class Ranking {
    private String username;
    private String difficulty;
    private String letters;
    private String points;

    public Ranking(String username, String difficulty, String letters, String points) {
        this.username = username;
        this.difficulty = difficulty;
        this.letters = letters;
        this.points = points;
    }

    public String getUsername() {
        return username;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getLetters() {
        return letters;
    }

    public String getPoints() {
        return points;
    }
}
