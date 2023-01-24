package DB;

import java.util.ArrayList;
import java.sql.*;
import java.util.List;
import java.util.Scanner;

import Card.Card;
import User.*;


public class UserRepository {

    private static final String url = "jdbc:postgresql://localhost:5432/cardgame";
    private static final String user ="postgres";
    private static final String pass="pwd123456";



    public void editUserData(User player, String name, String bio, String image) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, user, pass)) {
            String sql = "UPDATE users SET name = ?, bio = ?, image = ? WHERE id = ?";
            PreparedStatement updateStmt = connection.prepareStatement(sql);
            updateStmt.setString(1, name);
            updateStmt.setString(2, bio);
            updateStmt.setString(3, image);
            updateStmt.setInt(4, player.getId());
            updateStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public int registerUser(String username, String password) throws SQLException {
        // Check if the username is already in use
        boolean usernameExists = false;
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement selectStmt = connection.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            selectStmt.setString(1, username);
            ResultSet rs = selectStmt.executeQuery();
            if (((ResultSet) rs).next()) {          //if user exist
                usernameExists = true;
            }
        }
        catch (SQLException ex){
            System.out.println(ex);
            return -1;
        }

        if (usernameExists) {
            // Error if user already exist
            System.out.println("Error: the username '" + username + "' already exist.");
            return 1;
        } else {
            // Insert the new user
            try (Connection connection = DriverManager.getConnection(url, user, pass);
                 PreparedStatement insertStmt = connection.prepareStatement("""
            INSERT INTO users
            (username, password, coins, elo, gamesCount, wins)
            VALUES (?,?,?,?,?,?);
            """)) {
                insertStmt.setString(1, username);
                insertStmt.setString(2, password);
                insertStmt.setInt(3, 20);
                insertStmt.setInt(4, 100);
                insertStmt.setInt(5,0);
                insertStmt.setInt(6,0);
                insertStmt.execute();

                return 0;
            }
            catch (SQLException ex){
                System.out.println(ex);
                return -1;
            }
        }
    }

    public User showUserData(User player) throws  SQLException{
        try(Connection conn = DriverManager.getConnection(url, user, pass);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE userid= ?")) {

            stmt.setInt(1, player.getId());
            ResultSet rs = stmt.executeQuery();

            List<Card> stack = new ArrayList<>();

            if (!rs.next()) {
                System.out.println("You have no cards in your stack!");         //if empty
            } else {
                do {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    float damage = rs.getFloat("damage");
                    String element = rs.getString("element");
                    String cardType = rs.getString("cardtype");
                    String cardId = rs.getString("cardid");
                    int user = rs.getInt("userid");

                } while (rs.next());
            }
            conn.close();

        } catch (SQLException ex){
            System.out.println(ex);
            return null;
        }
        return null;
    }

    public /*User*/ int loginUser(String username, String password) throws SQLException {
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
                int gamesCount = rs.getInt("gamesCount");
                int wins = rs.getInt("wins");
                String token = "Basic " + username + "-mtcgToken";
                PreparedStatement updateStmt = connection.prepareStatement("UPDATE users SET token = ? WHERE id = ?");
                updateStmt.setString(1, token);
                updateStmt.setInt(2, id);
                updateStmt.executeUpdate();
                System.out.println("You have successfully logged in!");
                //return new User(username, password, id, coins, elo,gamesCount, wins);
                return 0;
            } else {
                System.out.println("User not found!!!");
                // if no match
                //return null;
                return 1;
            }
        }
    }

    public User getUserInfoByToken(String token) {
        User player = null;
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement selectStmt = connection.prepareStatement("SELECT * FROM users WHERE token = ?")) {
            selectStmt.setString(1, token);
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                // if user exist return User object
                String username = rs.getString("username");
                String password = rs.getString("password");
                int id = rs.getInt("id");
                int coins = rs.getInt("coins");
                int elo = rs.getInt("elo");
                int gamesCount = rs.getInt("gamesCount");
                int wins = rs.getInt("wins");
                String name = rs.getString("name");
                String bio = rs.getString("bio");
                String image = rs.getString("image");
                player = new User(username, password, id, coins, elo,gamesCount, wins,name,bio,image);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return player;
    }

    public int logout(User player) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement updateStmt = connection.prepareStatement("UPDATE users SET token = '' WHERE id = ?")) {
            updateStmt.setInt(1, player.getId());
            updateStmt.executeUpdate();
            return 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }
    }


    public User getWinnerTest() throws SQLException {            //only for test
        User winner = null;
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement selectStmt = connection.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            selectStmt.setString(1, "winnerTest");
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                int elo = rs.getInt("elo");
                int gamesCount = rs.getInt("gamescount");
                int coins = rs.getInt("coins");
                int wins = rs.getInt("wins");
                String name = rs.getString("name");
                String bio = rs.getString("bio");
                String image = rs.getString("image");
                winner = new User(username,password,id,coins,elo,gamesCount,wins,name,bio,image);
            }
        }
        return winner;
    }

    public User getLoserTest() throws SQLException {            //only for test
        User loser = null;
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement selectStmt = connection.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            selectStmt.setString(1, "loserTest");
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                int elo = rs.getInt("elo");
                int gamesCount = rs.getInt("gamescount");
                int coins = rs.getInt("coins");
                int wins = rs.getInt("wins");
                String name = rs.getString("name");
                String bio = rs.getString("bio");
                String image = rs.getString("image");
                loser = new User(username,password,id,coins,elo,gamesCount,wins,name,bio,image);
            }
        }
        return loser;
    }

    public void resetTestUser(User player) throws SQLException {                 // Only for test
        try (Connection connection = DriverManager.getConnection(url, user, pass)) {
            String sql = "UPDATE users SET coins = 20, elo = 100, gamescount = 0, wins = 0 WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, player.getId());
                statement.executeUpdate();
            }
        }
    }

    public void updateGamesCount(User player1,User player2) throws SQLException{            //only updates gamesCount -> DRAW
        player1.setGamesCount(player1.getGamesCount()+1);
        player2.setGamesCount(player2.getGamesCount()+1);

        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement updateStmt = connection.prepareStatement("UPDATE users SET gamescount = ? WHERE id = ?")) {
            updateStmt.setInt(1,player1.getGamesCount());
            updateStmt.setInt(2, player1.getId());
            updateStmt.executeUpdate();

            updateStmt.setInt(1,player2.getGamesCount());
            updateStmt.setInt(2, player2.getId());
            updateStmt.executeUpdate();
        }

    }


    public void updateStats(User winner, User loser) throws SQLException {
        // Winner elo by +3 and wins by +1
        winner.setEloValue(winner.getEloValue() + 3);
        winner.setWins(winner.getWins() + 1);
        winner.setGamesCount(winner.getGamesCount()+1);

        // loser elo by -5
        loser.setEloValue(loser.getEloValue() - 5);
        loser.setGamesCount(loser.getGamesCount()+1);

        // update elo and wins in db
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement updateStmt = connection.prepareStatement("UPDATE users SET elo = ?, wins = ?, gamescount = ? WHERE id = ?")) {
            updateStmt.setInt(1, winner.getEloValue());
            updateStmt.setInt(2, winner.getWins());
            updateStmt.setInt(3,winner.getGamesCount());
            updateStmt.setInt(4, winner.getId());
            updateStmt.executeUpdate();

            updateStmt.setInt(1, loser.getEloValue());
            updateStmt.setInt(2, loser.getWins());
            updateStmt.setInt(3,loser.getGamesCount());
            updateStmt.setInt(4, loser.getId());
            updateStmt.executeUpdate();
        }
    }


    public static String showScoreBoard() throws SQLException {

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>[ Scoreboard]<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        System.out.printf("| %-5s | %-15s | %-10s | %-10s |\n", "elo", "username", "gamesCount", "winRatio");
        System.out.println("----------------------------------------------------------------------");
        String response ="";
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement selectStmt = connection.prepareStatement("SELECT username, elo, gamesCount, wins FROM users ORDER BY elo DESC")) {
            ResultSet rs = selectStmt.executeQuery();
            while (rs.next()) {
                String username = rs.getString("username");
                int elo = rs.getInt("elo");
                int gamesCount = rs.getInt("gamesCount");
                int wins = rs.getInt("wins");

                float winRatio = 0;
                if (gamesCount != 0) {
                    winRatio = (wins / (float) gamesCount) * 100;           //so divide by 0 not possible
                }

                System.out.printf("| %-5d | %-15s | %-10d | %-10.2f%% |\n", elo,username, gamesCount, winRatio);
                response += String.format("| %-5d | %-15s | %-10d | %-10.2f%% |\n", elo,username, gamesCount, winRatio);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("----------------------------------------------------------------------");
        return response;
    }


    //old version
    public static User editProfile(User player) throws SQLException {               //prompts INPUT
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter new username (leave empty if you dont want to chagne it): ");
        String newUsername = scanner.nextLine();
        System.out.println("Enter old password: ");
        String oldPassword = scanner.nextLine();
        System.out.println("Enter new password (leave empty if you dont want to chagne it): ");
        String newPassword = scanner.nextLine();

        //verifying the old password and updating the new username and password
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement selectStmt = connection.prepareStatement("SELECT * FROM users WHERE id = ? AND password = ?")) {
            selectStmt.setInt(1, player.getId());
            selectStmt.setString(2, oldPassword);
            ResultSet rs = selectStmt.executeQuery();
            if (!rs.next()) {
                System.out.println("Error: Incorrect password!");
                return null;
            }
        }
        if(!newUsername.isEmpty()){             //changes if non empty
            player.setUsername(newUsername);
        }
        if(!newPassword.isEmpty()){
            player.setPassword(newPassword);
        }

        //update to the database
        try (Connection connection = DriverManager.getConnection(url, user, pass)) {
            String sql = "UPDATE users SET";
            ArrayList<Object> values = new ArrayList<Object>();
            if(!newUsername.isEmpty()){             //if non empty inserts into sql string
                sql += " username = ?,";
                values.add(newUsername);
            }
            if(!newPassword.isEmpty()){
                sql += " password = ?,";
                values.add(newPassword);
            }
            if(values.size()>0){
                sql = sql.substring(0, sql.length()-1);
                sql += " WHERE id = ?";
                values.add(player.getId());
                PreparedStatement updateStmt = connection.prepareStatement(sql);
                for (int i = 0; i < values.size(); i++) {
                    updateStmt.setObject(i + 1, values.get(i));     // updateStmt depends of amount value
                }
                updateStmt.execute();
                System.out.println("Profile updated!");
                return player;
            }
        }
        return null;
    }

    public static User editProfileNew(User player, String newUsername, String oldPassword, String newPassword) throws SQLException {    //Test (Old version)

        //verifying the old password
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement selectStmt = connection.prepareStatement("SELECT * FROM users WHERE id = ? AND password = ?")) {
            selectStmt.setInt(1, player.getId());
            selectStmt.setString(2, oldPassword);
            ResultSet rs = selectStmt.executeQuery();
            if (!rs.next()) {           //if nothing found
                System.out.println("Error: Incorrect password!");
                return player;
            }
        }
        if (!newUsername.isEmpty()) {             // change if non empty
            player.setUsername(newUsername);
        }
        if (!newPassword.isEmpty()) {
            player.setPassword(newPassword);
        }

        //update to db
        try (Connection connection = DriverManager.getConnection(url, user, pass)) {
            String sql = "UPDATE users SET";
            ArrayList<Object> values = new ArrayList<Object>();
            if (!newUsername.isEmpty()) {
                sql += " username = ?,";            // if non empty insert into sql string
                values.add(newUsername);
            }
            if (!newPassword.isEmpty()) {
                sql += " password = ?,";
                values.add(newPassword);
            }
            if (values.size() > 0) {
                sql = sql.substring(0, sql.length() - 1); //removes last character ","
                sql += " WHERE id = ?";
                values.add(player.getId());
                PreparedStatement updateStmt = connection.prepareStatement(sql);
                for (int i = 0; i < values.size(); i++) {                       //depending on values, updateStmt
                    updateStmt.setObject(i + 1, values.get(i));
                }
                updateStmt.execute();
                System.out.println("Profile successfully updated!");
                return player;
            }
        }
        return null;


    }



    }
