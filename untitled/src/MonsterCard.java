
import java.util.Random;

public class MonsterCard extends Card {

    private enum names{
        GOBLIN, DRAGON, ORC, WIZARD, KNIGHT, ELF, KRAKEN
    }
    private enum type{
        WATER, FIRE, NORMAL
    }

    public MonsterCard() {
        Random rn = new Random();

        int num = rn.nextInt(7);                //Generate Number between 0-6

        this.name = names.values()[num].toString();

        num = rn.nextInt(3);                    //Generate Number between 0-2

        this.elementType = type.values()[num].toString();

        num = rn.nextInt(50);                  //Generate Number between 0-49
        this.damage = num;


    }


}
