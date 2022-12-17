import java.util.ArrayList;
import java.util.List;


public class User {
    private String username;
    private String password;

    private int id;
    private List<Card> deck = new ArrayList<Card>();

    private int coins = 20;

    private int eloValue = 100;

    public User(String username, String password, int id, int coins, int eloValue) {
        this.username = username;
        this.password = password;
        this.id = id;
        this.coins = coins;
        this.eloValue = eloValue;
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


}
