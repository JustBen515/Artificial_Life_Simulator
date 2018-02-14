package artificiallifesimulatorGUI;

/**
 * subclass of Food, Deals with food of type blueberry
 *
 * @author Ben Phillips
 * @since 05/11/16
 *
 */
public class BlueBerry extends Food {

    BlueBerry(int X, int Y, int ID, AWorld iWorld) {
        super("Blueberries", "Blueberries.png", X, Y, ID, iWorld);
        nutrition = 5;
        time = 0;
    }

}
