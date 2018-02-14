package artificiallifesimulatorGUI;

/**
 * subclass of Food, Deals with food of type Trash, Food that will injure
 * entities if eaten
 *
 * @author Ben Phillips
 * @since 05/11/16
 *
 */
public class Trash extends Food {

    Trash(int X, int Y, int ID, AWorld iWorld) {
        super("Trash", "Trash.png", X, Y, ID, iWorld);
        nutrition = -10;
        time = 0;
    }
}
