package Card;

import java.util.Random;

public class SpellCard extends Card{

    private enum type{
        WATER, FIRE, NORMAL, ICE
    }
    public SpellCard(String name,String elementType,float damage,int id, String cardId){             //EingabeParameter nur für test
        Random rn = new Random();
        int num = rn.nextInt(3);                    //Generate Number between 0-3
        //this.elementType = type.values()[num].toString();
        this.name = name;
        this.elementType = elementType;
        this.damage = damage;
        this.id = id;
        this.cardId = cardId;


    }

    public SpellCard generateRandomizedSpellcard() {
        Random rn = new Random();

        int num = rn.nextInt(4);                // Generate a number between 0-3
        this.elementType = type.values()[num].toString();

        this.name = this.elementType+"SPELL";

        num = rn.nextInt(50);                   // Generate a number between 0-49
        this.damage = num;



        return this;
    }
}
