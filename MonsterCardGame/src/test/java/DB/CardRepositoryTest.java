package DB;
import Card.Card;
import User.*;
import java.sql.*;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardRepositoryTest {

    @Test
    void buyPackage() throws SQLException {

        CardRepository pckg = new CardRepository();
        User user1 = new User("TestUser","123",3304,0,0,0,0,"","","");
        assertEquals(1, pckg.acquirePackage(user1));     //should expect 1 because not enough coin
    }

    @Test
    void showDeck() {
        CardRepository dck = new CardRepository();
        User user1 = new User("winnerTest","winner123",3302,20,100,0,0,"","","");
        assertEquals("", dck.showDeck(user1));          //should show empty string, because it has no deck

    }

    @Test
    void configureDeck() throws SQLException {
        CardRepository dck = new CardRepository();
        User user1 = new User("TestUser","123",3304,0,0,0,0,"","","");

        assertEquals(0, dck.configureDeck(user1,"12345","23456","34567","45678"));   // 1:all cards exist, so valid
        assertEquals(1, dck.configureDeck(user1,"12345","23456","34567","invalid"));// 0: card4 does not exist
    }

}