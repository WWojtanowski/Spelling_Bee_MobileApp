package com.example.spellingbee.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spellingbee.R;
import com.example.spellingbee.cards.GuessedWord;

import java.util.ArrayList;

/**
 * Klasa GuessedWordAdapter to adapter do RecyclerView, który może być z nim powiązany poprzez wstawianie
 * danych do widoków wykorzystywanych przez RecyclerView.
 *
 * @author      Wiktor Wojtanowski
 * @version     %I%, %G%
 * @see         GuessedWord
 * @see         RecyclerView
 * @since       1.0
 */
public class GuessedWordAdapter extends RecyclerView.Adapter<GuessedWordAdapter.GuessedWordViewHolder> {

    /**
     * Lista guessedWord zawiera odgadnięte (lub możliwe do odgadnięcia) słowa.
     * @see     com.example.spellingbee.models.Game
     * @since   1.0
     */
    public ArrayList<GuessedWord> guessedWords;

    /**
     * Konstruktor GuessedWordAdapter ładuje i zapisuje listę z instancjami GuessedWords.
     * Najczęściej w tym projekcie używane są listy z klasy Game (guessedWords lub possibleWords).
     *
     * @param guessedWords  Wprowadzona lista z instancjami GuessedWords
     * @see                 com.example.spellingbee.models.Game
     * @since               1.0
     */
    public GuessedWordAdapter(ArrayList<GuessedWord> guessedWords) {
        this.guessedWords = guessedWords;
    }

    /**
     * Metoda onCreateViewHolder() jest wywoływane, kiedy RecyclerView potrzebuje nowego GuessedWordViewHolder,
     * by pokazać element w liście. GuessedWordViewHolder jest tworzony dzięki klasy LayoutInflater.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return  Nowa instancja GuessedWordViewHolder
     * @see     LayoutInflater
     * @see     GuessedWordViewHolder
     * @since   1.0
     */
    @NonNull
    @Override
    public GuessedWordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.guessed_word_item,
                parent, false);
        GuessedWordViewHolder gwvh = new GuessedWordViewHolder(v);
        return gwvh;
    }

    /**
     * Aktualizuje elementy GuessedWordViewHolder w poszczególnej pozycji.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     * @see     GuessedWordViewHolder
     * @since   1.0
     */
    @Override
    public void onBindViewHolder(@NonNull GuessedWordViewHolder holder, int position) {
        GuessedWord item = guessedWords.get(position);
        holder.word.setText(item.getWord());
        holder.points.setText(item.getPoints());
    }

    /**
     * Zwraca rozmiar listy - liczba instancji klasy GuessedWord.
     * @return  Liczba instancji w liście.
     * @since   1.0
     */
    @Override
    public int getItemCount() {
        return guessedWords.size();
    }

    /**
     * Klasa GuessedWordViewHolder pochodzi od klasy RecyclerView.ViewHolder, który opisuje widok
     * danego elementu i jego miejsce w RecyclerView. Przypisuje pola 'word' i 'points'
     * do poszczególnych elementów widoku elementu.
     * @see         androidx.recyclerview.widget.RecyclerView.ViewHolder
     * @since       1.0
     */
    public static class GuessedWordViewHolder extends RecyclerView.ViewHolder {
        public TextView word;
        public TextView points;

        public GuessedWordViewHolder(@NonNull View itemView) {
            super(itemView);
            word = itemView.findViewById(R.id.guessedWord);
            points = itemView.findViewById(R.id.points);
        }
    }
}
