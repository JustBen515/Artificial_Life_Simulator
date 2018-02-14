package artificiallifesimulatorGUI;

/**
 * subclass of Food, Deals with food of type blackberry
 *
 * @author Ben Phillips
 * @since 05/11/16
 *
 */
public class BlackBerry extends Food {

    BlackBerry(int X, int Y, int ID, AWorld iWorld) {
        super("Blackberries", "Blackberries.png", X, Y, ID, iWorld);
        nutrition = 5;
        time = 0;
    }
}
