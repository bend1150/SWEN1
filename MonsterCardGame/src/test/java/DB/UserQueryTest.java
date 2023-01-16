package DB;
import java.util.ArrayList;
import java.util.List;
import Card.*;
import DB.*;
import User.*;
import java.sql.*;
import java.util.Random;
import java.util.Scanner;


import httpserver.server.HeaderMap;
import httpserver.server.Request;
import httpserver.server.Response;
import httpserver.server.Server;
import httpserver.server.Service;

import httpserver.service.acquirePackageService;
import httpserver.service.configureDeckService;
import httpserver.service.createPackageService;
import httpserver.service.LoginService;
import httpserver.service.showCardsService;
import httpserver.service.statsService;
import httpserver.service.UserService;

import httpserver.http.ContentType;
import httpserver.http.HttpStatus;
import httpserver.http.Method;

import httpserver.utils.RequestBuilder;
import httpserver.utils.RequestHandler;
import httpserver.utils.Router;








import httpserver.service.UserService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserQueryTest {
    private static final String url = "jdbc:postgresql://localhost:5432/cardgame";
    private static final String user ="postgres";
    private static final String pass="pwd123456";

    @Test
    void registerUser() throws SQLException {
        String username= "TestUser";
        String password = "123";
        assertEquals(1,UserQuery.registerUser(username,password));    // should return 1, user already exist
    }

    @Test
    void loginUser() throws SQLException {
        String username ="TestUser";
        String password = "123";

        assertEquals(0,UserQuery.loginUser(username,password));                     // user exists
        assertEquals(1,UserQuery.loginUser("anyName","anyPass"));  // does NOT exist

    }


    @Test
    void testLogout() throws SQLException {
        String username ="TestUser";
        String password = "123";

        User player = new User("TestUser","123",1,20,1000,20,1);

        assertEquals(0,UserQuery.logout(player));


    }

    @Test
    void updateStats() throws SQLException {
        User winner = UserQuery.getWinnerTest();
        User loser = UserQuery.getLoserTest();

        UserQuery.updateStats(winner, loser);

        assertEquals(103, winner.getEloValue());
        assertEquals(1, winner.getWins());
        assertEquals(1, winner.getGamesCount());

        assertEquals(95, loser.getEloValue());
        assertEquals(0, loser.getWins());
        assertEquals(1, loser.getGamesCount());

        UserQuery.resetTestUser(winner);
        UserQuery.resetTestUser(loser);


    }


    @Test
    void editProfile() {        //oldPassword & newPassword needs to be changed in every Unit Test (look into DB to see current password)
        // Create a test user
        User testUser = new User("editProfileTest", "5", 3301, 0, 0, 0, 0);

        // Update the user's name and password using the editProfile function
        User updatedUser = null;
        try {
            updatedUser = UserQuery.editProfileNew(testUser,"editProfileTest","5","6");
        } catch (SQLException e) {
            fail("Error during test");
        }

        // Changes Password
        assertNotNull(updatedUser);
        assertEquals("editProfileTest", updatedUser.getUsername());
        assertEquals("6", updatedUser.getPassword());
    }
}