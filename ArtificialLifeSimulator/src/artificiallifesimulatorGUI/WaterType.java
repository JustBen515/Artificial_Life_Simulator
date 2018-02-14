package artificiallifesimulatorGUI;

import javafx.scene.image.Image;

/**
 * subclass of LifeForm, Deals with life forms of water type
 *
 * @author Ben Phillips
 * @since 05/11/16
 *
 */
public class WaterType extends LifeForm {

    WaterType(String name, int X, int Y, int ID, AWorld iworld) {
        super(name, name + ".png", X, Y, ID, iworld);
    }

    WaterType() {
        super();
    }

    /**
     * Function to detect food
     *
     * @param d A direction from the Direction enum
     * @param Range The range that an entity can smell food
     * @return True if there is food, false if not
     */
    public boolean smellFood(AWorld.Direction d, int Range) {//Function to detect food

        String[] type = {"Blueberries", "Honey"};
        switch (d) {
            case North:
                for (int j = 1; j <= (Range / 4); j++) {                        //for quarter of the range away
                    if (world.checkObstacle(x, (y - j)) == 1) {                 //if there is an obstacle
                        return false;                                           //dont go this way because entity will end up getting stuck
                    }
                }
                for (int k = 0; k < type.length; k++) {                         //for each type of food
                    for (int i = 1; i <= Range; i++) {                          //for the spaces up to range distance away
                        if (world.checkFood(x, (y - i), type[k]) == 1) {        //if there is food in any of them
                            return true;
                        }
                    }
                }
                return false;
            case East:                                                          //same but for east
                for (int j = 1; j <= (Range / 4); j++) {
                    if (world.checkObstacle((x + j), y) == 1) {
                        return false;
                    }
                }
                for (int k = 0; k < type.length; k++) {
                    for (int i = 1; i <= Range; i++) {
                        if (world.checkFood((x + i), y, type[k]) == 1) {
                            return true;
                        }
                    }
                }
                return false;
            case South:                                                         //same but for south
                for (int j = 1; j <= (Range / 4); j++) {
                    if (world.checkObstacle(x, (y + j)) == 1) {
                        return false;
                    }
                }
                for (int k = 0; k < type.length; k++) {
                    for (int i = 1; i <= Range; i++) {
                        if (world.checkFood(x, (y + i), type[k]) == 1) {
                            return true;
                        }
                    }
                }
                return false;
            case West:                                                          //same but for west
                for (int j = 1; j <= (Range / 4); j++) {
                    if (world.checkObstacle((x - j), y) == 1) {
                        return false;
                    }
                }
                for (int k = 0; k < type.length; k++) {
                    for (int i = 1; i <= Range; i++) {
                        if (world.checkFood((x - i), y, type[k]) == 1) {
                            return true;
                        }
                    }
                }
                return false;
        }
        return false;
    }

    /**
     * Function to deal with water types evolving
     */
    void Evolve() {

        if (energy >= 10) {                                                                  //if energy is high enough to evolve
            if (species.equals("Squirtle")) {                                                //depending on the species
                species = "Wartortle";                                                       //sets the species and the image 
                entIm = new Image(getClass().getResourceAsStream("Wartortle.png"));
            }
        }
        if (energy >= 20) {                                                                  //if energy is high enough for second evolution
            if (species.equals("Wartortle")) {                                               //depending on the species
                species = "Blastoise";                                                      //sets the species and image
                entIm = new Image(getClass().getResourceAsStream("Blastoise.png"));
            }
        }
    }
}
