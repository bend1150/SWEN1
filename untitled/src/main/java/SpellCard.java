import java.util.Random;

public class SpellCard extends Card{

    private enum type{
        WATER, FIRE, NORMAL
    }
    public SpellCard(){
        Random rn = new Random();
        int num = rn.nextInt(3);                    //Generate Number between 0-3
        this.elementType = type.values()[num].toString();


    }
}
