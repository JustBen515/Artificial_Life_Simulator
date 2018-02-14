package artificiallifesimulatorGUI;

/**
 * subclass of Obstacle, Deals with obstacles of type Center, Entity will
 * have to wait here for a turn but will get energy
 *
 * @author Ben Phillips
 * @since 05/11/16
 *
 */
public class Center extends Obstacle {

    int healing = 5;

    Center(int X, int Y, int ID, AWorld iworld) {
        super("Center", "Center.png", X, Y, ID, iworld);
    }

    @Override
    public int getEnergy() {
        return healing;
    }

    @Override
    public void setEnergy(int inputEnergy) {
        healing = inputEnergy;
    }

}
