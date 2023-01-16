package DB;
import java.util.ArrayList;
import java.util.List;
import Card.*;
import DB.*;
import User.*;
import java.sql.*;
import java.util.Random;
import java.util.Scanner;


import httpserver.service.*;
import httpserver.server.*;
import httpserver.http.*;
import httpserver.utils.*;

import httpserver.service.UserService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardQueryTest {

    @Test
    void buyPackage() throws SQLException {
        User user1 = new User("TestUser","123",3304,0,0,0,0);
        assertEquals(1,DB.CardQuery.acquirePackage(user1));     //should expect 1 because not enough coin
    }

    @Test
    void showDeck() {
        User user1 = new User("winnerTest","winner123",3302,20,100,0,0);
        assertEquals("",DB.CardQuery.showDeck(user1));          //should show empty string, because it has no deck

    }

    @Test
    void configureDeck() throws SQLException {
        User user1 = new User("TestUser","123",3304,0,0,0,0);

        assertEquals(0,DB.CardQuery.configureDeck(user1,"12345","23456","34567","45678"));   // 1:all cards exist, so valid
        assertEquals(1,DB.CardQuery.configureDeck(user1,"12345","23456","34567","invalid"));// 0: card4 does not exist
    }

}