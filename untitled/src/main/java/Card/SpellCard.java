package Card;

import java.util.Random;

public class SpellCard extends Card{

    private enum type{
        WATER, FIRE, NORMAL, ICE
    }
    public SpellCard(String elementType,int damage,int cardID){             //EingabeParameter nur für test
        Random rn = new Random();
        int num = rn.nextInt(3);                    //Generate Number between 0-3
        //this.elementType = type.values()[num].toString();
        this.elementType = elementType;
        this.damage = damage;
        this.cardID = getCardID();


    }

    public SpellCard generateRandomizedSpellcard() {
        Random rn = new Random();

        int num = rn.nextInt(4);                // Generate a number between 0-3
        this.elementType = type.values()[num].toString();

        num = rn.nextInt(50);                   // Generate a number between 0-49
        this.damage = num;

        return this;
    }
}
