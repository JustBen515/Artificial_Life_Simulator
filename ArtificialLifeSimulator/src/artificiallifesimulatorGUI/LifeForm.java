package artificiallifesimulatorGUI;

/**
 * subclass of AnEntity, Deals with living entities/creatures
 *
 * @author Ben Phillips
 * @since 05/11/16
 *
 */
public class LifeForm extends AnEntity {

    int energy = 5;
    int wait = 0;       //if life form is in a space that they need to wait on

    LifeForm() {
        super();
    }

    LifeForm(String Species, String imName, int X, int Y, int ID, AWorld iworld) {
        super(Species, imName, X, Y, ID, iworld);
    }

    @Override
    public int getEnergy() {
        return energy;
    }

    public int getWait() {
        return wait;
    }

    @Override
    public void setEnergy(int inputEnergy) {
        energy = inputEnergy;
    }

    public void setWait(int input) {
        wait = input;
    }

}
