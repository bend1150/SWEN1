
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import Card.*;
import User.*;

import java.sql.SQLException;

class BattleLogicTest {

    @Test
    void battle(){
        // Create two users with different decks
        User user1 = new User("user1","123",1,20,1000,20,1,"","","");
        User user2 = new User("user2","456",2,20,1000,30,5,"","","");

        Card card1 = new MonsterCard("DRAGON","FIRE", 20,1,"1234");
        Card card2 = new MonsterCard("GOBLIN","WATER", 16,2,"2345");
        Card card3 = new MonsterCard("TROLL","FIRE", 15,3,"3456");
        Card card4 = new MonsterCard("ORC","NORMAL", 5,4,"4567");

        user1.getDeck().add(card1);
        user1.getDeck().add(card2);
        user2.getDeck().add(card3);
        user2.getDeck().add(card4);

        // Call the battle method and assert the result
        BattleLogic btl = new BattleLogic();

        int result = btl.battle(user1, user2);
        assertEquals(1, result);                    // Player1 should win

        // Check that the cards have been moved to the correct decks
        assertEquals(4, user1.getDeck().size()); // Player1 should have 4 cards
        assertTrue(user1.getDeck().contains(card3));       // Player1 should have cards from Player2
        assertTrue(user1.getDeck().contains(card4));        // Player1 should have cards from Player2
        assertEquals(0, user2.getDeck().size()); // Player2 should have 0 cards
    }
    @Test
    void battleinfinity(){
        User user1 = new User("user1","123",1,20,1000,20,1,"","","");
        User user2 = new User("user2","456",2,20,1000,30,5,"","","");

        Card card1 = new MonsterCard("DRAGON","FIRE", 20,1,"1234");
        Card card2 = new MonsterCard("DRAGON","FIRE", 20,1,"1234");
        BattleLogic btl = new BattleLogic();
        user1.getDeck().add(card1);
        user2.getDeck().add(card2);

        int result = btl.battle(user1,user2);
        assertEquals(3,result);                 //returns 3 if more than 99 rounds => draw
    }

    @Test   //Dragon wins against Goblins
    void monsterBattle1() {
        Card card1 = new MonsterCard("FIREDRAGON","FIRE", 20,1,"1234");
        Card card2 = new MonsterCard("GOBLIN","NORMAL", 50,1,"1234");
        BattleLogic btl = new BattleLogic();
        assertEquals(1,btl.MonsterBattle(card1,card2));
        assertEquals(2,btl.MonsterBattle(card2,card1));
        assertEquals(3,btl.MonsterBattle(card1,card1));
    }

    @Test       //Wizzard wins against Orcs
    void monsterBattle2(){
        Card card1 = new MonsterCard("WIZZARD","FIRE", 20,1,"1234");
        Card card2 = new MonsterCard("ORK","WATER", 50,1,"1234");
        BattleLogic btl = new BattleLogic();
        assertEquals(1,btl.MonsterBattle(card1,card2));
        assertEquals(2,btl.MonsterBattle(card2,card1));
        assertEquals(3,btl.MonsterBattle(card1,card1));
    }

    @Test       //FireElves beats Dragon
    void monsterBattle3(){
        Card card1 = new MonsterCard("ELF","FIRE", 20,1,"1234");
        Card card2 = new MonsterCard("DRAGON","NORMAL", 50,1,"1234");
        BattleLogic btl = new BattleLogic();
        assertEquals(1,btl.MonsterBattle(card1,card2));
        assertEquals(2,btl.MonsterBattle(card2,card1));
        assertEquals(3,btl.MonsterBattle(card1,card1));
    }

    @Test   //Ice should win against Water
    void spellBattle1() {
        BattleLogic battle = new BattleLogic();
        Card card1 = new SpellCard("ICESPELL","ICE", 9, 1,"15");
        Card card2 = new SpellCard("WATERSPELL","WATER", 10, 2,"15");
        assertEquals(1,battle.SpellBattle(card1,card2));
        assertEquals(2, battle.SpellBattle(card2,card1));
        assertEquals(3, battle.SpellBattle(card1,card1));    //draw
    }
    @Test   //Fire should win against Ice
    void spellBattle2() {
        BattleLogic battle = new BattleLogic();
        Card card1 = new SpellCard("FIRESPELL","FIRE", 7, 1,"15");
        Card card2 = new SpellCard("ICESPELL","ICE", 10, 2,"15");

        assertEquals(1,battle.SpellBattle(card1,card2));
        assertEquals(2,battle.SpellBattle(card2,card1));
        assertEquals(3,battle.SpellBattle(card1,card1));       //draw
    }

    @Test //Normalspell beats Water
    void spellBattle3(){
        BattleLogic battle = new BattleLogic();
        Card card1 = new SpellCard("REGULARSPELL","NORMAL", 5, 1,"15");
        Card card2 = new SpellCard("WATERSPELL","WATER", 7, 2,"15");

        assertEquals(1,battle.SpellBattle(card1,card2));
        assertEquals(2,battle.SpellBattle(card2,card1));
        assertEquals(3,battle.SpellBattle(card1,card1));   //draw
    }

    @Test       //Water beats Fire
    void spellBattle4(){
        BattleLogic battle = new BattleLogic();
        Card card1 = new SpellCard("WATERSPELL","WATER", 5, 1,"15");
        Card card2 = new SpellCard("FIRESPELL","FIRE", 7, 2,"15");
        assertEquals(1,battle.SpellBattle(card1,card2));
        assertEquals(2,battle.SpellBattle(card2,card1));
        assertEquals(3,battle.SpellBattle(card1,card1));   //draw

    }

    @Test       // monster vs spell
    void mixedBattle1() {
        BattleLogic battle = new BattleLogic();
        Card card1 = new MonsterCard("WATERGOBLIN","WATER", 10, 1,"15");
        Card card2 = new SpellCard("FIRESPELL","FIRE", 10, 2,"15");
        assertEquals(1,battle.MixedBattle(card1,card2));
        assertEquals(2,battle.MixedBattle(card2,card1));

    }

    @Test       //Waterspellls beats Knight
    void mixedBattle2() {
        Card card1 = new SpellCard("WATERSPELL","WATER", 10, 1,"15");
        Card card2 = new MonsterCard("KNIGHT","FIRE", 70, 2,"15");
        BattleLogic battle = new BattleLogic();
        assertEquals(1,battle.MixedBattle(card1,card2));
        assertEquals(2,battle.MixedBattle(card2,card1));
    }

    @Test
    void mixedBattle3() {       //Kraken beats all spells
        Card card1 = new MonsterCard("KRAKEN","NORMAL", 10, 1,"15");
        Card card2 = new SpellCard("FIRESPELL","FIRE", 70, 2,"15");
        BattleLogic battle = new BattleLogic();
        assertEquals(1,battle.MixedBattle(card1,card2));
        assertEquals(2,battle.MixedBattle(card2,card1));
        assertEquals(3,battle.MixedBattle(card1,card1));
    }



}