import java.util.Random;


public class BattleLogic {


        static public int startFight(User user1, User user2){
            int victoryPlayer = 0;
            int victoryCard = 0;

            while(user1.getDeck().size()>0&&user2.getDeck().size()>0) {         //solange beide user was im deck haben, spielen wir
                int num1 = ((int) (Math.random() * user1.getDeck().size()));
                int num2 = ((int) (Math.random() * user2.getDeck().size()));
                Card card1 = user1.getDeck().get(num1);
                Card card2 = user2.getDeck().get(num2);

                if (card1 instanceof MonsterCard && card2 instanceof  MonsterCard){ // Pure monster battle
                    victoryCard = MonsterBattle(card1, card2);
                }
                if (victoryCard==1){
                    user2.getDeck().remove(card2);
                    user1.getDeck().add(card2);
                }

            }


            if(user1.getDeck().size()==0){
                victoryPlayer = 2;
            } else {
                victoryPlayer = 1;
            }
            return victoryPlayer;
        }

        static public int MonsterBattle(Card card1,Card card2){             //returniert gewonnene Karte
            if((card1.getName()=="GOBLIN"||card2.getName()=="GOLBIN")&&(card1.getName()=="DRAGON"||card2.getName()=="DRAGON")){
                if(card1.getName()=="GOBLIN"){
                    return 2;
                }
                return 1;
            }


            return 1;//platzhalter
        }

}
