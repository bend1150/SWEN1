package DB;

import java.util.ArrayList;
import java.util.List;
import Card.*;
import User.*;
import java.sql.*;
import java.util.Scanner;


public class CardQuery {
    private static final String url = "jdbc:postgresql://localhost:5432/cardgame";
    private static final String user ="postgres";
    private static final String pass="pwd123456";

    public static void showDeck(){

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



    public static void showStack(User player) throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, pass);

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM cards WHERE userid= " + player.getId());

        List<Card> stack = new ArrayList<>();

        System.out.println("Your stack: ");
        if (!rs.next()) {
            System.out.println("You have no cards in your stack!");
        } else {
            do {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int damage = rs.getInt("damage");
                String element = rs.getString("element");
                String cardType = rs.getString("cardtype");
                int user = rs.getInt("userid");

                if ("MONSTER".equals(cardType)) {
                    //MonsterCard card = new MonsterCard(name, element, damage, id);
                    System.out.println("Cardnr.:" + id + " " + element + name + "(" + damage + ")");
                    //stack.add(card);

                } else if ("SPELL".equals(cardType)) {
                    //SpellCard card = new SpellCard(element,damage,id);
                    System.out.println("Cardnr.:" + id + " " + element + "SPELL" + "(" + damage + ")");
                    //stack.add(card);
                }
            } while (rs.next());
        }

        conn.close();
    }


}
