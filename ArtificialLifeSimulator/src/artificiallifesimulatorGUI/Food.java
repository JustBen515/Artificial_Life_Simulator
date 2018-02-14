package artificiallifesimulatorGUI;

/**
 * subclass of AnEntity, Deals with Entities of type food, what life forms eat
 *
 * @author Ben Phillips
 * @since 05/11/16
 *
 */
public class Food extends AnEntity {

    int nutrition;
    int time;

    Food() {
        super();
    }

    Food(String Type, String image, int X, int Y, int ID, AWorld iworld) {
        super(Type, image, X, Y, ID, iworld);
    }

    @Override
    public int getEnergy() {
        return nutrition;
    }

    public int getGrowTime() {
        return time;
    }

    @Override
    public void setEnergy(int input) {
        nutrition = input;
    }

    public void setGrowTime(int input) {
        time = input;
    }
}
