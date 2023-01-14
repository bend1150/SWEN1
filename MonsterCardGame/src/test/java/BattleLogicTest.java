
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import Card.*;
import User.*;

import java.sql.SQLException;

class BattleLogicTest {

    @Test
    void battle()throws SQLException {
        /*

        // Create two users with different decks
        User user1 = new User("user1","123",1,20,1000,20,1);
        User user2 = new User("user2","456",2,20,1000,30,5);


        Card card1 = new MonsterCard("DRAGON","FIRE", 20,1);
        Card card2 = new MonsterCard("GOBLIN","WATER", 16,2);
        Card card3 = new MonsterCard("TROLL","FIRE", 15,3);
        Card card4 = new MonsterCard("ORC","NORMAL", 5,4);

        user1.getDeck().add(card1);
        user1.getDeck().add(card2);
        user2.getDeck().add(card3);
        user2.getDeck().add(card4);



        // Call the battle method and assert the result
        int result = BattleLogic.battle(user1, user2);
        assertEquals(1, result);                    // Player1 should win

        // Check that the cards have been moved to the correct decks
        assertEquals(4, user1.getDeck().size()); // Player1 should have 4 cards
        assertTrue(user1.getDeck().contains(card3));
        assertTrue(user1.getDeck().contains(card4));
        assertEquals(0, user2.getDeck().size()); // Player2 should have 0 cards
    */
    }

    @Test
    void monsterBattle() {

        Card card1 = new MonsterCard("Goblin", "WATER", 10,1,"15");
        Card card2 = new MonsterCard("Troll", "FIRE", 15,2,"15");

        assertEquals(2,BattleLogic.MonsterBattle(card1,card2)); //Player1 should win
        assertEquals(1,BattleLogic.MonsterBattle(card2,card1)); //Player2 should win
        assertEquals(3,BattleLogic.MonsterBattle(card1,card1)); // Should end in draw


    }

    @Test
    void spellBattle() {

        Card card1 = new SpellCard("ICESPELL","ICE", 9, 1,"15");
        Card card2 = new SpellCard("WATERSPELL","WATER", 10, 2,"15");


        assertEquals(1,BattleLogic.SpellBattle(card1,card2));     //Player1 should win
        assertEquals(2, BattleLogic.SpellBattle(card2,card1));    //Player2 should win
        assertEquals(3, BattleLogic.SpellBattle(card1,card1));    //Should end in draw


    }

    @Test
    void mixedBattle() {

        Card card1 = new MonsterCard("WATERGOBLIN", "WATER", 10,1,"12");
        Card card2 = new SpellCard("WATERSPELL", "WATER", 2,2,"14");

        Card card3 = new MonsterCard("KRAKEN", "NORMAL", 20,3,"15");
        Card card4 = new SpellCard("FIRESPELL","FIRE",45,4,"12");

        assertEquals(1, BattleLogic.MixedBattle(card1,card2));  //Player1 should win
        assertEquals(2, BattleLogic.MixedBattle(card2,card1));  //Player2 should win
        assertEquals(1, BattleLogic.MixedBattle(card3,card4));  //Player1 should win
        assertEquals(2, BattleLogic.MixedBattle(card4,card3));  //Player2 should win
        assertEquals(3, BattleLogic.MixedBattle(card1,card1));  //Should end in draw

    }
}