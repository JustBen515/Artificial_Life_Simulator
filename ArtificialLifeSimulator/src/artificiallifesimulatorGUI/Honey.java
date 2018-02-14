package artificiallifesimulatorGUI;

/**
 * subclass of Food, Deals with food of type Honey, Food that will not grow back
 * over time
 *
 * @author Ben Phillips
 * @since 05/11/16
 *
 */
public class Honey extends Food {

    Honey(int X, int Y, int ID, AWorld iWorld) {
        super("Honey", "Honey.png", X, Y, ID, iWorld);
        nutrition = 5;
        time = 0;
    }

}
