package com.example.spellingbee.models;

import com.example.spellingbee.MainActivity;
import com.example.spellingbee.cards.GuessedWord;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Klasa Game to model gry 'Spelling Bee', który przechowuje dane związane z grą:
 * <ul>
 *     <li>Lista wszystkich słów w języku polskim (wordLibrary),</li>
 *     <li>Sekwencja liter znajdujących się na przyciskach (letters),</li>
 *     <li>Wartość każdej litery (lettetValue),</li>
 *     <li>Lista odgadniętych słów (guessedWords),</li>
 *     <li>Lista możliwych słów do odgadnięcia (possibleWords).</li>
 * </ul>
 *
 * @author      Wiktor Wojtanowski
 * @version     %I%, %G%
 * @since       1.0
 */
public class Game {

    /**
     * letterValue to mapa, która przypisuje do każdej polskiej litery wartość punktową.
     * @see     Map
     * @since   1.0
     */
    public Map<Character, Integer> letterValue;

    /**
     * wordLibrary to lista wszystkich polskich słów pobierana z klasy MainActivity.
     * @see     MainActivity
     * @since   1.0
     */
    private List<String> wordLibrary;

    /**
     * letters to sekwencja siedmiu liter, które pojawiają się na przyciskach w widoku gry.
     * @since   1.0
     */
    private String letters;

    /**
     * guessedWords to lista odgadniętych słów, która zawiera odgadnięte słowo i wartość punktową słowa.
     * @see     GuessedWord
     * @since   1.0
     */
    private ArrayList<GuessedWord> guessedWords;

    /**
     * possibleWods to lista możliwych do ułożenia słów z sekwencji liter (letters).
     * @see     GuessedWord
     * @since   1.0
     */
    private ArrayList<GuessedWord> possibleWords;

    /**
     * isActive określa stan gry. Jeżeli jest true, to gra jest czynna, false, gdy gra jest skończona.
     * @since   1.0
     */
    private boolean isActive;

    /**
     * Pole difficulty zachowuje poziom trudności danej gry.
     * @since   1.2
     */
    private String difficulty;

    /**
     * Konstruktor Game() wykonuje kod przy tworzeniu instancji tej klasy.
     * Ten kontruktor:
     * <ul>
     *     <li>Ustawia poziom trudności gry,</li>
     *     <li>Inicjalizuje listy i mapy,</li>
     *     <li>Generuje sekwencję liter (letters),</li>
     *     <li>Pobiera listę polskich słów (wordLibrary),</li>
     *     <li>Wypełnia w tle listę możliwych liter (possibleWords).</li>
     * </ul>
     *
     * @see     Thread
     * @since   1.2
     */
    public Game(String difficulty) {
        isActive = true;
        this.difficulty = difficulty;
        initMap();
        generateLetters();
        wordLibrary = MainActivity.wordLibrary;

        new Thread(new Runnable() {
            @Override
            public void run() {
                fillPossibleWords();
            }
        }).start();
    }

    /**
     * Metoda checkWord() sprawdza, czy ułożone słowo jest poprawne.
     * Metoda także sprawdza, czy litera z środkowego przycisku znajduje się w ułożonym słowie
     * i czy to słowo nie jest już w liście odgadniętych słów.
     *
     * @param check     Ułożone słowo
     * @return          true jeżeli słowo znajduje się w słowniku (wordLibrary);
     *                  false jeżeli słowa nie ma w słowniku
     * @see             Pattern
     * @see             Matcher
     * @since           1.0
     */
    public boolean checkWord(String check) {
        Pattern ptrn = Pattern.compile("["+letters.charAt(0)+"]+", Pattern.CASE_INSENSITIVE);
        Matcher mtchr = ptrn.matcher(check);

        for (String s: wordLibrary)
            if (Objects.equals(s, check) && mtchr.find()) {
                boolean isValid = true;
                if (!guessedWords.isEmpty()) {
                    for (GuessedWord word : guessedWords) {
                        if (Objects.equals(word.getWord(), check.toUpperCase())) {
                            isValid = false;
                            break;
                        }
                    }
                }
                if (isValid)
                    guessedWords.add(0, new GuessedWord(
                            check.toUpperCase(),
                            calcPoints(check) + " pkt."
                    ));
                return isValid;
            }
        return false;
    }

    /**
     * Metoda getSumPoints sumuje punkty z wszystkich odgadniętych słów (guessedWords).
     * @return  Suma punktów
     * @since   1.0
     */
    public Integer getSumPoints() {
        Integer sum = 0;
        for (GuessedWord word: guessedWords) {
            sum += getPointsFromGW(word);
        }
        return sum;
    }

    /**
     * Metoda getLetters() służy do zwracania prywatnego pola 'letters'.
     * @return  Sekwencja liter (letters)
     * @since   1.0
     */
    public String getLetters() {
        return letters;
    }

    /**
     * Metoda getGuessedWords() służy do zwracania posortowanej listy odgadniętych słów.
     * @return  Lista odgadniętych słów posortowana według liczby punktów
     * @since   1.0
     */
    public ArrayList<GuessedWord> getGuessedWords() {
        return sortGuessedWords(guessedWords);
    }

    /**
     * Metoda getPossibleWords() służy do zwracania posortowanej listy możliwych słów.
     * @return  Lista możliwych słów posortowana według liczby punktów
     * @since   1.0
     */
    public ArrayList<GuessedWord> getPossibleWords() {
        return sortGuessedWords(possibleWords);
    }

    /**
     * Metoda setActive() zmienia stan gry na prawdziwą albo fałszywą.
     * @param active    Podana wartość logiczna stanu gry
     * @since           1.0
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Metoda fillPossibleWords() inicjalizuje i wypełnia listę możliwych słów (possibleWords)
     * słowami, które składają się tylko z liter, które są w sekwencji liter 'letters' i
     * pierwsza litera z tej sekwencji musi być zawarta w słowie przynajmniej raz.
     *
     * @see     Pattern
     * @see     Matcher
     * @since   1.0
     */
    private void fillPossibleWords() {
        possibleWords = new ArrayList<>();
        for (String line: wordLibrary) {
            Pattern pattern1 = Pattern.compile("^["+letters+"]+$", Pattern.CASE_INSENSITIVE);
            Matcher matcher1 = pattern1.matcher(line);

            Pattern pattern2 = Pattern.compile("["+letters.charAt(0)+"]+", Pattern.CASE_INSENSITIVE);
            Matcher matcher2 = pattern2.matcher(line);

            if (matcher1.find() && matcher2.find())
                possibleWords.add(0, new GuessedWord(
                        line.toUpperCase(),
                        calcPoints(line) + " pkt.")
                );
            if (!isActive) break;
        }
    }

    /**
     * Metoda sortGuessedWords służy do sortowania bąbelkowego list, które zawierają instancje GuessedWord
     * (czyli guessedWords i possibleWords). Listy są sortowane według liczby punktów danego słowa.
     *
     * @param gwList    Lista zawierająca instancje klasy GuessedWords
     * @return          Posortowana lista z instancjami GuessedWords
     * @since           1.0
     */
    private ArrayList<GuessedWord> sortGuessedWords(ArrayList<GuessedWord> gwList) {
        int n = gwList.size();
        while (n>1) {
            for (int i = 0; i < n-1; i++) {
                if (getPointsFromGW(gwList.get(i)) < getPointsFromGW(gwList.get(i+1))) {
                    GuessedWord placeholder = gwList.get(i+1);
                    gwList.remove(i+1);
                    gwList.add(i, placeholder);
                }
            }
            n--;
        }
        return gwList;
    }

    /**
     * Metoda getPointsFromGW przemienia z danej instancji GuessedWord tekst z wartością punktową
     * na liczbę punktów w postaci numerycznej ("20 pkt." -> 20).
     *
     * @param gw    Dana instancja GuessedWord
     * @return      Wartość punkowa w GuessedWord w postaci numerycznej
     * @see         GuessedWord
     * @since       1.0
     */
    private int getPointsFromGW(GuessedWord gw) {
        String pktTxt = gw.getPoints();
        return Integer.parseInt(pktTxt.substring(0, pktTxt.length()-5));
    }

    /**
     * Metoda calcPoints() oblicza wartość punktową danego słowa korzystając z mapy 'letterValue'.
     *
     * @param word  Dane słowo
     * @return      Wartość punktowa danego słowa
     * @since       1.0
     */
    public int calcPoints(String word) {
        int points = 0;
        for (int i = 0; i < word.length(); i++){
            points += letterValue.get(word.charAt(i));
        }
        return points;
    }

    /**
     * Metoda generateLetters() generuje siedmioliterową sekwencję liter (letters)
     * w oparciu o poziom trudności.
     * @since               1.0
     */
    private void generateLetters() {
        this.letters = "";
        switch (difficulty) {
            case "B":
                this.letters += getRandomLetter(3, 3, 2);
                this.letters += getRandomLetter(3, 4, 1);
                this.letters += getRandomLetter(5, 5, 3);
                this.letters += getRandomLetter(5, 6, 1);
                break;
            case "Ł":
                this.letters += getRandomLetter(3, 3, 2);
                this.letters += getRandomLetter(5, 5, 3);
                this.letters += getRandomLetter(6, 6, 1);
                this.letters += getRandomLetter(8, 8, 1);
                break;
            case "Ś":
                this.letters += getRandomLetter(5, 5, 2);
                this.letters += getRandomLetter(3, 3, 1);
                this.letters += getRandomLetter(3, 4, 1);
                this.letters += getRandomLetter(5, 6, 1);
                this.letters += getRandomLetter(6, 7, 1);
                this.letters += getRandomLetter(8, 8, 1);
                break;
            case "T":
                this.letters += getRandomLetter(5, 5, 1);
                this.letters += getRandomLetter(3, 3, 1);
                this.letters += getRandomLetter(3, 4, 1);
                this.letters += getRandomLetter(6, 6, 2);
                this.letters += getRandomLetter(7, 7, 1);
                this.letters += getRandomLetter(8, 9, 1);
                break;
            case "E":
                this.letters += getRandomLetter(6, 6, 2);
                this.letters += getRandomLetter(3, 3, 1);
                this.letters += getRandomLetter(4, 4, 1);
                this.letters += getRandomLetter(7, 7, 2);
                this.letters += getRandomLetter(9, 9, 1);
                break;
            default:
                this.letters += getRandomLetter(3, 4, 2);
                this.letters += getRandomLetter(5, 7, 4);
                this.letters += getRandomLetter(8, 9, 1);
                break;
        }
    }

    /**
     * Metoda getRandomLetter() losuje z alfabetu literę lub litery, które nie powtarzają się ze sobą
     * i nie zawierają się w sekwencji liter 'letters'. Litery muszą się zgadzać z zakresem wartości.
     *
     * @param valueFrom     początkowy zakres wartości litery
     * @param valueTo       końcowy zakres wartości litery
     * @param quantity      liczba losowych liter
     * @return              sekwencja wylosowanych liter
     * @since               1.0
     */
    private String getRandomLetter(int valueFrom, int valueTo, int quantity) {
        String alphabet = "aąbcćdeęfghijklłmnńoóprsśtuwyzźż";
        String outputLtrs = "";
        while (quantity > 0) {
            Random rand = new Random();
            int randNum = rand.nextInt(5);
            int i = 0;
            while(true) {
                Character ch = alphabet.charAt(i);
                if (letterValue.get(ch) >= valueFrom && letterValue.get(ch) <= valueTo
                        && !letters.contains(ch.toString()) && !outputLtrs.contains(ch.toString()))
                    if (randNum == 0) {
                        outputLtrs += ch.toString();
                        break;
                    } else
                        randNum--;
                i++;
                if (i >= alphabet.length())
                    i -= alphabet.length();
            }
            quantity--;
        }
        return outputLtrs;
    }

    /**
     * Metoda initMap() wypełnia mapę 'letterValue' wszystkimi polskimi literami wraz z przypisanymi
     * do nimi wartościami.
     *
     * @see     TreeMap
     * @since   1.0
     */
    private void initMap() {
        guessedWords = new ArrayList<>();

        letterValue = new TreeMap<>();
        letterValue.put('a', 3);
        letterValue.put('ą', 8);
        letterValue.put('b', 7);
        letterValue.put('c', 6);
        letterValue.put('ć', 9);
        letterValue.put('d', 6);
        letterValue.put('e', 3);
        letterValue.put('ę', 8);
        letterValue.put('f', 7);
        letterValue.put('g', 7);
        letterValue.put('h', 7);
        letterValue.put('i', 3);
        letterValue.put('j', 7);
        letterValue.put('k', 6);
        letterValue.put('l', 7);
        letterValue.put('ł', 8);
        letterValue.put('m', 6);
        letterValue.put('n', 5);
        letterValue.put('ń', 9);
        letterValue.put('o', 3);
        letterValue.put('ó', 8);
        letterValue.put('p', 6);
        letterValue.put('r', 5);
        letterValue.put('s', 5);
        letterValue.put('ś', 9);
        letterValue.put('t', 5);
        letterValue.put('u', 4);
        letterValue.put('w', 5);
        letterValue.put('y', 4);
        letterValue.put('z', 5);
        letterValue.put('ź', 8);
        letterValue.put('ż', 9);
    }

    /**
     * Metoda getDifficulty() zwraca poziom trudności tej instancji gry.
     * @return  Poziom trudności w formacie tekstowej
     * @since   1.2
     */
    public String getDifficulty() {
        return difficulty;
    }
}
