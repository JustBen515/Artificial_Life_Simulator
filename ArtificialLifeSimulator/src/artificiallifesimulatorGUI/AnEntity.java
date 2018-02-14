package artificiallifesimulatorGUI;

import javafx.scene.image.Image;

/**
 * superclass for all entities held within the world
 *
 * @author Ben Phillips
 * @since 05/11/16
 *
 */
abstract public class AnEntity {

    Image entIm;
    protected String species;//variables to hold the parameters of AnEntity
    protected int x, y, id;
    protected AWorld world;

    AnEntity() {
        species = "Default";
        x = 0;
        y = 0;
        id = 0;
        world = new AWorld();
    }

    AnEntity(String Species, String ImName, int X, int Y, int ID, AWorld iWorld) {
        species = Species;
        entIm = new Image(getClass().getResourceAsStream(ImName));
        x = X;
        y = Y;
        id = ID;
        world = iWorld;
    }

    //<editor-fold desc="Getters" defaultstate="collapsed">
    public String getSpecies() {
        return species;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getID() {
        return id;
    }

    abstract int getEnergy();
    //</editor-fold>
    //<editor-fold desc="Setters" defaultstate="collapsed">

    public void setSpecies(String inputSpecies) {
        species = inputSpecies;
    }

    public void setX(int inputX) {
        x = inputX;
    }

    public void setY(int inputY) {
        y = inputY;
    }

    public void setID(int inputID) {

        id = inputID;
    }

    abstract void setEnergy(int inputEnergy);
    //</editor-fold>   
    //<editor-fold desc="Other Functions" defaultstate="collapsed">

    void showEntity(GUInterface userI) {

        /**
         * Function to give the entities information so it can be drawn on the
         * GUI
         */
        userI.showEntity(entIm, x, y);
    }

    /**
     * Function to return, species and co-ords of an entity as a string
     *
     * @return The species name and Co-ordinates as a string
     */
    @Override
    public String toString() {
        String SpeciesPosition = "Species = " + getSpecies() + "\nX Co-Ord = " + getX() + "\nY Co-Ord = " + getY();
        return SpeciesPosition;
    }

    /**
     * Function to return all parameters of entity as a string
     *
     * @return All information about an entity as a string
     */
    public String toText() {
        String EntityInfo = "Species = " + getSpecies() + "\nX Co-Ord = " + getX() + "\nY Co-Ord = " + getY() + "\nID = " + getID();
        return EntityInfo;
    }

    //</editor-fold>
}
