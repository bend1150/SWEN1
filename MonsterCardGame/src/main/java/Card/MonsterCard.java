package Card;

import java.util.Random;

public class MonsterCard extends Card {

    private enum names{
        GOBLIN, DRAGON, ORK, WIZZARD, KNIGHT, ELF, KRAKEN
    }
    private enum type{
        WATER, FIRE, NORMAL, ICE
    }

    public MonsterCard(String name, String elementType,float damage, int id, String cardId) {        //EingabeParameter nur für test
        Random rn = new Random();

        this.name = name;

        this.elementType = elementType;

        this.damage = damage;

        this.id = id;

        this.cardId = cardId;

    }

    public MonsterCard generateRandomizedMonsterCard() {
        Random rn = new Random();

        int num = rn.nextInt(4);                    // Generate a number between 0-3
        this.elementType = type.values()[num].toString();

        num = rn.nextInt(7);                // Generate a number between 0-6
        this.name = this.elementType+names.values()[num].toString();

        num = rn.nextInt(50);                   // Generate a number between 0-49
        this.damage = num;

        return this;
    }



}
