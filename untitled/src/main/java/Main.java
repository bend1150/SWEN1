
import java.util.Random;
public class Main {
    private enum name{
        GOBLIN, DRAGON, ORC, WIZARD, KNIGHT, ELF, KRAKEN
    }
    public static void main(String[] args) {




        Random rn = new Random();
        for (int i=0;i<10;i++){
            int num = rn.nextInt(7);

            System.out.println(
                    name.values()[6].toString()
            );
        }

    }

}