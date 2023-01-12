package DB;

import java.util.ArrayList;
import java.util.List;
import Card.*;
import User.*;
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
                            deck.add(new MonsterCard(name, elementType, damage, cardId));
                        } else if (type.equals("SPELL")) {
                            String elementType = cardRs.getString("element");
                            int damage = cardRs.getInt("damage");
                            deck.add(new SpellCard(elementType, damage, cardId));
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



    public static void buyPackage(User player) throws SQLException {            // Maybe should also return User so the stack gets updated?
        // Check if the user has enough coins to buy a package
        if (player.getCoins() < 5) {
            // The user does not have enough coins to buy a package
            System.out.println("You do not have enough coins to buy a package!");
        } else {
            // The user has enough coins to buy a package, so subtract 5 coins from their balance
            player.setCoins(player.getCoins() - 5);

            // Generate 4 random cards for the package

            Random rand = new Random();

            for(int i=0; i < 5; i++) {
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

    public static User updateStack(User user1, User user2){
        // This function belongs at the end of the BattleLogic so the
        //  DB is updated with the current Version of the Stack and deck of the player
        return null;
    }


}
