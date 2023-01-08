import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import DB.*;
import Card.*;
import User.*;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) throws SQLException {
        System.out.println("Hi");

        Database.openConnection();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username:");
        String username = scanner.nextLine();

        System.out.println(("Enter password: "));
        String password = scanner.nextLine();

        //User user1 = UserQuery.loginUser(username,password);
        User user1 = UserQuery.loginUser(username,password);



        CardQuery.showStack(user1);
        CardQuery.createDeck(user1);
        /*
        System.out.println("Enter your username2:");
        String username2 = scanner.nextLine();

        System.out.println(("Enter password2: "));
        String password2 = scanner.nextLine();
        */
        //User user2 = UserQuery.loginUser(username2,password2);

        //UserQuery.updateEloStat(user1, user2);



        User player1 = new User("Ben","123",1,20,1000);
        User player2 = new User("Viv","345",2,19,1001);

        /*
        MonsterCard card1 = new MonsterCard("ORC","WATER", 48,1);
        MonsterCard card2 = new MonsterCard("KRAKEN","FIRE", 16,2);
        MonsterCard card3 = new MonsterCard("ELF","NORMAL", 55,3);
        SpellCard card4 = new SpellCard("NORMAL",8,4);
        SpellCard card5 = new SpellCard("WATER",7, 5);
        SpellCard card6 = new SpellCard("FIRE",15, 6);

        MonsterCard card7 = new MonsterCard("GOBLIN","NORMAL", 34,7);
        MonsterCard card8 = new MonsterCard("WIZZARD","WATER", 24,8);
        MonsterCard card9 = new MonsterCard("DRAGON","FIRE", 41,9);
        SpellCard card10 = new SpellCard("WATER",14,10);
        SpellCard card11 = new SpellCard("NORMAL",15, 11);
        SpellCard card12 = new SpellCard("FIRE",6, 12);
        */


        MonsterCard card58 = new MonsterCard("1","1",1,1).generateRandomizedMonsterCard();
        MonsterCard card59 = new MonsterCard("1","1",1,1).generateRandomizedMonsterCard();


        List<Card> deck1 = new ArrayList<Card>();
        deck1.add(card58);
        /*deck1.add(card2);
        deck1.add(card3);
        deck1.add(card4);
        deck1.add(card5);
        deck1.add(card6);
        */

        player1.setDeck(deck1);

        List<Card> deck2 = new ArrayList<Card>();
        deck2.add(card59);
        /*
        deck2.add(card8);
        deck2.add(card9);
        deck2.add(card10);
        deck2.add(card11);
        deck2.add(card12);
        */


        player2.setDeck(deck2);


/*
        int victory = BattleLogic.battle(player1,player2);
        if(victory == 1 || victory == 2) {
            System.out.println(" >>>>>>>>>>>>>>>>>>>Player" + victory + " wins!!!<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        } else if(victory == 3){
            System.out.println("DRAW!!!");
        }
*/

    }

}