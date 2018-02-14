package artificiallifesimulatorGUI;

import javafx.scene.image.Image;

/**
 * subclass of LifeForm, Deals with life forms of fire type
 *
 * @author Ben Phillips
 * @since 05/11/16
 *
 */
public class FireType extends LifeForm {

    FireType(String name, int X, int Y, int ID, AWorld iworld) {
        super(name, name + ".png", X, Y, ID, iworld);
    }

    FireType() {
        super();
    }

    /**
     * Function to detect food
     *
     * @param d A direction from the Direction enum
     * @param Range The range that an entity can smell food
     * @return True if there is food, false if not
     */
    public boolean smellFood(AWorld.Direction d, int Range) {
        String[] type = {"Redberries", "Honey"};
        switch (d) {
            case North:
                for (int j = 1; j <= (Range / 4); j++) {                        //for half the range away
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
     * Function for a fire type to evolve
     */
    void Evolve() {
        if (energy >= 10) {                                                                  //if energy high enough for first evolution
            if (species.equals("Charmander")) {                                              //depending on the species
                species = "Charmeleon";                                                      //change species and image
                entIm = new Image(getClass().getResourceAsStream("Charmeleon.png"));
            }
        }
        if (energy >= 20) {                                                                  //if energy high enough for second evolution
            if (species.equals("Charmeleon")) {                                              //depending on the species
                species = "Charizard";                                                       //change species and image
                entIm = new Image(getClass().getResourceAsStream("Charizard.png"));
            }
        }
    }
}
