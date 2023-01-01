import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) throws SQLException {

        Database.openConnection();

        User player1 = new User("Ben","123",1,20,1000);
        User player2 = new User("Viv","345",2,19,1001);

        MonsterCard card1 = new MonsterCard("GOBLIN","FIRE", 15,1);
        MonsterCard card2 = new MonsterCard("DRAGON","WATER", 13,2);
        SpellCard card3 = new SpellCard("NORMAL",8,3);
        SpellCard card4 = new SpellCard("FIRE",7, 4);


        MonsterCard card5 = new MonsterCard("ORC","WATER", 9,5);
        MonsterCard card6 = new MonsterCard("WIZARD","NORMAL", 13,6);
        SpellCard card7 = new SpellCard("NORMAL",6,7);
        SpellCard card8 = new SpellCard("WATER",5, 8);

        List<Card> deck1 = new ArrayList<Card>();
        deck1.add(card1);
        deck1.add(card2);
        deck1.add(card3);
        deck1.add(card4);

        player1.setDeck(deck1);


        List<Card> deck2 = new ArrayList<Card>();
        deck2.add(card5);
        deck2.add(card6);
        deck2.add(card7);
        deck2.add(card8);

        player2.setDeck(deck2);


        int victor = BattleLogic.startFight(player1,player2);

        System.out.println("Player" + victor + " wins");

    }

}