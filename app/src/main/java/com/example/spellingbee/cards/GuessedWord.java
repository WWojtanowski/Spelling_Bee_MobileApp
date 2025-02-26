package com.example.spellingbee.cards;

/**
 * Klasa GuessedWord reprezentuje słowo (word), która ma swoją wartość punktową (points).
 * Ta klasa jest stworzona głównie do zbudowania klasy adaptera, która będzie używana przez RecyclerView.
 * Klasa nie musi być używana tylko do przechowywania słów odgadniętych (guessedWords), można także
 * przechować np. słowa możliwe do odgadnięcia (possibleWords).
 *
 * @author      Wiktor Wojtanowski
 * @version     %I%, %G%
 * @see         com.example.spellingbee.adapters.GuessedWordAdapter
 * @see         com.example.spellingbee.models.Game
 * @since       1.0
 */
public class GuessedWord {
    private String word;
    private String points;

    public GuessedWord(String word, String points) {
        this.word = word;
        this.points = points;
    }

    public String getWord() {
        return word;
    }

    public String getPoints() {
        return points;
    }
}
