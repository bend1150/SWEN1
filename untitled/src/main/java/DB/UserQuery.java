package DB;

import java.util.Random;
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

            Random rand = new Random();

            for(int i=0; i < 4; i++) {
                int cardType = rand.nextInt(2);     //random number between 1 and 2
                Card card;
                if(cardType == 0){
                    card = new MonsterCard("1","1",1,1).generateRandomizedMonsterCard();
                } else {
                    card = new SpellCard("1",1,1).generateRandomizedSpellcard();
                }
                // Add to stack
                player.getStack().add(card);

                // Add the cards to the cards table in the database
                try (Connection connection = DriverManager.getConnection(url, user, pass);
                     PreparedStatement insertStmt = connection.prepareStatement("INSERT INTO cards (name, damage, element, cardType, userid) VALUES (?, ?, ?, ?, ?)")) {
                    insertStmt.setString(1, card.getName());
                    insertStmt.setInt(2, card.getDamage());
                    insertStmt.setString(3, card.getElementType());
                    insertStmt.setString(4, cardType==0 ? "MONSTER" : "SPELL");
                    insertStmt.setInt(5, player.getId());
                    insertStmt.executeUpdate();
                }

            }
            // Decrease coin -5 in DB
            try (Connection connection = DriverManager.getConnection(url, user, pass);
                 PreparedStatement updateStmt = connection.prepareStatement("UPDATE users SET coins = coins - 5 WHERE id = ?")) {
                updateStmt.setInt(1, player.getId());
                updateStmt.executeUpdate();
            }

        }
    }


}
