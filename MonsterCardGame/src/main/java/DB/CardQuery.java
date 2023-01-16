package DB;

import java.util.ArrayList;
import java.util.List;
import Card.*;
import User.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;
import java.util.Random;
import java.util.Scanner;


public class CardQuery {
    private static final String url = "jdbc:postgresql://localhost:5432/cardgame";
    private static final String user ="postgres";
    private static final String pass="pwd123456";

    public static User getDeckFromDB(User player) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, user, pass)) {
            // Retrieve the card IDs from the 'deck' table for the given user ID
            PreparedStatement selectStmt = connection.prepareStatement("SELECT card1, card2, card3, card4 FROM deck WHERE userid = ?");
            selectStmt.setInt(1, player.getId());
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                List<Card> deck = new ArrayList<>();
                // Retrieve the corresponding card objects from the 'cards' table and add them to the user's deck list
                for (int i = 1; i <= 4; i++) {
                    int cardId = rs.getInt("card" + i);
                    PreparedStatement selectCardStmt = connection.prepareStatement("SELECT * FROM cards WHERE id = ?");
                    selectCardStmt.setInt(1, cardId);
                    ResultSet cardRs = selectCardStmt.executeQuery();
                    if (cardRs.next()) {
                        String type = cardRs.getString("cardtype");
                        if (type.equals("MONSTER")) {
                            String name = cardRs.getString("name");
                            String elementType = cardRs.getString("element");
                            int damage = cardRs.getInt("damage");
                            deck.add(new MonsterCard(name, elementType, damage, cardId,""));
                        } else if (type.equals("SPELL")) {
                            String name = cardRs.getString("name");
                            String elementType = cardRs.getString("element");
                            int damage = cardRs.getInt("damage");
                            deck.add(new SpellCard(name,elementType, damage, cardId,""));
                        }
                    }
                }
                player.setDeck(deck);
                return player;
            } else {
                System.out.println("Deck not found");
                return null;
            }
        }
    }


    public static String showDeck(User player) {
        String response = "";
        try (Connection connection = DriverManager.getConnection(url, user, pass)) {
            String selectSql = "SELECT card1,card2,card3,card4 FROM deck WHERE userid = ?";
            try (PreparedStatement statement = connection.prepareStatement(selectSql)) {
                statement.setInt(1, player.getId());
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    String card1 = rs.getString("card1");
                    String card2 = rs.getString("card2");
                    String card3 = rs.getString("card3");
                    String card4 = rs.getString("card4");
                    String[] cards = {card1, card2, card3, card4};
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>[Deck]<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                    for (String card : cards) {
                        String selectCardSql = "SELECT * FROM cards WHERE cardid = ?";
                        try (PreparedStatement cardStatement = connection.prepareStatement(selectCardSql)) {
                            cardStatement.setString(1, card);
                            ResultSet cardRs = cardStatement.executeQuery();
                            if (cardRs.next()) {
                                                                                                                                                   // Output the card
                                System.out.println("Name: " + cardRs.getString("name") + "("+cardRs.getFloat("damage")+" Damage)");
                                response += "Name: "+ cardRs.getString("name")+"("+cardRs.getFloat("damage")+")"+"\n";
                                /*
                                System.out.println("id: " + cardRs.getInt("id"));
                                System.out.println("name: " + cardRs.getString("name"));
                                System.out.println("damage: " + cardRs.getFloat("damage"));
                                System.out.println("element: " + cardRs.getString("element"));
                                System.out.println("cardtype: " + cardRs.getString("cardtype"));
                                System.out.println("cardid: " + cardRs.getString("cardid"));
                                */

                            }
                        }
                    }
                }
                return response;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return response;
        }
    }





    public static int configureDeck(User player, String card1, String card2, String card3, String card4) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, user, pass)) {
            String checkSql = "SELECT * FROM cards WHERE userid=? AND(cardid = ? OR cardid = ? OR cardid = ? OR cardid = ?)";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setInt(1,player.getId());
                checkStmt.setString(2, card1);
                checkStmt.setString(3, card2);
                checkStmt.setString(4, card3);
                checkStmt.setString(5, card4);
                ResultSet rs = checkStmt.executeQuery();
                int count = 0;
                while (rs.next()) {
                    count++;
                }
                if (count != 4) {
                    return 1; // not all cards exist in cards table
                }
            }
            // The inserts update the row if the userid already exist, to avoid many userid's
            String insertSql = "INSERT INTO deck (userid, card1, card2, card3, card4) VALUES (?, ?, ?, ?, ?) ON CONFLICT (userid) DO UPDATE SET card1 = ?, card2 = ?, card3 = ?, card4 = ?";
            try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                insertStmt.setInt(1, player.getId());
                insertStmt.setString(2, card1);
                insertStmt.setString(3, card2);
                insertStmt.setString(4, card3);
                insertStmt.setString(5, card4);
                insertStmt.setString(6, card1);
                insertStmt.setString(7, card2);
                insertStmt.setString(8, card3);
                insertStmt.setString(9, card4);

                insertStmt.executeUpdate();
            }
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }


    public static void logout(User player) throws SQLException {                        //removes token from user
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement updateStmt = connection.prepareStatement("UPDATE users SET token = null WHERE id = ?")) {
            updateStmt.setInt(1, player.getId());
            int rowsAffected = updateStmt.executeUpdate();
        }
    }

    public static int acquirePackage(User player) {
        try (Connection connection = DriverManager.getConnection(url, user, pass)) {
            connection.setAutoCommit(false);
            // check if enough coins
            String checkCoinsSql = "SELECT coins FROM users WHERE id = ?";
            try (PreparedStatement checkCoinsStmt = connection.prepareStatement(checkCoinsSql)) {
                checkCoinsStmt.setInt(1, player.getId());
                ResultSet checkCoinsRs = checkCoinsStmt.executeQuery();
                if (checkCoinsRs.next()) {
                    int coins = checkCoinsRs.getInt("coins");
                    if (coins < 5) {
                        // player does not have enough money
                        return 1;
                    }
                }
            }
            // query -5 to db
            String updateCoinsSql = "UPDATE users SET coins = coins - 5 WHERE id = ?";
            try (PreparedStatement updateCoinsStmt = connection.prepareStatement(updateCoinsSql)) {
                updateCoinsStmt.setInt(1, player.getId());
                updateCoinsStmt.executeUpdate();
            }
            // change userid on table
            String acquirePackageSql = "UPDATE cards SET userid = ? WHERE id IN (SELECT id FROM cards WHERE userid IS NULL ORDER BY id LIMIT 5)";
            try (PreparedStatement acquirePackageStmt = connection.prepareStatement(acquirePackageSql)) {
                acquirePackageStmt.setInt(1, player.getId());
                int rowsAffected = acquirePackageStmt.executeUpdate();
                if (rowsAffected == 0) {
                    return 2;
                }
            }
            connection.commit();
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }





    public static void createDeck(User player) throws SQLException {
        // Connect to the database
        Connection conn = DriverManager.getConnection(url, user, pass);

        // Create a scanner to read input from the user
        Scanner scanner = new Scanner(System.in);

        // Prompt the user for the IDs of the cards they want to add to their deck
        System.out.println("Enter the IDs of the cards you want to add to your deck, separated by spaces: ");
        String input = scanner.nextLine();

        // Split the input into an array of card IDs
        String[] cardIds = input.split(" ");

        // Retrieve the selected cards from the 'cards' table
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM cards WHERE id IN (?, ?, ?, ?) AND userid = ?");

        stmt.setInt(1, Integer.parseInt(cardIds[0])); // Set the first card ID
        stmt.setInt(2, Integer.parseInt(cardIds[1])); // Set the second card ID
        stmt.setInt(3, Integer.parseInt(cardIds[2])); // Set the third card ID
        stmt.setInt(4, Integer.parseInt(cardIds[3])); // Set the fourth card ID
        stmt.setInt(5, player.getId());                 // Set the player's ID
        ResultSet rs = stmt.executeQuery();

        // Check that the retrieved cards are owned by the player
        List<Integer> allowedCardIds = new ArrayList<>();
        while (rs.next()) {
            int userId = rs.getInt("userid");
            int cardId = rs.getInt("id");
            if (userId == player.getId()) {
                allowedCardIds.add(cardId);
            }
        }

        if (allowedCardIds.size() != cardIds.length) {
            System.out.println("Card not found in your stack");
            return;
        }

        // Use an upsert statement to insert a row into the 'deck' table for the player
        PreparedStatement upsertStmt = conn.prepareStatement("INSERT INTO deck (userid, card1, card2, card3, card4) VALUES (?, ?, ?, ?, ?) ON CONFLICT (userid) DO UPDATE SET card1 = ?, card2 = ?, card3 = ?, card4 = ?");
        upsertStmt.setInt(1, player.getId()); // Set the user ID
        upsertStmt.setInt(2, allowedCardIds.get(0)); // Set the first card ID
        upsertStmt.setInt(3, allowedCardIds.get(1)); //

        // Set the second card ID
        upsertStmt.setInt(4, allowedCardIds.get(2)); // Set the third card ID
        upsertStmt.setInt(5, allowedCardIds.get(3)); // Set the fourth card ID
        upsertStmt.setInt(6, allowedCardIds.get(0)); // Set the first card ID (for update)
        upsertStmt.setInt(7, allowedCardIds.get(1)); // Set the second card ID (for update)
        upsertStmt.setInt(8, allowedCardIds.get(2)); // Set the third card ID (for update)
        upsertStmt.setInt(9, allowedCardIds.get(3)); // Set the fourth card ID (for update)
        upsertStmt.executeUpdate();

        // Close the connection to the database
        conn.close();
    }



    public static List<Card> showStack(User player) throws SQLException {

        try(Connection conn = DriverManager.getConnection(url, user, pass);
        //Statement stmt = conn.createStatement();
        //ResultSet rs = stmt.executeQuery("SELECT * FROM cards WHERE userid= " + player.getId());
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM cards WHERE userid= ?")) {

            stmt.setInt(1, player.getId());
            ResultSet rs = stmt.executeQuery();

            List<Card> stack = new ArrayList<>();

            if (!rs.next()) {
                System.out.println("You have no cards in your stack!");
            } else {
                do {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    float damage = rs.getFloat("damage");
                    String element = rs.getString("element");
                    String cardType = rs.getString("cardtype");
                    String cardId = rs.getString("cardid");
                    int user = rs.getInt("userid");

                    if ("MONSTER".equals(cardType)) {
                        MonsterCard card = new MonsterCard(name, element, damage, id, cardId);
                        stack.add(card);

                    } else if ("SPELL".equals(cardType)) {
                        SpellCard card = new SpellCard(name, element, damage, id, cardId);
                        stack.add(card);
                    }
                } while (rs.next());
            }
            conn.close();
            return stack;
        } catch (SQLException ex){
            System.out.println(ex);
            return null;
        }
    }



    // OLD VERSION
    public static /*void*/ int buyPackage(User player) throws SQLException {            // Maybe should also return User so the stack gets updated?
        // Check if the user has enough coins to buy a package
        if (player.getCoins() < 5) {
            // The user does not have enough coins to buy a package
            System.out.println("You do not have enough coins to buy a package!");
            return 0;
        } else {
            // The user has enough coins to buy a package, so subtract 5 coins from their balance
            player.setCoins(player.getCoins() - 5);

            // Generate 4 random cards for the package

            Random rand = new Random();

            for(int i=0; i < 5; i++) {
                int cardType = rand.nextInt(2);     //random number between 1 and 2
                Card card;
                if(cardType == 0){
                    card = new MonsterCard("1","1",1,1,"").generateRandomizedMonsterCard();
                } else {
                    card = new SpellCard("1","1",1,1,"").generateRandomizedSpellcard();
                }
                // Add to stack
                player.getStack().add(card);

                // Add the cards to the cards table in the database
                try (Connection connection = DriverManager.getConnection(url, user, pass);
                     PreparedStatement insertStmt = connection.prepareStatement("INSERT INTO cards (name, damage, element, cardType, userid) VALUES (?, ?, ?, ?, ?)")) {
                    insertStmt.setString(1, card.getName());
                    insertStmt.setFloat(2, card.getDamage());
                    insertStmt.setString(3, card.getElementType());
                    insertStmt.setString(4, cardType==0 ? "MONSTER" : "SPELL");
                    insertStmt.setInt(5, player.getId());
                    insertStmt.executeUpdate();
                }
                catch (SQLException ex){
                    System.out.println(ex);
                    return -1;
                }

            }
            // Decrease coin -5 in DB
            try (Connection connection = DriverManager.getConnection(url, user, pass);
                 PreparedStatement updateStmt = connection.prepareStatement("UPDATE users SET coins = coins - 5 WHERE id = ?")) {
                updateStmt.setInt(1, player.getId());
                updateStmt.executeUpdate();
            }
            catch (SQLException ex){
                System.out.println(ex);
                return -1;
            }
            return 0;

        }
    }

    public static User updateStack(User user1, User user2){
        // This function belongs at the end of the BattleLogic so the
        //  DB is updated with the current Version of the Stack and deck of the player
        return null;
    }

    public static int createPackage(List<Card> myCard, User player){

        try (Connection connection = DriverManager.getConnection(url, user, pass)) {
            String insertSql = "INSERT INTO cards (name, damage, cardid,element,cardtype) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(insertSql)) {
                for(Card card: myCard){
                    statement.setString(1, card.getName());
                    statement.setFloat(2, card.getDamage());
                    statement.setString(3, card.getCardId());
                    statement.setString(4,card.getElementType());
                    //statement.setInt(5,player.getId());
                    if(card instanceof SpellCard){
                        statement.setString(5, "SPELL");
                    } else if(card instanceof MonsterCard) {
                        statement.setString(5,"MONSTER");
                    }

                    //statement.setString(4, /*insert here*/);
                    statement.executeUpdate();
                }
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
