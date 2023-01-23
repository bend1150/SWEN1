package DB;
import User.*;
import java.sql.*;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {
    private static final String url = "jdbc:postgresql://localhost:5432/cardgame";
    private static final String user ="postgres";
    private static final String pass="pwd123456";

    @Test
    void registerUser() throws SQLException {
        UserRepository user = new UserRepository();
        String username= "TestUser";
        String password = "123";
        assertEquals(1, user.registerUser(username,password));    // should return 1, user already exist
    }

    @Test
    void loginUserFail() throws SQLException {
        UserRepository usr = new UserRepository();
        String username ="TestUser";
        String password = "123";
        assertEquals(1, usr.loginUser("anyName","anyPass"));  // does NOT exist in DB
    }

    @Test
    void loginUserSuccess()throws SQLException{
        UserRepository usr = new UserRepository();
        String username ="TestUser";
        String password = "123";
        assertEquals(0, usr.loginUser(username,password));                     // user exists
    }

    @Test
    void testLogout() throws SQLException {
        String username ="TestUser";
        String password = "123";
        UserRepository test = new UserRepository();
        User player = new User("TestUser","123",3304,20,1000,20,1,"","","");
        assertEquals(0, test.logout(player));
    }
    @Test
    void updateStats() throws SQLException {
        UserRepository test = new UserRepository();

        User winner = test.getWinnerTest();
        User loser = test.getLoserTest();
        test.updateStats(winner, loser);
        winner = test.getWinnerTest();
        loser = test.getLoserTest();

        assertEquals(103, winner.getEloValue());
        assertEquals(1, winner.getWins());
        assertEquals(1, winner.getGamesCount());

        assertEquals(95, loser.getEloValue());
        assertEquals(0, loser.getWins());
        assertEquals(1, loser.getGamesCount());
        test.resetTestUser(winner);
        test.resetTestUser(loser);

    }


    @Test
    void editUserData() {
        UserRepository test = new UserRepository();
        User testUser = test.getUserInfoByToken("UserData-Token");

        try {
            test.editUserData(testUser,"ben","nice",":D");  //adjust the parameter with your input
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        testUser = test.getUserInfoByToken("UserData-Token");


        //adjust the parameter with your input
        assertEquals("ben", testUser.getName());
        assertEquals("nice", testUser.getBio());
        assertEquals(":D", testUser.getImage());

    }
}