package DB;

import java.sql.*;
import Card.*;
import DB.*;
import User.*;

public class UserQuery {

    private static final String url = "jdbc:postgresql://localhost:5432/cardgame";
    private static final String user ="postgres";
    private static final String pass="pwd123456";



    public static void registerUser(String username, String password) throws SQLException {
        // Check if the username is already in use
        boolean usernameExists = false;
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement selectStmt = connection.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            selectStmt.setString(1, username);
            ResultSet rs = selectStmt.executeQuery();
            if (((ResultSet) rs).next()) {
                usernameExists = true;
            }
        }

        if (usernameExists) {
            // Error if user already exist
            System.out.println("Error: the username '" + username + "' already exist.");
        } else {
            // Insert the new user
            try (Connection connection = DriverManager.getConnection(url, user, pass);
                 PreparedStatement insertStmt = connection.prepareStatement("""
                INSERT INTO users
                (username, password, coins, elo)
                VALUES (?,?,?,?);
                """)) {
                insertStmt.setString(1, username);
                insertStmt.setString(2, password);
                insertStmt.setInt(3, 20);
                insertStmt.setInt(4, 100);
                insertStmt.execute();
            }
        }
    }


    public static User loginUser(String username, String password) throws SQLException {
        // check if user and pass match
        System.out.println("LOGIN");
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement selectStmt = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
            selectStmt.setString(1, username);
            selectStmt.setString(2, password);
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                // if user exist return User object
                int id = rs.getInt("id");
                int coins = rs.getInt("coins");
                int elo = rs.getInt("elo");
                System.out.println("You have successfully logged in!");
                return new User(username, password, id, coins, elo);
            } else {
                System.out.println("User not found!!!");
                // if no match
                return null;
            }
        }
    }

    public static void updateEloStat(User winner, User loser) throws SQLException {
        // Increase winner elo by 3
        winner.setEloValue(winner.getEloValue() + 3);

        // Decrease loser elo by 5
        loser.setEloValue(loser.getEloValue() - 5);

        // update elo in db
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement updateStmt = connection.prepareStatement("UPDATE users SET elo = ? WHERE username = ?")) {
            updateStmt.setInt(1, winner.getEloValue());
            updateStmt.setString(2, winner.getUsername());
            updateStmt.executeUpdate();

            updateStmt.setInt(1, loser.getEloValue());
            updateStmt.setString(2, loser.getUsername());
            updateStmt.executeUpdate();
        }
    }

    public static void buyPackage(User player) throws SQLException {
        // Check if the user has enough coins to buy a package
        if (player.getCoins() < 5) {
            // The user does not have enough coins to buy a package
            System.out.println("You do not have enough coins to buy a package!");
        } else {
            // The user has enough coins to buy a package, so subtract 5 coins from their balance
            player.setCoins(player.getCoins() - 5);

            // Generate 4 random cards for the package


            Card card1 = new MonsterCard("1","1",1,1).generateRandomizedMonsterCard();
            Card card2 = new MonsterCard("1","1",1,1).generateRandomizedMonsterCard();
            Card card3 = new MonsterCard("1","1",1,1).generateRandomizedMonsterCard();
            Card card4 = new MonsterCard("1","1",1,1).generateRandomizedMonsterCard();



            // Add the cards to the user's stack

            player.getStack().add(card1);
            player.getStack().add(card2);
            player.getStack().add(card3);
            player.getStack().add(card4);



            // Add the cards to the cards table in the database
            try (Connection connection = DriverManager.getConnection(url, user, pass);
                 PreparedStatement insertStmt = connection.prepareStatement("INSERT INTO cards (name, damage, element, cardType, userid) VALUES (?, ?, ?, ?, ?)")) {
                insertStmt.setString(1, card1.getName());
                insertStmt.setInt(2, card1.getDamage());
                insertStmt.setString(3, card1.getElementType());
                insertStmt.setString(4, "MONSTER");
                insertStmt.setInt(5, player.getId());
                insertStmt.executeUpdate();

                insertStmt.setString(1, card2.getName());
                insertStmt.setInt(2, card2.getDamage());
                insertStmt.setString(3, card2.getElementType());
                insertStmt.setString(4, "MONSTER");
                insertStmt.setInt(5, player.getId());
                insertStmt.executeUpdate();

                insertStmt.setString(1, card3.getName());
                insertStmt.setInt(2, card3.getDamage());
                insertStmt.setString(3, card3.getElementType());
                insertStmt.setString(4, "MONSTER");
                insertStmt.setInt(5, player.getId());
                insertStmt.executeUpdate();

                insertStmt.setString(1, card4.getName());
                insertStmt.setInt(2, card4.getDamage());
                insertStmt.setString(3, card4.getElementType());
                insertStmt.setString(4, "MONSTER");
                insertStmt.setInt(5, player.getId());
                insertStmt.executeUpdate();
            }
        }
    }


}
