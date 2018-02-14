package artificiallifesimulatorGUI;

/**
 * subclass of Obstacle Deals with obstacles of type Trap will trap entities and
 * damage them
 *
 * @author Ben Phillips
 * @since 05/11/16
 *
 */
public class Trap extends Obstacle {

    int damage = -10;

    Trap(int X, int Y, int ID, AWorld iworld) {
        super("Trap", "Trap.png", X, Y, ID, iworld);
    }

    @Override
    public int getEnergy() {
        return damage;
    }

    @Override
    public void setEnergy(int inputEnergy) {
        damage = inputEnergy;
    }

}
