import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AdministratorFrame extends JFrame {

    private final Container container = getContentPane();
    private final JButton addStudentButton = new JButton("ADD STUDENT");
    private final JButton updateDetailsButton = new JButton("UPDATE STUDENT DETAILS");
    private final JButton logoutButton = new JButton("LOGOUT");
    private final String databasePath = "/run/media/2021/SEF/PROJECT/user_info/studentDetails.ndjson";

    private boolean LoggedOut = false;


    public AdministratorFrame()
    {
        this.setTitle("Administrator");
        this.setVisible(true);
        this.setResizable(false);
        this.setBounds(10,10,370,600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent(); //calling addActionEvent() method

    }

    private void setLayoutManager()
    {
        container.setLayout(null);
    }

    private void setLocationAndSize()
    {
        addStudentButton.setBounds(60,150,250,30);
        updateDetailsButton.setBounds(60,200,250,30);
        logoutButton.setBounds(60, 500, 250, 30);
    }

    private void addComponentsToContainer()
    {
        container.add(addStudentButton);
        container.add(updateDetailsButton);
        container.add(logoutButton);

    }

    private void addActionEvent()
    {
        // adding Action listener to the addStudentButton button
        addStudentButton.addActionListener(e -> {

            // move the updateDetailsButton a bit below
            updateDetailsButton.setLocation(60, 350);

            // make the button invisible for now
            addStudentButton.setVisible(false);

            // create 2 new fields, one for the Name, and one for the Surname
            JLabel surnameLabel = new JLabel("SURNAME");
            JLabel nameLabel = new JLabel("NAME");
            JTextField nameTextField = new JTextField();
            JTextField surnameTextField = new JTextField();
            JButton addButton = new JButton("ADD");
            JButton doneButton = new JButton("DONE");


            // set the location and size
            nameLabel.setBounds(60, 150, 100, 30);
            nameTextField.setBounds(60, 180, 250, 30);
            surnameLabel.setBounds(60, 210, 250, 30);
            surnameTextField.setBounds(60, 240, 250, 30);
            addButton.setBounds(60, 270, 125, 30);
            doneButton.setBounds(185, 270, 125, 30);
            container.add(nameTextField);
            container.add(nameLabel);
            container.add(surnameLabel);
            container.add(surnameTextField);
            container.add(addButton);
            container.add(doneButton);


            // add action listeners for buttons and for the TextFields
            // add an action listener for the add button and for the done button
            addButton.addActionListener(e1 -> {
                // if the button is pressed, check in the database if the student already exists, and if
                // not, add it to the database
                String name = nameTextField.getText();
                String surname = surnameTextField.getText();

                // if one or neither of the text fields for name and surname doesn't contain data, print error message
                if(name.equals("") || surname.equals(""))
                    printErrorMessage(1);

                addToDatabase(name, surname);

                // set the text fields to empty strings
                nameTextField.setText("");
                surnameTextField.setText("");

            });

            doneButton.addActionListener(e1 -> {
                container.removeAll();

                // make the addStudentButton visible again
                addStudentButton.setVisible(true);

                // move the updateDetailsButton back to its initial place
                updateDetailsButton.setLocation(60, 200);

                addComponentsToContainer();

                container.repaint();
            });

        });

        // Logout the user if the user presses the corresponding button, also closing the Administrator window
        logoutButton.addActionListener( e -> {
            // remove this page and set the hasLogout to true
//            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            this.dispose();
            LoggedOut = true;
        });
    }

    private void addToDatabase(String name, String surname) {
        if((existInDatabase(name, surname)))
            printErrorMessage(2);
        else {
            JSONObject studentDetails = new JSONObject();
            studentDetails.put("name", name);
            studentDetails.put("surname", surname);

            // Open the JSON file and search in it at first, and if the user with the same username is not found, continue
            try(FileWriter file = new FileWriter(databasePath, true)) {

                file.write(studentDetails.toJSONString());
                file.write('\n');

                file.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean existInDatabase(String name, String surname) {
        try (FileReader reader = new FileReader(databasePath)) {
            // Read from the .json file line by line -> .ndjson style
            BufferedReader buffReader = new BufferedReader(reader);
            String line;

            while((line = buffReader.readLine()) != null) {
                Object o = new JSONParser().parse(line);
                JSONObject obj = (JSONObject) o;

                String objectName = (String) obj.get("name");
                String objectSurname = (String) obj.get("surname");

                if(name.equals(objectName) && surname.equals(objectSurname))
                    return true;
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        // if it gets till here it means that it either is an exception, or the user does not exist in the database
        return false;
    }

    public boolean isLoggedOut() {
        return LoggedOut;
    }


    // this method will print various error messages based on the errors
    // error_number -> 1 : one of the boxes was left empty, cannot add a student to the database
    //              -> 2 : Student already exists in the database
    //              -> 3 : the user with the given username already exists, cannot register user
    private void printErrorMessage(int error_number) {
        // create a JLabel above all of the information, make it red
        JLabel errorLabel = new JLabel();
        errorLabel.setForeground(Color.red);
        errorLabel.setBounds(60, 80, 300, 20);
        container.add(errorLabel);

        switch (error_number) {
            case 1 -> {
                errorLabel.setOpaque(true);
                errorLabel.setText("*One of the fields was left empty");
            }
            case 2 -> {
                errorLabel.setOpaque(true);
                errorLabel.setText("*Student already exists in the database");
            }
            default -> {
                errorLabel.setOpaque(true);
                errorLabel.setText("*Invalid error_number");
            }
        }
    }
}
