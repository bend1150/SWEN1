import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) throws SQLException {

        Database.openConnection();

        User player1 = new User("Ben","123",1,20,1000);
        User player2 = new User("Viv","345",2,19,1001);





        MonsterCard card1 = new MonsterCard("ORC","WATER", 48,1);
        MonsterCard card2 = new MonsterCard("KRAKEN","FIRE", 16,2);
        MonsterCard card3 = new MonsterCard("ELF","NORMAL", 55,3);
        SpellCard card4 = new SpellCard("NORMAL",8,4);
        SpellCard card5 = new SpellCard("WATER",7, 5);
        SpellCard card6 = new SpellCard("FIRE",15, 6);

        MonsterCard card7 = new MonsterCard("GOBLIN","NORMAL", 34,7);
        MonsterCard card8 = new MonsterCard("WIZZARD","WATER", 24,8);
        MonsterCard card9 = new MonsterCard("DRAGON","FIRE", 41,9);
        SpellCard card10 = new SpellCard("WATER",14,10);
        SpellCard card11 = new SpellCard("NORMAL",15, 11);
        SpellCard card12 = new SpellCard("FIRE",6, 12);



        List<Card> deck1 = new ArrayList<Card>();
        deck1.add(card1);
        deck1.add(card2);
        deck1.add(card3);
        deck1.add(card4);
        deck1.add(card5);
        deck1.add(card6);

        player1.setDeck(deck1);

        List<Card> deck2 = new ArrayList<Card>();
        deck2.add(card7);
        deck2.add(card8);
        deck2.add(card9);
        deck2.add(card10);
        deck2.add(card11);
        deck2.add(card12);

        player2.setDeck(deck2);



        int victory = BattleLogic.battle(player1,player2);
        if(victory == 1 || victory == 2) {
            System.out.println(" >>>>>>>>>>>>>>>>>>>Player" + victory + " wins!!!<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        } else if(victory == 3){
            System.out.println("DRAW!!!");
        }
        /*
        int victory = BattleLogic.MonsterBattle(card1,card5);
        if(victory == 1 || victory == 2) {
            System.out.println("Player" + victory + " wins");
        } else if(victory == 3){
            System.out.println("DRAW!!!");
        }
        */

        MonsterCard card100 = new MonsterCard("ORC","FIRE", 8,5);
        MonsterCard card101 = new MonsterCard("DRAGON","WATER", 15,3);

        SpellCard card110 = new SpellCard("NORMAL",5, 8);
        SpellCard card111 = new SpellCard("FIRE",10, 9);

        //BattleLogic.MixedBattle(card11, card12);
    }

}