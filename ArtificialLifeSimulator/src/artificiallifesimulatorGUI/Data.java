package artificiallifesimulatorGUI;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;

/**
 * Deals with the configuration files
 *
 * @author Ben Phillips
 * @since 05/11/16
 *
 */
public class Data {

    File f; //Variable to hold file
    String fileName = null;//Hold the file name of initial file
    int holder = 0;

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    Data() {
        fileName = "Config.txt";
        f = new File(fileName);
    }

    Data(String name) {//Constructor
        f = new File(name);	//sets f to file with the name input
        fileName = name;
    }

    //</editor-fold>
    //<editor-fold desc="Other Functions" defaultstate="collapsed">
    /**
     * Function for writing configurations in the file
     */
    void writeToFile(String input) {
        try {
            BufferedWriter fout = new BufferedWriter(new FileWriter(f, true));      //creates a bufferedwriter to write to file f
            if (!f.exists()) {                                                      //if f doesn't exist
                f.createNewFile();                                                  //creates new file
            }
            if (holder == 1) {
                fout.newLine();
                holder = 0;
            }
            fout.write(input);                                                      //writes input in file f
            fout.flush();                                                           //clears the stream
            fout.newLine();                                                         //moves to the next line in the file
            fout.close();                                                           //closes the stream
            AlertBox("", "Configuration Saved");

        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    /**
     * Function for reading the last line of the file (current configuration)
     */
    String readFromFile(int lineNum) {
        String read = null;//initialises string
        int lineCount = 0;
        try {
            BufferedReader fIn = new BufferedReader(new FileReader(f));             //creates a bufferedReader to read from file f
            Scanner scan = new Scanner(f);                                          //creates a scanner that scans from f
            while (scan.hasNext()) {                                                //while there is still more text
                scan.nextLine();                                                    //move the scanner to next line
                read = fIn.readLine();                                              //keep setting read to the current line (will leave read equal the last line of the file)
                if (lineCount == (lineNum - 1)) {
                    fIn.close();                                                    //close the stream and scanner
                    scan.close();
                    return read;
                }
                lineCount++;
            }
            fIn.close();                                                            //close the stream and scanner
            scan.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
        return read;                                                                //return read (last line of file)
    }

    /*    void renameFile(String name) {//Function for renaming the file
        try {
            if (!(new File(name).exists())) {
                Path source = Paths.get(fileName);//sets source to the path for file f
                Files.move(source, source.resolveSibling(name));//renames the file to name
                fileName = name;//so if this function is called again it will get the path for the correct file
                f = new File(name);//sets f = to the new file
            }
        } catch (IOException e) {//for exceptions that might be thrown

        }
    }*/
    /**
     * function for saving the file as another file/name
     */
    void saveAs() {
        holder++;
        String temp1;
        TextInputDialog box = new TextInputDialog(".txt");
        box.setTitle("Save As");
        box.setContentText("What Would You Like To Name The File? ");
        Optional<String> result = box.showAndWait();                                //get new name of file from user
        if (result.isPresent()) {
            temp1 = result.get();
            File temp = new File(temp1);                                            //create a new file with the new name
            try {
                if (temp.exists()) {                                                //if a file of that name already exists
                    AlertBox("", "That file already exists. Try again.");           //alert the user of this
                } else {
                    Files.copy(f.toPath(), temp.toPath());                          //otherwise creat a new file in the directory
                    f = temp;                                                       //set the file currently being used to that one
                }
            } catch (IOException e) {

            }
        }

    }

    /**
     *
     * Function for opening the file returns each line to be printed
     */
    String openFile() {
        String line = "";
        try {
            Scanner scan = new Scanner(f);
            BufferedReader fIn = new BufferedReader(new FileReader(f));
            if (scan.hasNext()) {
                while (scan.hasNext()) {                                            //while there is more text in the file
                    scan.nextLine();                                                //scan the next line
                    line += fIn.readLine() + "\n";                                  //add it to the string to be returned
                }
            }
            if ("".equals(line)) {                                                  //if the first line has no text
                line = "The File Is Empty Please Try Again.";                       //alert the user that the file is empty
            } else {
                return line;                                                        //return the string
            }
            scan.close();                                                           //close the scanner and reader
            fIn.close();

        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
        return line;
    }

    /**
     * Function for creating an alert box
     */
    void AlertBox(String title, String content) {

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }

    //</editor-fold>

    public static void main(String[] args) {

        /* Scanner scan = new Scanner(System.in);
		Data test = new Data();
		System.out.println("Enter the configuration");
		test.writeToFile(scan.nextLine());
		test.writeToFile(scan.nextLine());
		System.out.println(test.readFromFile(scan.nextInt()));
		System.out.println("What do you want to rename the file to: (Example: name.txt)");
		test.renameFile(scan.nextLine());
		System.out.println("Enter another configuration");
		test.writeToFile(scan.nextLine());
		scan.close(); */
    }

}
