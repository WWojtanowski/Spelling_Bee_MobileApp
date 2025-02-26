package com.example.spellingbee.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spellingbee.R;
import com.example.spellingbee.cards.GuessedWord;
import com.example.spellingbee.cards.Ranking;

import java.util.ArrayList;

/**
 * Klasa RankingAdapter to adapter do RecyclerView, który może być z nim powiązany poprzez wstawianie
 * danych do widoków wykorzystujących RecyclerView.
 * Ten adapter jest tylko obsługiwany przez tylko jeden RecyclerView w fragmencie rankingu.
 *
 * @author      Wiktor Wojtanowski
 * @version     %I%, %G%
 * @see         Ranking
 * @see         com.example.spellingbee.fragments.RankingFragment
 * @see         RecyclerView
 * @since       1.2
 */
public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.RankingViewHolder> {

    /**
     * Lista rankList zawiera wszystkie zapisane wyniki z granych gier Spelling Bee.
     * W głównej klasie activity jest prezentowane jako rankingList
     * @see     com.example.spellingbee.MainActivity
     * @since   1.2
     */
    public ArrayList<Ranking> rankList;

    /**
     * Konstruktor RankingAdapter() ładuje i zapisuje listę z instancjami Ranking.
     * @param rankList      Wprowadzona lista z instancjami Ranking
     * @since               1.2
     */
    public RankingAdapter(ArrayList<Ranking> rankList) {
        this.rankList = rankList;
    }

    /**
     * Metoda onCreateViewHolder() jest wywoływane, kiedy RecyclerView potrzebuje nowego RankingViewHolder,
     * by pokazać element w liście. Nowy RankingViewHolder jest tworzony dzięki klasy LayoutInflater.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return  Nowa instancja RankingViewHolder
     * @see     LayoutInflater
     * @see     RankingAdapter.RankingViewHolder
     * @since   1.2
     */
    @NonNull
    @Override
    public RankingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ranking,
                parent, false);
        return new RankingViewHolder(v);
    }

    /**
     * Aktualizuje elementy RankingViewHolder w poszczególnej pozycji.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     * @see     RankingAdapter.RankingViewHolder
     * @since   1.2
     */
    @Override
    public void onBindViewHolder(@NonNull RankingViewHolder holder, int position) {
        Ranking item = rankList.get(position);
        holder.username.setText(item.getUsername());
        holder.difficulty.setText(item.getDifficulty());
        holder.letters.setText(item.getLetters());
        holder.points.setText(item.getPoints());
    }

    /**
     * Metoda getItemCount() zwraca rozmiar listy - liczba instancji klasy Ranking.
     * @return  Liczba instancji w liście.
     * @since   1.2
     */
    @Override
    public int getItemCount() {
        return rankList.size();
    }

    /**
     * Klasa RankingViewHolder pochodzi od klasy RecyclerView.ViewHolder, który opisuje widok
     * danego elementu i jego miejsce w RecyclerView. Przypisuje pola zawarte w klasie Ranking
     * do poszczególnych elementów widoku elementu.
     * @see         androidx.recyclerview.widget.RecyclerView.ViewHolder
     * @since       1.2
     */
    public static class RankingViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public TextView difficulty;
        public TextView letters;
        public TextView points;
        public RankingViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.itemName);
            difficulty = itemView.findViewById(R.id.itemDiffic);
            letters = itemView.findViewById(R.id.itemLetters);
            points = itemView.findViewById(R.id.itemPoints);
        }
    }
}
