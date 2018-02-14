package artificiallifesimulatorGUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

/**
 * Creates a world for all entities to exist in
 *
 * @author Ben Phillips
 * @since 05/11/16
 *
 */
public class AWorld {

    private int xSize;//variables used throughout AWorld
    private int ySize;
    private int maxEntities;
    private int numEntities = 0;
    private final Random myRandom = new Random();
    ArrayList<AnEntity> EntityArr;//Holds all the entities

    // <editor-fold desc="Constructors" defaultstate="collapsed">
    AWorld() {
        xSize = 10;
        ySize = 10;
        EntityArr = new ArrayList<>(maxEntities);
    }

    AWorld(int XSize, int YSize, int MaxEntities) {
        xSize = XSize;
        ySize = YSize;
        maxEntities = MaxEntities;
        EntityArr = new ArrayList<>(maxEntities);
    }

    // </editor-fold>
    //<editor-fold desc="Getters" defaultstate="collapsed">
    public int getX() {
        return xSize;
    }

    public int getY() {
        return ySize;
    }

    public int getMaxEntities() {
        return maxEntities;
    }

    public int getNumEntities() {
        return numEntities;
    }
    //</editor-fold>
    //<editor-fold desc="Setters" defaultstate="collapsed">

    public void setX(int XSize) {
        xSize = XSize;
    }

    public void setY(int YSize) {
        ySize = YSize;
    }

    public void setMaxEntities(int MaxEntities) {
        maxEntities = MaxEntities;
    }

    //</editor-fold>
    //<editor-fold desc="Other Functions" defaultstate="collapsed">
    /**
     * Function for adding an entity to the world
     */
    public void addEntity() {
        int x;//initialises the x,y variables
        int y;
        boolean temp = true;//variable for for loop
        if (numEntities < maxEntities) {
            Optional<String> result = TextInputBox("Species ID", "Add Entity", null, "Enter The Species, Image and ID").showAndWait();      //allows user to enter info about the entity to be added
            if (result.isPresent()) {
                String[] tempS = result.get().split(" ");                                           //splits the input string into seperate parts
                String species = tempS[0];
                int id = Integer.parseInt(tempS[1]);
                x = myRandom.nextInt(xSize);                                                        //gives x and y a random value
                y = myRandom.nextInt(ySize);
                while (temp == true) {
                    for (int i = 0; i < EntityArr.size(); i++) {                                    //for all entities
                        if (EntityArr.get(i).getX() == x && EntityArr.get(i).getY() == y) {         //if entity all ready at the position
                            x = myRandom.nextInt(xSize);                                            //get new x,y values
                            y = myRandom.nextInt(ySize);
                            i = 0;                                                                  //check from beginning again
                        }
                    }
                    temp = false;                                                                   //if there is no entity at space end while
                }

                /* Deals with adding the correct type of life form*/
                switch (species) {
                    case "Squirtle":
                        EntityArr.add(new WaterType(species, x, y, id, this));
                        numEntities++;
                        break;
                    case "Charmander":
                        EntityArr.add(new FireType(species, x, y, id, this));
                        numEntities++;
                        break;
                    case "Bulbasaur":
                        EntityArr.add(new GrassType(species, x, y, id, this));
                        numEntities++;
                        break;
                }

                numEntities++;                                                                      //keep track of how many entities there is   
            }
        } else {
            AlertBox("Add Entity", "Max number of entities reached");                               //if max entities is reached, alert box tells user this
        }
    }

    /**
     * Function to get each entity
     *
     * @param userI The GUI
     */
    public void showEntities(GUInterface userI) {
        AnEntity ent;
        Food temp;
        for (int i = 0; i < numEntities; i++) {
            ent = EntityArr.get(i);
            if (ent instanceof Food) {                         //if the entity is food
                temp = (Food) ent;                             //cast temp ent to food so methods can be called
                if (temp.getGrowTime() == 0) {                 //if the food has not been eaten 
                    EntityArr.get(i).showEntity(userI);        //carry on to draw it in the world
                }
            } else {
                EntityArr.get(i).showEntity(userI);            //if not food just draw the entity
            }
        }
    }

    /*
     * Function no longer used, could be useful again in the future
     */
//  public void listEntities() {
//   
//      /*
//        * Function to list all the entities and info about them
//        */
//        for (int i = 0; i < numEntities; i++) {
//            System.out.println(EntityArr.get(i).toText());      //calls and prints the toText function
//            System.out.println(EntityArr.get(i).world);         //prints the world the entity lives in
//        }
//    }
    /**
     * Function to initialise the world for a string input by the user
     *
     * @param config The configuration string
     */
    public void fromText(String config) {
        boolean tempW = true;
        numEntities = 0;
        do {
            String[] temp = config.split(" ");                                  //splits the string by spaces
            int[] numSpecies = new int[(temp.length - 4) / 2];                  //array to hold the number of each species
            String[] typeSpecies = new String[(temp.length - 4) / 2];           //array to hold the different types of species
            int speciesCount = 4;                                               //variables for the for loop so type and num array have values placed in the correct spaces
            int numCount = 5;
            int x = Integer.parseInt(temp[0]);                                  //converts this place in the string array to an int
            int y = Integer.parseInt(temp[1]);                                  //converts to int		
            float foodper = Integer.parseInt(temp[2]);                          //converts to int
            float obstaclesper = Integer.parseInt(temp[3]);                     //converts to int
            for (int i = 4; i < temp.length; i++) {                             //for the rest of the array of strings
                if (i % 2 == 0) {                                               //if i is even
                    typeSpecies[i - speciesCount] = temp[i];                    //turn the next space in the type array to the next thing in the temp array
                    speciesCount++;
                } else {                                                        //if its odd
                    numSpecies[i - numCount] = Integer.parseInt(temp[i]);       //convert temp to an int and add it to the number array
                    numCount++;
                }
            }
            xSize = x;                                                          //sets x,y to the user inputs
            ySize = y;
            int food = Math.round((x * y) * (foodper / 100));                   //calculates how many food entities are needed 
            int obstacles = (Math.round((x * y) * (obstaclesper / 100)));       //calculates how many obstacle entities are needed
            maxEntities = ((x * y) / 4);
            EntityArr = new ArrayList<AnEntity>(maxEntities);
            int maxCount = food + obstacles;
            for (int i = 0; i < numSpecies.length; i++) {
                maxCount += numSpecies[i];
            }
            if (maxCount > maxEntities) {                                       //if number of entities to be added is greater than max then inform the user of this
                AlertBox("Configuration", "You have Exceeded The Max Number Of Entities ((x*y)/4).\nPlease Try Again.");
                tempW = false;
            } else {
                this.addToArr("Redberries", food / 5);                          //calls AddToArr for food
                this.addToArr("Blueberries", food / 5);
                this.addToArr("Blackberries", food / 5);
                this.addToArr("Trash", food / 5);
                this.addToArr("Honey", food / 5);
                this.addToArr("Rock", ((obstacles - 1) / 2));                     //calls AddToArr for obstacles
                this.addToArr("Center", 1);
                this.addToArr("Trap", ((obstacles - 1) / 2));
                for (int i = 0; i < ((temp.length - 4) / 2); i++) {             //for however many other species the user specified
                    this.addToArr(typeSpecies[i], numSpecies[i]);               //call AddToArr for each one
                    tempW = false;
                }
            }
        } while (tempW == true);
    }

    /**
     * Function for adding a certain number of entities to the array
     *
     * @param name Name (species) of the entity
     * @param numOf How many of this entity are to be added
     */
    public void addToArr(String name, int numOf) {
        boolean temp;
        for (int i = 0; i < numOf; i++) {                                                   //for amount of species needed
            temp = true;
            int x = myRandom.nextInt(xSize);                                                //get random x,y values
            int y = myRandom.nextInt(ySize);
            while (temp == true) {
                for (int j = 0; j < EntityArr.size(); j++) {//for each entity
                    if (EntityArr.get(j).getX() == x && EntityArr.get(j).getY() == y) {     //if entity already at x,y
                        x = myRandom.nextInt(xSize);                                        //change x,y
                        y = myRandom.nextInt(ySize);
                        j = 0;                                                              //check all entities again
                    }
                }
                temp = false;                                                               //if no entity at x,y stop while
            }

            /*
             * Deals with adding the correct type of entitiy
             */
            switch (name) {
                case "Blueberries":
                    EntityArr.add(new BlueBerry(x, y, i, this));
                    numEntities++;
                    break;
                case "Redberries":
                    EntityArr.add(new RedBerry(x, y, i, this));
                    numEntities++;
                    break;
                case "Blackberries":
                    EntityArr.add(new BlackBerry(x, y, i, this));
                    numEntities++;
                    break;
                case "Trash":
                    EntityArr.add(new Trash(x, y, i, this));
                    numEntities++;
                    break;
                case "Honey":
                    EntityArr.add(new Honey(x, y, i, this));
                    numEntities++;
                    break;
                case "Squirtle":
                    EntityArr.add(new WaterType(name, x, y, i, this));
                    numEntities++;
                    break;
                case "Charmander":
                    EntityArr.add(new FireType(name, x, y, i, this));
                    numEntities++;
                    break;
                case "Bulbasaur":
                    EntityArr.add(new GrassType(name, x, y, i, this));
                    numEntities++;
                    break;
                case "Rock":
                    EntityArr.add(new Rock(x, y, i, this));
                    numEntities++;
                    break;
                case "Center":
                    EntityArr.add(new Center(x, y, i, this));
                    numEntities++;
                    break;
                case "Trap":
                    EntityArr.add(new Trap(x, y, i, this));
                    numEntities++;
                    break;
            }
        }
    }

    /**
     * Enumeration for direction
     */
    public enum Direction {
        North,
        East,
        South,
        West;

        /**
         * Function to get random direction
         *
         * @return a random direction
         */
        public static Direction GetRandomDirection() {
            Random number = new Random();                                       //sets up random
            return values()[number.nextInt(values().length)];                   //returns a random value
        }
    }

    /**
     * Function for checking if there is food at a given x, y position
     *
     * @param x The X Co-Ordinate of space
     * @param y The Y Co-ordinate of space
     * @param type The type of food being checked for
     * @return 1 if there is food there, 0 if not
     */
    public int checkFood(int x, int y, String type) {
        AnEntity temp;
        if ((x < 0) || (y < 0) || (x > xSize - 1) || (y > ySize - 1)) {                 //if the co-ord is out of bounds
            return 0;                                                                   //no food
        }
        for (int i = 0; i < EntityArr.size(); i++) {
            temp = EntityArr.get(i);
            if ((EntityArr.get(i).getX() == x) && (EntityArr.get(i).getY() == y)) {     //if there is an entity at that position
                if (EntityArr.get(i).getSpecies().equals(type)) {                       //if the entity is the correct food
                    if (((Food) temp).getGrowTime() == 0) {                             //if the food isn't in the growth period
                        return 1;                                                       //there is food
                    } else {
                        return 0;                                                       //if food hasn't grown back, ignore it
                    }
                } else {
                    return 0;                                                           //there is no food there
                }
            }
        }
        return 0;                                                                       //if no entity at position then there is no food
    }

    /**
     * function for checking if there is an obstacle at a given x,y position
     *
     * @param x The X Co-ordinate of the space
     * @param y The Y Co-ordinate of the space
     * @return 1 if obstacle is there, 0 if not
     */
    public int checkObstacle(int x, int y) {
        AnEntity temp;
        if ((x < 0) || (y < 0) || (x > xSize - 1) || (y > ySize - 1)) {                 //if the co-ord is out of bounds
            return 0;                                                                   //no obstacle
        }
        for (int i = 0; i < EntityArr.size(); i++) {                                    //for each entity
            temp = EntityArr.get(i);
            if ((temp.getX() == x) && (temp.getY() == y)) {                             //if there is an entity at that position
                if (temp instanceof Obstacle) {                                         //if the entity is an Obstacle
                    if (temp instanceof Rock) {                                         //if entity is a rock, obstacle wont be able to move past it
                        return 1;                                                       //there is Obstacle, entity cant move past
                    }
                }
            }
        }
        return 0;                                                                       //if no entity at position then the is either no obstacle or a trap or center
    }

    /**
     * Function for checking if an entity can move to a given x,y position
     *
     * @param x The X Co-ordinate of space
     * @param y The Y Co-ordinate of the space
     * @param Ent The entity that is checking if it can move
     * @return 1 if it can move and food removed for array, 2 if moves but
     * nothing removed, 0 if cant move
     */
    public int checkMove(int x, int y, AnEntity Ent) {
        if ((x < 0) || (y < 0) || (x > xSize - 1) || (y > ySize - 1)) {                         //if x,y out of bounds
            return 0;
        }
        for (int i = 0; i < numEntities; i++) {
            if (EntityArr.get(i).getX() == x && EntityArr.get(i).getY() == y) {                 //if there is an entity at x,y
                AnEntity temp = EntityArr.get(i);
                if (Ent instanceof FireType) {                                                  //if the entity thas checking its move is a FireType
                    if (temp instanceof RedBerry) {                                             //if the entity at the space being checked is Redberry
                        Ent.setEnergy((Ent.getEnergy() + temp.getEnergy()));                    //sets the energy of the entity
                        ((RedBerry) temp).setGrowTime(5);                                       //removes the food from world and sets growth time
                        return 2;                                                               //entity can move
                    } else if (temp instanceof BlueBerry || temp instanceof BlackBerry) {       //if it is a different kind of food
                        return 2;                                                               //can move but cant eat it
                    } else if (temp instanceof Trash || temp instanceof Honey) {                //if trash or honey at space
                        EntityArr.remove(i);                                                    //remove it
                        Ent.setEnergy((Ent.getEnergy() + temp.getEnergy()));                     //set energy
                        numEntities--;
                        return 1;                                                               //can move, has eaten trash
                    } else if (temp instanceof Center) {
                        Ent.setEnergy((Ent.getEnergy() + temp.getEnergy()));                    //add health to entitiy
                        ((FireType) Ent).setWait(1);                                             //set the wait time
                    } else if (temp instanceof Trap) {
                        Ent.setEnergy((Ent.getEnergy() + temp.getEnergy()));                    //do damage to entity
                        ((FireType) Ent).setWait(5);                                             //set wait time
                    } else {                                                                    //if it is an entity or rock
                        return 0;
                    }
                } else if (Ent instanceof WaterType) {                                          //if the entity is a WaterType
                    if (temp instanceof BlueBerry) {                                            //if entity at space is food that watertype eats
                        Ent.setEnergy(Ent.getEnergy() + temp.getEnergy());                      //sets the energy of the entity
                        ((BlueBerry) temp).setGrowTime(5);                                      //removes the food from world and sets growth time
                        return 2;                                                               //entity can move
                    } else if (temp instanceof RedBerry || temp instanceof BlackBerry) {        //if it is a different kind of food
                        return 2;                                                               //can move but cant eat it
                    } else if (temp instanceof Trash || temp instanceof Honey) {                //if trash or honey at space
                        EntityArr.remove(i);                                                    //remove it
                        Ent.setEnergy(Ent.getEnergy() + temp.getEnergy());                      //set energy
                        numEntities--;
                        return 1;                                                               //can move, has eaten trash
                    } else if (temp instanceof Center) {
                        Ent.setEnergy((Ent.getEnergy() + temp.getEnergy()));                    //add health to entitiy
                        ((WaterType) Ent).setWait(1);                                             //set the wait time
                    } else if (temp instanceof Trap) {
                        Ent.setEnergy((Ent.getEnergy() + temp.getEnergy()));                    //do damage to entity
                        ((WaterType) Ent).setWait(5);                                            //set wait time
                    } else {
                        return 0;                                                               //if entity or obstacle
                    }
                } else {                                                                        //if entity is a GrassType
                    if (temp instanceof BlackBerry) {
                        Ent.setEnergy(Ent.getEnergy() + temp.getEnergy());                      //sets the energy of the entity
                        ((BlackBerry) temp).setGrowTime(5);                                     //removes the food from world and sets growth time
                        return 2;                                                               //can move
                    } else if (temp instanceof RedBerry || temp instanceof BlueBerry) {         //if it is a different type of food
                        return 2;
                    } else if (temp instanceof Trash || temp instanceof Honey) {                //if trash at space
                        EntityArr.remove(i);                                                    //remove it
                        Ent.setEnergy(Ent.getEnergy() + temp.getEnergy());                      //set energy
                        numEntities--;
                        return 1;                                                               //can move, has eaten trash
                    } else if (temp instanceof Center) {
                        Ent.setEnergy((Ent.getEnergy() + temp.getEnergy()));                    //add health to entitiy
                        ((GrassType) Ent).setWait(1);                                            //set the wait time
                    } else if (temp instanceof Trap) {
                        Ent.setEnergy((Ent.getEnergy() + temp.getEnergy()));                    //do damage to entity
                        ((GrassType) Ent).setWait(5);                                            //set wait time
                    } else {                                                                    //if it is an entity or obstacle
                        return 0;
                    }
                }
            }
        }
        return 2;                                                                               //if no entity at x,y entity can move
    }

    /**
     * Function for moving entities
     */
    public void movement() {
        int range;
        char Ent;
        FireType tempF = new FireType();
        WaterType tempW = new WaterType();
        GrassType tempG = new GrassType();
        if (xSize < ySize) {                                                                    //if the x size is smaller than the left, scale range accordingly 
            range = (xSize / 4);                                                                //set range to x/4
        } else {
            range = (ySize / 4);                                                                //otherwise set range to y/4
        }
        for (int i = 0; i < EntityArr.size(); i++) {                                            //for each entity
            int temp = 6;                                                                       //set temp to 6
            AnEntity tempEnt = EntityArr.get(i);
            if ((!(tempEnt instanceof Food)) && (!(tempEnt instanceof Obstacle))) {             //if entity is a life form
                if (((LifeForm) tempEnt).getWait() == 0) {
                    if (tempEnt instanceof FireType) {                                              //work out what kind of life form the entity is, make a note of this
                        tempF = (FireType) tempEnt;                                                 //cast the entity to the right type
                        Ent = 'F';                                                                  //make a note of which type it is
                    } else if (tempEnt instanceof WaterType) {
                        tempW = (WaterType) tempEnt;
                        Ent = 'W';
                    } else {
                        tempG = (GrassType) tempEnt;
                        Ent = 'G';
                    }
                    List<Direction> dire = new ArrayList<>();                                       //create a list of directions
                    Collections.addAll(dire, Direction.values());                                   //add each direction to the list
                    Collections.shuffle(dire);                                                      //shuffle the list

                    /* 
                 * Depending on the type of entitiy call the smell food function
                 * Each switch does the same thing 
                 * but calls the smell food function for the respective life form type
                     */
                    switch (Ent) {
                        case 'F':
                            for (int j = 0; j < 4; j++) {                                           //for each direction
                                if (tempF.smellFood(dire.get(j), range) == true) {                  //if there is food in that direction, withtin the range                         
                                    temp = j;                                                       //set temp to the space in the list of the direction that food is in
                                    break;                                                          //stop looking for food
                                }
                            }
                            break;
                        case 'W':
                            for (int j = 0; j < 4; j++) {
                                if (tempW.smellFood(dire.get(j), range) == true) {
                                    temp = j;
                                    break;
                                }
                            }
                            break;
                        case 'G':
                            for (int j = 0; j < 4; j++) {
                                if (tempG.smellFood(dire.get(j), range) == true) {
                                    temp = j;
                                    break;
                                }
                            }
                            break;
                    }
                    if (temp == 6) {                                                                                                        //if no food was found in any direction
                        temp = 0;                                                                                                           //use the first direction in the list
                    }
                    switch (dire.get(temp)) {                                                                                               //for the direction selected (towards food if possible)
                        case North:
                            if (this.checkMove(EntityArr.get(i).getX(), (EntityArr.get(i).getY() - 1), EntityArr.get(i)) == 1) {            //if the entity is moving on to food
                                EntityArr.get(i - 1).setY((EntityArr.get(i - 1).getY() - 1));
                                /*
                             * set the y to y-1 (y-1 because printing the display y increases as you go down, so moving up y decreases
                             * EntityArr.get(i-1) because when the food is removed from the array in the CheckMove function the entity then moves back one space in the array
                                 */
                            } else if (this.checkMove(EntityArr.get(i).getX(), (EntityArr.get(i).getY() - 1), EntityArr.get(i)) == 2) {     //if the entity can move but the space is blank
                                EntityArr.get(i).setY((EntityArr.get(i).getY() - 1));                                                       //change the y to y-1
                                //EntityArr.get(i) because no food will be removed so the array stays the same
                            }
                            break;
                        case East:                                                                                                          //Essentially the same as north but x+1 not y-1
                            if (this.checkMove((EntityArr.get(i).getX() + 1), EntityArr.get(i).getY(), EntityArr.get(i)) == 1) {            //if moves to food
                                EntityArr.get(i - 1).setX((EntityArr.get(i - 1).getX() + 1));                                               //change the entities x co-ords
                            } else if (this.checkMove((EntityArr.get(i).getX() + 1), EntityArr.get(i).getY(), EntityArr.get(i)) == 2) {     //if empty space
                                EntityArr.get(i).setX((EntityArr.get(i).getX() + 1));                                                       //change entities x co-ord
                            }
                            break;
                        case South:                                                                                                         //same as others but y+1
                            if (this.checkMove(EntityArr.get(i).getX(), (EntityArr.get(i).getY() + 1), EntityArr.get(i)) == 1) {            //if moves to food
                                EntityArr.get(i - 1).setY((EntityArr.get(i - 1).getY() + 1));                                               //change the entities y co-ord
                            } else if (this.checkMove(EntityArr.get(i).getX(), (EntityArr.get(i).getY() + 1), EntityArr.get(i)) == 2) {     //if empty space
                                EntityArr.get(i).setY((EntityArr.get(i).getY() + 1));                                                       //change the y co-ord
                            }
                            break;
                        case West:
                            if (this.checkMove((EntityArr.get(i).getX() - 1), EntityArr.get(i).getY(), EntityArr.get(i)) == 1) {            //if moving to food
                                EntityArr.get(i - 1).setX((EntityArr.get(i - 1).getX() - 1));                                               //change the x co-ord 
                            } else if (this.checkMove((EntityArr.get(i).getX() - 1), EntityArr.get(i).getY(), EntityArr.get(i)) == 2) {     //if empty space
                                EntityArr.get(i).setX((EntityArr.get(i).getX() - 1));                                                       //Change x co-ord
                            }
                            break;
                    }
                } else {
                    ((LifeForm) tempEnt).setWait(((LifeForm) tempEnt).getWait() - 1);
                }
            }
        }
    }

    /**
     * Function for getting statistics for entities
     *
     * @return The statistics of entities as a string
     */
    public String stats() {
        String statOut = "";
        for (int i = 0; i < EntityArr.size(); i++) {
            AnEntity tempEnt = EntityArr.get(i);
            if ((!(tempEnt instanceof Food)) && (!(tempEnt instanceof Obstacle))) {                                                                                     //if it is a life form
                statOut += ("Species: " + EntityArr.get(i).getSpecies() + " " + EntityArr.get(i).getID() + " Energy: " + EntityArr.get(i).getEnergy() + "\n");          //add its species,id and energy to the string
            }
        }
        return statOut;
    }

    /**
     * Function for getting the world info
     *
     * @return The statistics of the world as a string
     */
    public String worldInfo() {
        String stats = "X size: " + xSize + "\nY size: " + ySize + "\n";                                    //adds the x and y size to the string
        int foodCount = 0;
        int obstacleCount = 0;
        for (int i = 0; i < EntityArr.size(); i++) {
            AnEntity tempEnt = EntityArr.get(i);
            if (tempEnt instanceof Food) {                                                                  //if it is food
                foodCount++;                                                                                //increment the food counter
            } else if ((tempEnt instanceof Obstacle)) {                                                     //if obstacle
                obstacleCount++;                                                                            //increment obstacle counter
            }
        }
        stats += "Food Remaining: " + foodCount;                                                            //add food count
        stats += "\nObstacles: " + obstacleCount;                                                           //add obstacle count
        return stats;
    }

    /**
     * Function for finding an entity by name and id
     */
    AnEntity findEntity(String name, int id) {
        for (int i = 0; i < numEntities; i++) {
            AnEntity tempEnt = EntityArr.get(i);
            if ((!(tempEnt instanceof Food)) && (!(tempEnt instanceof Obstacle))) {                         //if its an entity
                if (EntityArr.get(i).getSpecies().equals(name) && EntityArr.get(i).getID() == id) {         //if it is the correct entity
                    return EntityArr.get(i);
                }
            }
        }
        return null;
    }

    /**
     * Function to remove an entity
     */
    public void removeEntity() {
        String name;
        int id;
        Optional<String> result = TextInputBox("EntityName 0", "Remove Entity", null, "Enter The Name And ID Of The Entitiy").showAndWait();                        //allows user to choose entity to remove
        if (result.isPresent()) {
            String[] tempS = result.get().split(" ");
            name = tempS[0];
            id = Integer.parseInt(tempS[1]);
            if (!(findEntity(name, id) == null)) {                                                                                                                  //find the entity
                AnEntity temp = findEntity(name, id);
                Optional<String> result2 = TextInputBox("", "Remove Entity", temp.toText(), "Are You Sure You Want To Delete This Entity? Y/N").showAndWait();      //allow user to confirm this is the entity they want to delete
                if (result2.isPresent()) {
                    switch (result2.get()) {
                        case "Y":                                                                                                                                   //if yes, remove entity, alert user that it has been done
                        case "y":
                            EntityArr.remove(temp);
                            numEntities--;
                            AlertBox("Remove Entity", "Entity Removed.");
                            break;
                        case "N":
                        case "n":
                            AlertBox("Remove Entity", "Entitiy Not Removed.");                                                                                      //if no, confirm entity wasnt removed
                            break;
                        default:
                            AlertBox("Remove Entity", "Invalid Option");                                                                                            //if gibberish entered...
                            break;
                    }
                }
            } else {
                AlertBox("Remove Entity", "Invalid Option, Try Again.");                                                                                            //if gibberish entered...
            }
        }
    }

    /**
     * Function to modify a certain entity
     */
    public void modifyEntity() {
        String name;
        int id;
        Optional<String> result = TextInputBox("EntityName 0", "Modify Entity", null, "Enter The Name And ID Of The Entitiy").showAndWait();                                //allows user to choose entity to be modified
        if (result.isPresent()) {
            String[] tempS = result.get().split(" ");
            name = tempS[0];
            id = Integer.parseInt(tempS[1]);
            if (!(findEntity(name, id) == null)) {                                                                                                                          //finds the entity
                AnEntity temp = findEntity(name, id);
                Optional<String> result2 = TextInputBox("", "Modify Entity", temp.toText(), "What would you like to modify? (X)pos, (Y)pos or (I)d").showAndWait();         //asks user what they would like to modify
                if (result2.isPresent()) {
                    switch (result2.get()) {
                        case "X":
                        case "x":
                            Optional<String> resultX = TextInputBox("", "Modify Entity", null, "Enter The New X Value").showAndWait();                                      //alllow them to enter a new x position
                            if (resultX.isPresent()) {
                                int tempX = Integer.parseInt(resultX.get());
                                if (0 < tempX || tempX > xSize - 1) {                                                                                                       //if it is within the boundries
                                    temp.setX(tempX);                                                                                                                       //set x
                                } else {
                                    AlertBox("Modify Entity", "Invalid Space. Not Modified");                                                                               //if invalid, notify the user
                                }
                            }
                            break;
                        case "Y":
                        case "y":
                            Optional<String> resultY = TextInputBox("", "Modify Entity", null, "Enter The New Y Value").showAndWait();                                      //allow user to enter a new y position
                            if (resultY.isPresent()) {
                                int tempY = Integer.parseInt(resultY.get());
                                if (0 < tempY || tempY > ySize - 1) {                                                                                                       //if in boudries
                                    temp.setY(tempY);                                                                                                                       //set y
                                } else {
                                    AlertBox("Modify Entity", "Invalid Space. Not Modified");                                                                               //if invalid, notify the user
                                }
                            }
                            break;
                        case "I":
                        case "i":
                            Optional<String> resultID = TextInputBox("", "Modify Entity", null, "Enter The New ID Value").showAndWait();                                    //allow user to enter new id
                            if (resultID.isPresent()) {
                                int tempID = Integer.parseInt(resultID.get());
                                temp.setID(tempID);                                                                                                                         //set the id
                            }
                            break;
                    }
                }
            } else {
                AlertBox("Modify Entity", "Invalid Choice, Try Again.");                                                                                                    ///if gibberish entered...
            }

        }

    }

    /**
     * Function for creating an alert box
     */
    void AlertBox(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }

    /**
     * Function for creating a textinputdialog
     */
    TextInputDialog TextInputBox(String option, String title, String header, String content) {
        TextInputDialog dialog = new TextInputDialog(option);
        dialog.setTitle(title);
        dialog.setContentText(content);
        dialog.setHeaderText(header);

        return dialog;
    }

    /**
     * Function for decreasing entities energy after moving
     */
    void decreaseEnergy() {
        for (int i = 0; i < numEntities; i++) {
            AnEntity tempEnt = EntityArr.get(i);
            if (!(tempEnt instanceof Food) && !(tempEnt instanceof Obstacle)) {     //for every life form
                EntityArr.get(i).setEnergy(EntityArr.get(i).getEnergy() - 1);       //decrease energy by 1
                if (EntityArr.get(i).getEnergy() <= 0) {
                    EntityArr.remove(i);                                            //remove the entity (it has died)
                    numEntities--;
                }
            }
        }
    }

    /**
     * function for checking if an entity can evolve
     */
    public void EvolutionCheck() {
        for (int i = 0; i < EntityArr.size(); i++) {
            AnEntity tempEnt = EntityArr.get(i);
            if ((!(tempEnt instanceof Food)) && (!(tempEnt instanceof Obstacle))) {         //if entity is a life form
                if (tempEnt instanceof FireType) {
                    FireType tempF = (FireType) tempEnt;                                    //cast entity to correct type
                    tempF.Evolve();                                                         //call function for that entity to evolve
                } else if (tempEnt instanceof WaterType) {
                    WaterType tempW = (WaterType) tempEnt;                                  //cast entity to correct type
                    tempW.Evolve();                                                         //call function for that entity to evolve
                } else {
                    GrassType tempG = (GrassType) tempEnt;                                  //cast entity to correct type
                    tempG.Evolve();                                                         //call function for that entity to evolve
                }
            }
        }
    }

    /**
     * Function to keep track of when food grows back
     */
    public void growTimer() {
        AnEntity temp;
        for (int i = 0; i < EntityArr.size(); i++) {
            temp = EntityArr.get(i);
            if (temp instanceof Food) {
                if (!(((Food) temp).getGrowTime() == 0)) {
                    ((Food) temp).setGrowTime(((Food) temp).getGrowTime() - 1);
                }
            }
        }
    }
    //</editor-fold>
}
