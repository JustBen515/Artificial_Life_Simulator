package artificiallifesimulatorGUI;

/**
 * subclass of Food, Deals with food of type red berry
 *
 * @author Ben Phillips
 * @since 05/11/16
 *
 */
public class RedBerry extends Food {

    RedBerry(int X, int Y, int ID, AWorld iWorld) {
        super("Redberries", "Strawberries.png", X, Y, ID, iWorld);
        nutrition = 5;
        time = 0;
    }
}
