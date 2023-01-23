package User;

import java.util.ArrayList;
import java.util.List;
import Card.*;


public class User {
    private String username;
    private String password;

    private int coins = 20;
    private int eloValue = 100;
    private int id;
    private List<Card> deck = new ArrayList<Card>();

    private List<Card> stack = new ArrayList<Card>();

    private int gamesCount;

    private int wins;

    private String name;

    private String bio;
    private String image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getGamesCount() {
        return gamesCount;
    }

    public void setGamesCount(int gamesCount) {
        this.gamesCount = gamesCount;
    }


    public User(String username, String password, int id, int coins, int eloValue, int gamesCount, int wins, String name, String bio, String image) {
        this.username = username;
        this.password = password;
        this.id = id;
        this.coins = coins;
        this.eloValue = eloValue;
        this.gamesCount = gamesCount;
        this.wins = wins;
        this.name = name;
        this.bio = bio;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }




    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }


    public int getEloValue() {
        return eloValue;
    }

    public void setEloValue(int eloValue) {
        this.eloValue = eloValue;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public void setDeck(List<Card> deck) {
        this.deck = deck;
    }

    public List<Card> getStack() {
        return stack;
    }

    public void setStack(List<Card> stack) {
        this.stack = stack;
    }



}
