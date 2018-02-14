package artificiallifesimulatorGUI;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Implements the GUI for the artificial life simulator
 *
 * @author Ben Phillips
 * @since 05/11/16
 *
 */
public class GUInterface extends Application {

    AWorld world = new AWorld();
    Data data = new Data();
    String config;
    int imageH = 40; //Sets image height and width 
    int imageW = 40;
    Image grass = new Image(getClass().getResourceAsStream("Grass.png"), imageW, imageH, false, false); //the two images used in the background
    ImageView back = new ImageView(new Image(getClass().getResourceAsStream("GrassBack.png"), (imageW * 10), (imageH * 10), false, false));
    AnimationTimer timer;
    int pressCount;
    String name = null;
    StackPane stack = new StackPane();
    Pane backPane = new Pane();
    GridPane entPane = new GridPane();
    final Text stats = new Text("No Entities Present\nPlease Enter A Configuration To View Info\nENTITIES:\nSquirtle (W)\nCharmander (F)\nBulbasaur (G)");
    final Text worldInfo = new Text(world.worldInfo());
    final Text text = new Text();
    private final BorderPane pane = new BorderPane();

    @Override
    public void start(Stage primaryStage) {

        /*
         * Sets up the GUI when the program is run
         */
        resizeBack();
        fillGrass();
        TabPane tabPane = new TabPane(); //for holding information about the world
        tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        /*Sets up the tabs content and titles, adds them to the tabpane*/
        Tab tab = new Tab();
        tab.setText("Entity Info");
        tab.setContent(stats);
        Tab tab1 = new Tab();
        tab1.setText("World Info");
        tab1.setContent(worldInfo);
        tabPane.getTabs().add(tab);
        tabPane.getTabs().add(tab1);
        /*Sets up the stage andpanes inside the stage, adding buttons etc in the right places*/
        primaryStage.setTitle("Artificial Life Simulator");
        text.setText("Current Configuration: " + config);
        Button btn = new Button("Run Simulation");
        Button btn1 = new Button("Pause");
        Button btn2 = new Button("Reset");
        Group group = new Group();
        HBox box = new HBox();
        group.getChildren().add(text);
        group.getChildren().add(box);
        pane.setTop(setMenu());
        pane.setRight(tabPane);
        stack.getChildren().addAll(backPane, entPane);
        pane.setCenter(stack);
        box.setPadding(new Insets(25));
        box.setSpacing(25);
        box.getChildren().add(btn);
        box.getChildren().add(btn1);
        box.getChildren().add(btn2);
        pane.setBottom(group);
        Scene scene = new Scene(pane, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
        /*Allows the user to select a data file to use*/
        Optional<String> result = TextInputBox("Config.txt", "File", null, "Please Enter The Name Of The File: ").showAndWait();
        if (result.isPresent()) {
            name = result.get();            //If they chose, use that file
        } else {
            name = "Config.txt";            //else use default 
        }
        data = new Data(name);              //create an instance of data using the selected file name
        config = data.readFromFile(1);      //read the first line of the file, remembering last config used
        setWorld();                         //calls set world function

        /*
         * sets up the buttons for simulation
         * Run, Pause/restart and Reset
         */
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {               //actions for the run button
                timer = new AnimationTimer() {                          //initialises the timer for simulation
                    @Override
                    public void handle(long now) {
                        world.movement();                               //calls movement function, moves entities
                        world.decreaseEnergy();                         //changes energy of entites after moving
                        entPane.getChildren().clear();                  //clears the world, ready for entities to be redrawn
                        fillGrass();                                    //add the background grass
                        world.EvolutionCheck();                         //checks if any entities are able to "evolve"
                        world.growTimer();                              //deals with food growing back
                        AddEntities();                                  //redraws all the entities
                        stats.setText(world.stats());                   //updates the two information tabs
                        worldInfo.setText(world.worldInfo());
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(GUInterface.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }

                };
                timer.start();
            }
        });
        btn1.setOnAction(new EventHandler<ActionEvent>() {      //action for the pause/restart button
            @Override
            public void handle(ActionEvent actionEvent) {
                pressCount++;                                   //keeps track of button presses, to switch from pause to restart
                if (pressCount % 2 == 0) {                      //if even presses, restart the timer, set text to puase 
                    btn1.setText("Pause");
                    timer.start();
                } else {                                        //odd presses does the opposite
                    btn1.setText("Restart");
                    timer.stop();
                }
            }
        });
        btn2.setOnAction(new EventHandler<ActionEvent>() {      //action for the reset button 
            @Override
            public void handle(ActionEvent actionEvent) {
                setWorld();                                     //resets the world
                btn1.setText("Pause");                          //sets the pause/restart button back to the pause setting
                if (!(pressCount == 0)) {
                    pressCount = 0;
                }
                timer.stop();                                   //stops timer incase it was running when the reset button was pressed
            }
        });

    }

    /**
     * Function to set up the menu
     */
    MenuBar setMenu() {
        MenuBar menuBar = new MenuBar();

        Menu mFile = new Menu("File");
        MenuItem mNew = new MenuItem("New Configuration");
        mNew.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stats.setText("No Entities Present\nPlease Enter A Configuration To View Info\nENTITIES:\nSquirtle (W)\nCharmander (F)\nBulbasaur (G)");
                worldInfo.setText("No Entities Present\nPlease Enter A Configuration To View Info\nENTITIES:\nSquirtle (W)\nCharmander (F)\nBulbasaur (G)");
                Optional<String> result = TextInputBox("", "New Configuration", null, "Please Enter The Configuration: ").showAndWait();
                if (result.isPresent()) {
                    config = result.get();
                    setWorld();
                }
                stats.setText(world.stats());
                worldInfo.setText(world.worldInfo());
            }
        });

        MenuItem mOpen = new MenuItem("Open Configuration File");
        mOpen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Optional<String> result = TextInputBox("", "Open File", data.openFile(), "Would You Like To Load A Configuration?").showAndWait();
                if (result.isPresent()) {
                    if ("Yes".equals(result.get()) || "Y".equals(result.get()) || "y".equals(result.get())) {
                        Optional<String> result2 = TextInputBox("", "Load File", data.openFile(), "Enter The Line Number Of The Config You Want To Load: ").showAndWait();
                        if (result2.isPresent()) {
                            try {
                                Integer.parseInt(result2.get());
                            } catch (NumberFormatException ex) {
                                AlertBox("Load Configuration", "Invalid Option, please try again");
                            }
                            config = data.readFromFile(Integer.parseInt(result2.get()));
                            setWorld();
                        }
                    } else if (!("Yes".equals(result.get()) || "Y".equals(result.get()) || "y".equals(result.get())
                            || "No".equals(result.get()) || "N".equals(result.get()) || "n".equals(result.get()))) {
                        AlertBox("Open Configuration", "That is not an option, try again");
                    }
                }
            }
        });

        MenuItem mSave = new MenuItem("Save Configuration");
        mSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                data.writeToFile(config);
            }
        });

        MenuItem mSaveAs = new MenuItem("Save As");
        mSaveAs.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                data.saveAs();
            }
        });

        MenuItem mExit = new MenuItem("Exit");
        mExit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
        mFile.getItems().addAll(mNew, mOpen, mSave, mSaveAs, mExit);
        Menu mView = new Menu("View");
        MenuItem mEdit = new MenuItem("Edit Configuration");
        mEdit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stats.setText("No Entities Present\nPlease Enter A Configuration To View Info\nENTITIES:\nSquirtle (W)\nCharmander (F)\nBulbasaur (G)");
                worldInfo.setText("No Entities Present\nPlease Enter A Configuration To View Info\nENTITIES:\nSquirtle (W)\nCharmander (F)\nBulbasaur (G)");
                Optional<String> result = TextInputBox("", "Edit Configuration", "Current Config: " + config, "Please Enter Edited Config: ").showAndWait();
                if (result.isPresent()) {
                    config = result.get();
                    setWorld();
                }
                stats.setText(world.stats());
                worldInfo.setText(world.worldInfo());

            }
        });
        mView.getItems().addAll(mEdit);
        Menu mEdit1 = new Menu("Edit");
        MenuItem mModify = new MenuItem("Modify Life Form");
        mModify.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                world.modifyEntity();
                stats.setText(world.stats());
                worldInfo.setText(world.worldInfo());
                entPane.getChildren().clear();
                fillGrass();
                AddEntities();
            }
        });

        MenuItem mRemove = new MenuItem("Remove Life Form");
        mRemove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                world.removeEntity();
                stats.setText(world.stats());
                worldInfo.setText(world.worldInfo());
                entPane.getChildren().clear();
                fillGrass();
                AddEntities();
            }
        });

        MenuItem mAdd = new MenuItem("Add New Life Form");
        mAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                world.addEntity();
                stats.setText(world.stats());
                worldInfo.setText(world.worldInfo());
                entPane.getChildren().clear();
                fillGrass();
                AddEntities();
            }
        });
        mEdit1.getItems().addAll(mModify, mRemove, mAdd);
        Menu mHelp = new Menu("Help");
        MenuItem mApp = new MenuItem("Information About Application");
        mApp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AlertBox("Application Information", "Artificial Life Simulator\nCS2JA16 Java\nDue: 12/01/17\n"
                        + "\n***USER GUIDE***\n* Enter configurations in the form XX YY FF OO EntityName NumberOf EntityName NumberOf\n"
                        + "* Please only choose entities from the list\n* When choosing entities use the form EntityName ID\n\n"
                        + "* Thanks for using this simulator");
            }
        });

        MenuItem mAuthor = new MenuItem("Information About Author");
        mAuthor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AlertBox("Author Information", "Author: Ben Phillips\nDegree Title: Cybernetics\nStudent Number: 24008538");
            }
        });
        mHelp.getItems().addAll(mApp, mAuthor);
        menuBar.getMenus().addAll(mFile, mView, mEdit1, mHelp);
        return menuBar;

    }

    /**
     * Function to add the correct entity to the correct position
     */
    void showEntity(Image image, int x, int y) {
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(imageH);
        imageView.setFitWidth(imageW);
        entPane.add(imageView, x, y, 1, 1);             //adds the entity to the pane
    }

    /**
     * Function for adding entities to the world
     */
    public void AddEntities() {
        world.showEntities(this);
    }

    /**
     * Function to resize the background image when the world size is changed
     */
    public void resizeBack() {
        backPane.getChildren().clear();
        back = new ImageView(new Image(getClass().getResourceAsStream("GrassBack.png"), (imageW * world.getX()), (imageH * world.getY()), false, false));
        backPane.getChildren().add(back);
    }

    /**
     * Function to add grass to the background of every position
     */
    public void fillGrass() {
        for (int x = 0; x < world.getX(); x++) {
            for (int y = 0; y < world.getY(); y++) {
                ImageView image = new ImageView(grass);
                image.setFitHeight(imageH);
                image.setFitWidth(imageW);
                entPane.add(image, x, y, 1, 1);
            }
        }
    }

    /**
     * Function to set up the world Resizes images depending on world size so it
     * will fit on the screen Sets the information tabs resizes and fills in
     * background and adds entities
     */
    public void setWorld() {
        if (!(config == null)) {
            world.fromText(config);
            imageW = 40;
            imageH = 40;
            if (world.getX() >= 17 || world.getY() >= 17) {
                imageW = 30;
                imageH = 30;
            }
            if (world.getX() >= 22 || world.getY() >= 22) {
                imageW = 25;
                imageH = 25;
            }
            if (world.getX() >= 27 || world.getY() >= 27) {
                imageW = 20;
                imageH = 20;
            }
            text.setText(config);
            stats.setText(world.stats());
            worldInfo.setText(world.worldInfo());
            entPane.getChildren().clear();
            resizeBack();
            fillGrass();
            AddEntities();
        }
    }

    /**
     * Function to create alert boxes
     */
    void AlertBox(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }

    /**
     * Function for creating TextInputDialogs
     */
    TextInputDialog TextInputBox(String option, String title, String header, String content) {
        TextInputDialog dialog = new TextInputDialog(option);
        dialog.setTitle(title);
        dialog.setContentText(content);
        dialog.setHeaderText(header);

        return dialog;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

}
