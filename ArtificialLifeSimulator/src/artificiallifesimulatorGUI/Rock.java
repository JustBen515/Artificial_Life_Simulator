package artificiallifesimulatorGUI;

/**
 * subclass of Obstacle, Deals with obstacles of type Rock, entities cant
 * pass through this space
 *
 * @author Ben Phillips
 * @since 05/11/16
 *
 */
public class Rock extends Obstacle {

    Rock(int X, int Y, int ID, AWorld iworld) {
        super("Rock", "Rock.png", X, Y, ID, iworld);
    }

    @Override
    public int getEnergy() {
        return 0;
    }

    @Override
    public void setEnergy(int inputEnergy) {
        /* Does nothing as rock doesnt have energy*/
    }
}
