package com.example.spellingbee.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spellingbee.MainActivity;
import com.example.spellingbee.R;
import com.example.spellingbee.adapters.RankingAdapter;
import com.example.spellingbee.cards.Ranking;

import java.util.ArrayList;

/**
 * RankingFragment to klasa typu 'Fragment', która wyświetla widok rankingu.
 * Ten widok wyświetla listę rankingową za pomocą Recycler View.
 * Lista rankingowa nie może mieć więcej wierszy niż 25.
 * Jeżeli limit zostanie przekroczony, to wiersz o najmniejszej punktacji zostanie usunięty
 *
 * @author      Wiktor Wojtanowski
 * @see         Ranking
 * @see         RankingAdapter
 * @version     1.2
 * @since       1.2
 */
public class RankingFragment extends Fragment {

    /**
     * Metoda onCreateView() pochodząca z klasy 'Fragment' wykonuje kod przy tworzeniu widoku rankingu.
     * Ta metoda ustawia i pokazuje listę rankingową używając rankingList z MainActivity.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return  Widok rankingu
     * @see     RecyclerView
     * @see     MainActivity
     * @since   1.2
     */
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);

        MainActivity mainActivity = (MainActivity) getContext();

        RecyclerView rankRV = view.findViewById(R.id.rankingRV);
        rankRV.setHasFixedSize(true);
        RecyclerView.LayoutManager rankLM = new LinearLayoutManager(getContext());
        rankRV.setLayoutManager(rankLM);

        sortRanking(mainActivity.rankingList);
        RecyclerView.Adapter rankAdpt = new RankingAdapter(sortRanking(mainActivity.rankingList));
        rankRV.setAdapter(rankAdpt);

        return view;
    }

    /**
     * Metoda sortRanking() służy do sortowania bąbelkowego listy rankingowej według liczby punktów danego słowa.
     * @param rankings  Lista zawierająca instancje klasy Ranking
     * @return          Posortowana lista rankingowa
     * @since           1.2
     */
    private ArrayList<Ranking> sortRanking(ArrayList<Ranking> rankings) {
        int n = rankings.size();

        while (n>1) {
            for (int i = 1; i < n-1; i++) {
                if (getPointsFromRank(rankings.get(i)) < getPointsFromRank(rankings.get(i+1))) {
                    Ranking placeholder = rankings.get(i+1);
                    rankings.remove(i+1);
                    rankings.add(i, placeholder);
                }
            }
            n--;
        }
        if (rankings.size() > 25)
            rankings.remove(rankings.size()-1);

        return rankings;
    }

    /**
     * Metoda getPointsFromRank() zaminenia tekst z wartością punktową
     * na liczbę punktów w postaci numerycznej.
     *
     * @param rank  Wybrany wiersz z listy rankingowej
     * @return      Suma punktów z danego wiersza rankingu w postaci numerycznej
     * @since       1.2
     */
    private int getPointsFromRank(Ranking rank) {
        String pktTxt = rank.getPoints();
        return Integer.parseInt(pktTxt.substring(0, pktTxt.length()-5));
    }
}