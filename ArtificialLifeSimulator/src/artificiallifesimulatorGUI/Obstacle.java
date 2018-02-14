package artificiallifesimulatorGUI;

/**
 * subclass of LifeForm, Deals with all obstacles in the world
 *
 * @author Ben Phillips
 * @since 05/11/16
 *
 */
abstract public class Obstacle extends AnEntity {

    Obstacle() {
        super();
    }

    Obstacle(String name, String image, int X, int Y, int ID, AWorld iworld) {
        super(name, image, X, Y, ID, iworld);
    }

}
