import java.util.ArrayList;
import java.util.List;


public class User {
    private String username;
    private String password;

    private int coins = 20;
    private List<Card> myCard = new ArrayList<>();
    private int eloValue = 100;

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

    public List<Card> getMyCard() {
        return myCard;
    }

    public void setMyCard(List<Card> myCard) {
        this.myCard = myCard;
    }

    public int getEloValue() {
        return eloValue;
    }

    public void setEloValue(int eloValue) {
        this.eloValue = eloValue;
    }
}
