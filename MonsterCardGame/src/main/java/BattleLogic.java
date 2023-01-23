
import java.util.Random;
import Card.*;
import User.*;
import DB.*;
import java.sql.*;

public class BattleLogic {


         public int battle(User user1, User user2){

            //user1 = CardQuery.getDeckFromDB(user1);
            //user2 = CardQuery.getDeckFromDB(user2);

            int numberRound = 1;
            int victoryPlayer = 0;
            // 1: Player1 wins; 2: Player2 wins ; 3: Draw
            int victoryCard = 0;



            while(user1.getDeck().size()>0 && user2.getDeck().size()>0) {         //continue if both player still has card in deck
                System.out.println("---------------Round "+numberRound+"---------------");
                int num1 = ((int) (Math.random() * user1.getDeck().size()));
                int num2 = ((int) (Math.random() * user2.getDeck().size()));
                Card card1 = user1.getDeck().get(num1);
                Card card2 = user2.getDeck().get(num2);

                if (card1 instanceof MonsterCard && card2 instanceof MonsterCard){ // Pure monster battle
                    System.out.println("Player1: "+card1.getElementType()+card1.getName()+"("+card1.getDamage()+")"+" VS Player2: "+card2.getElementType()+card2.getName()+"("+card2.getDamage()+")");
                    victoryCard = MonsterBattle(card1, card2);
                }

                if (card1 instanceof SpellCard && card2 instanceof SpellCard){ // Pure spell battle
                    System.out.println("Player1: "+card1.getElementType()+"Spell ("+card1.getDamage()+")"+" VS Player2: "+card2.getElementType()+"Spell ("+card2.getDamage()+")");
                    victoryCard = SpellBattle(card1, card2);
                }
                if((card1 instanceof MonsterCard && card2 instanceof SpellCard)||(card1 instanceof SpellCard && card2 instanceof MonsterCard)){
                    if(card1 instanceof MonsterCard){
                        System.out.println("Player1: "+card1.getElementType()+card1.getName()+ " ("+card1.getDamage()+") VS Player2: "+card2.getElementType()+"Spell ("+card2.getDamage()+")");
                    } else {
                        System.out.println("Player1: "+card1.getElementType()+"Spell ("+card1.getDamage()+") VS Player2: "+card2.getElementType()+card2.getName()+" ("+card2.getDamage()+")");
                    }
                    victoryCard = MixedBattle(card1, card2);
                }

                // log winning card
                if(victoryCard == 1 || victoryCard == 2) {
                    System.out.println("Player" + victoryCard + " wins!!!");
                } else {
                    System.out.println("DRAW!!!     NO ACTION TAKES PLACE!!!");
                }
                if (victoryCard==1){
                    user2.getDeck().remove(card2);
                    user1.getDeck().add(card2);
                }
                if (victoryCard==2){
                    user1.getDeck().remove(card1);
                    user2.getDeck().add(card1);
                }
                numberRound++;
                if(numberRound>99){
                    return 3;
                }
            }

            //Maybe hier updateStack() zu DB damit die karten in der Tabelle übergeben wird (id wird auf den Gewinner geändert)

            if(user1.getDeck().size()==0 && user2.getDeck().size()==0) {
                return 3;
            }

            if(user1.getDeck().size()==0){
                victoryPlayer = 2;
            } else {
                victoryPlayer = 1;
            }
            return victoryPlayer;
        }

         public int MonsterBattle(Card card1, Card card2){             //returniert gewonnene Karte
            System.out.println("this is a PURE MonsterBattle!");
            if((card1.getName().contains("GOBLIN")||card2.getName().contains("GOBLIN"))&&(card1.getName().contains("DRAGON")||card2.getName().contains("DRAGON"))){
                System.out.println("GOBLIN IS AFRAID OF DRAGON!!!");
                if(card1.getName().contains("GOBLIN")){              //Goblin afraid of Dragon
                    return 2;
                }
                return 1;
            }
            if((card1.getName().contains("ORK")||card2.getName().contains("ORK")&&(card1.getName().contains("WIZZARD")||card2.getName().contains("WIZZARD")))){
                System.out.println("WIZZARDS CONTROLLS ORKS");
                if(card1.getName().contains("ORK")){                 //Wizzard controlls Orcs
                    return 2;
                }
                return 1;
            }
            if(((card1.getName().contains("ELF") && card1.getElementType().contains("FIRE") || (card2.getName().contains("ELF") && card2.getElementType().contains("FIRE"))) && (card1.getName().contains("DRAGON")||card2.getName().contains("DRAGON")))){
                System.out.println("ELVES EVADES DRAGONS ATTACKS");
                if(card1.getName().contains("DRAGON")){                 // FireElves evade Dragon
                    return 2;
                }
                return 1;
            }

            System.out.println(" => "+card1.getDamage() + " VS " +card2.getDamage());
            if(card1.getDamage() > card2.getDamage()){
                return 1;
            }
            if(card2.getDamage() > card1.getDamage()){
                return 2;
            }
            if(card1.getDamage() == card2.getDamage()){
                return 3;
            }

            return 1;//platzhalter
        }

         public int SpellBattle(Card card1, Card card2) {
            System.out.println("This is a PURE Spellbattle!");
            float card1dmg = card1.getDamage();
            float card2dmg = card2.getDamage();
            String card1Type = card1.getElementType();
            String card2Type = card2.getElementType();

            // Damage-Effectiveness
            if (card1Type.equalsIgnoreCase("WATER") && card2Type.equalsIgnoreCase("FIRE")){
                card1dmg = card1dmg * 2;
            }   else if(card1Type.equalsIgnoreCase("FIRE") && card2Type.equalsIgnoreCase("WATER")){
                card1dmg = card1dmg / 2;
            }   else if(card1Type.equalsIgnoreCase("NORMAL") && card2Type.equalsIgnoreCase("WATER")){
                card1dmg = card1dmg * 2;
            }   else if(card1Type.equalsIgnoreCase("WATER") && card2Type.equalsIgnoreCase("NORMAL")){
                card1dmg = card1dmg / 2;
            }   else if(card1Type.equalsIgnoreCase("FIRE") && card2Type.equalsIgnoreCase("NORMAL")){
                card1dmg = card1dmg * 2;
            }   else if(card1Type.equalsIgnoreCase("NORMAL") && card2Type.equalsIgnoreCase("FIRE")){
                card1dmg = card1dmg / 2;
            }   else if(card1Type.equalsIgnoreCase("ICE") && card2Type.equalsIgnoreCase("WATER")){      //Unique Feature
                card1dmg = card1dmg * 2;
            }   else if(card1Type.equalsIgnoreCase("WATER") && card2Type.equalsIgnoreCase("ICE")){
                card1dmg = card1dmg /2;
            }   else if(card1Type.equalsIgnoreCase("ICE") && card2Type.equalsIgnoreCase("FIRE")){
                card1dmg = card1dmg/2;
            }else if(card1Type.equalsIgnoreCase("FIRE") && card2Type.equalsIgnoreCase("ICE")){
                card1dmg = card1dmg*2;
            }



            if (card2Type.equals("WATER") && card1Type.equals("FIRE")){
                card2dmg = card2dmg * 2;
            }   else if(card2Type.equals("FIRE") && card1Type.equals("WATER")){
                card2dmg = card2dmg / 2;
            }   else if(card2Type.equals("NORMAL") && card1Type.equals("WATER")){
                card2dmg = card2dmg * 2;
            }   else if(card2Type.equals("WATER") && card1Type.equals("NORMAL")){
                card2dmg = card2dmg / 2;
            }   else if(card2Type.equals("FIRE") && card1Type.equals("NORMAL")){
                card2dmg = card2dmg * 2;
            }   else if (card2Type.equals("NORMAL") && card1Type.equals("FIRE")) {
                card2dmg = card2dmg / 2;
            }   else if(card2Type.equals("ICE") && card1Type.equals("WATER")){
                card2dmg = card2dmg * 2;
            }   else if(card2Type.equals("WATER") && card1Type.equals("ICE")){
                card2dmg = card2dmg /2;
            }   else if(card2Type.equals("ICE") && card1Type.equals("FIRE")){
                card2dmg = card2dmg/2;
            }else if(card2Type.equals("FIRE") && card1Type.equals("ICE")){
                card2dmg = card2dmg*2;
            }




            System.out.println(" => "+card1dmg + " VS " +card2dmg);
            if(card1dmg>card2dmg) {
                return 1;
            }
            if(card2dmg>card1dmg) {
                return 2;
            }
            if (card1dmg == card2dmg) {
                return 3;
            }
            return 0;
        }
         public int MixedBattle(Card card1, Card card2){
            float card1dmg = card1.getDamage();
            float card2dmg = card2.getDamage();
            String card1Name = card1.getName();
            String card2Name = card2.getName();
            String card1Type = card1.getElementType();
            String card2Type = card2.getElementType();
            System.out.println("This is a mixed Battle!");

            if ((card1Name.contains("KNIGHT") && card2Type.contains("WATER"))||(card2Name.contains("KNIGHT") && card1Type.contains("WATER"))){
                System.out.println("KNIGHTS DROWNS AGAINST WATERSPELLS!!!");
                if(card1Name.contains("KNIGHT")){          //Knights drowns against WaterSpells
                    return 2;
                } else {
                    return 1;
                }
            }

            if((card1Name.contains("KRAKEN") && card2 instanceof SpellCard)||(card2Name.contains("KRAKEN") && card1 instanceof SpellCard)){
                System.out.println("KRAKEN IS IMMUNE AGAINST SPELLS!!!");
                if(card1Name.contains("KRAKEN")){
                    return 1;
                } else {
                    return 2;
                }
            }

            // Damage-Effectiveness
            if (card1Type.equalsIgnoreCase("WATER") && card2Type.equalsIgnoreCase("FIRE")){
                card1dmg = card1dmg * 2;
            }   else if(card1Type.equalsIgnoreCase("FIRE") && card2Type.equalsIgnoreCase("WATER")){
                card1dmg = card1dmg / 2;
            }   else if(card1Type.equalsIgnoreCase("NORMAL") && card2Type.equalsIgnoreCase("WATER")){
                card1dmg = card1dmg * 2;
            }   else if(card1Type.equalsIgnoreCase("WATER") && card2Type.equalsIgnoreCase("NORMAL")){
                card1dmg = card1dmg / 2;
            }   else if(card1Type.equalsIgnoreCase("FIRE") && card2Type.equalsIgnoreCase("NORMAL")){
                card1dmg = card1dmg * 2;
            }   else if(card1Type.equalsIgnoreCase("NORMAL") && card2Type.equalsIgnoreCase("FIRE")){
                card1dmg = card1dmg / 2;
            }   else if(card1Type.equalsIgnoreCase("ICE") && card2Type.equalsIgnoreCase("WATER")){
                card1dmg = card1dmg * 2;
            }   else if(card1Type.equalsIgnoreCase("WATER") && card2Type.equalsIgnoreCase("ICE")){
                card1dmg = card1dmg / 2;
            }   else if(card1Type.equalsIgnoreCase("ICE") && card2Type.equalsIgnoreCase("FIRE")){
                card1dmg = card1dmg / 2;
            }   else if(card1Type.equalsIgnoreCase("FIRE") && card2Type.equalsIgnoreCase("ICE")){
                card1dmg = card1dmg * 2;
            }
            // Unique feature here

            if (card2Type.equalsIgnoreCase("WATER") && card1Type.equalsIgnoreCase("FIRE")){
                card2dmg = card2dmg * 2;
            }   else if(card2Type.equalsIgnoreCase("FIRE") && card1Type.equalsIgnoreCase("WATER")){
                card2dmg = card2dmg / 2;
            }   else if(card2Type.equalsIgnoreCase("NORMAL") && card1Type.equalsIgnoreCase("WATER")){
                card2dmg = card2dmg * 2;
            }   else if(card2Type.equalsIgnoreCase("WATER") && card1Type.equalsIgnoreCase("NORMAL")){
                card2dmg = card2dmg / 2;
            }   else if(card2Type.equalsIgnoreCase("FIRE") && card1Type.equalsIgnoreCase("NORMAL")){
                card2dmg = card2dmg * 2;
            }   else if (card2Type.equalsIgnoreCase("NORMAL") && card1Type.equalsIgnoreCase("FIRE")) {
                card2dmg = card2dmg / 2;
            }   else if (card2Type.equalsIgnoreCase("ICE") && card1Type.equalsIgnoreCase("WATER")) {
                card2dmg = card2dmg * 2;
            }   else if (card2Type.equalsIgnoreCase("WATER") && card1Type.equalsIgnoreCase("ICE")) {
                card2dmg = card2dmg / 2;
            }   else if (card2Type.equalsIgnoreCase("ICE") && card1Type.equalsIgnoreCase("FIRE")) {
                card2dmg = card2dmg / 2;
            }   else if (card2Type.equalsIgnoreCase("FIRE") && card1Type.equalsIgnoreCase("ICE")) {
                card2dmg = card2dmg * 2;
            }


             System.out.println(" => "+card1dmg + " VS " +card2dmg);
            if(card1dmg>card2dmg) {
                return 1;
            }
            if(card2dmg>card1dmg) {
                return 2;
            }
            if (card1dmg == card2dmg) {
                return 3;
            }


            return 0;
        }


}
