import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class StudentFrame extends JFrame{
    private final Container container = getContentPane();
    private final JButton updateDetailsButton = new JButton("UPDATE DETAILS");
    private final JButton viewGradesButton = new JButton("VIEW GRADES");
    private final JButton logoutButton = new JButton("LOGOUT");


    private final String name;
    private final String surname;
    private final String databasePath = "C:/Users/Liviu/Desktop/JAVA/Projectululu/studentDetails.ndjson";


    public StudentFrame(String name, String surname)
    {
        this.name = name;
        this.surname = surname;

        this.setTitle("Student");
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
        updateDetailsButton.setBounds(60,150,250,30);
        viewGradesButton.setBounds(60, 200, 250, 30);
        logoutButton.setBounds(60, 50, 250, 30);
    }

    private void addComponentsToContainer()
    {
        container.add(updateDetailsButton);
        container.add(viewGradesButton);
        container.add(logoutButton);
    }

    private void addActionEvent()
    {
        // If the logout button is pressed, go back to SignIn Frame
        logoutButton.addActionListener(e -> dispose());

        // If the view grades button is pressed, show all the grades associated with the student with given name
        // and surname
        viewGradesButton.addActionListener(e -> {
            JSONObject obj;
            // search in the student database
            if((obj = existInDatabase(name, surname)) == null) {
                printErrorMessage(2);
                return;
            }

            // If everything went well during the search, get the grades of the student
            for (Object key : obj.keySet()) {
                if(key.equals("name") || key.equals("surname") || key.equals("status") || key.equals("telephone"))
                    continue;

                System.out.println("Subject: " + key + ": " + obj.get(key));
            }
        });
        updateDetailsButton.addActionListener(e -> {
            // Remove the initial buttons for now
            container.remove(updateDetailsButton);
            container.remove(viewGradesButton);

            // create some new fields etc
            JLabel surnameLabel = new JLabel("SURNAME");
            JLabel nameLabel = new JLabel("NAME");
            JLabel subjectLabel = new JLabel("Field");
            JLabel gradeLabel = new JLabel("Text");
            JTextField nameTextField = new JTextField();
            JTextField surnameTextField = new JTextField();
            JButton updateForButton = new JButton("UPDATE FOR THIS STUDENT");

            String[] userType = {"STATUS", "TELEPHONE"};
            JComboBox<String> subjectComboBox = new JComboBox<>(userType);
            JTextField gradeTextField = new JTextField();
            JButton addButton = new JButton("ADD");
            JButton doneButton = new JButton("DONE");


            // set the location and size
            nameLabel.setBounds(60, 150, 250, 30);
            nameTextField.setBounds(60, 180, 250, 30);
            surnameLabel.setBounds(60, 210, 250, 30);
            surnameTextField.setBounds(60, 240, 250, 30);
            updateForButton.setBounds(60, 270, 250, 30);
            subjectLabel.setBounds(60, 300, 250, 30);
            subjectComboBox.setBounds(60, 330, 250, 30);
            gradeLabel.setBounds(60, 360, 125, 30);
            gradeTextField.setBounds(130, 360, 125, 30);
            addButton.setBounds(60, 390, 125, 30);
            doneButton.setBounds(185, 390, 125, 30);

            // Add the new components to the container
            container.add(nameTextField);
            container.add(nameLabel);
            container.add(surnameLabel);
            container.add(surnameTextField);
            container.add(updateForButton);
            container.add(subjectLabel);
            container.add(subjectComboBox);
            container.add(gradeLabel);
            container.add(gradeTextField);
            container.add(addButton);
            container.add(doneButton);

            // Repaint the current container to properly show the new components
            container.repaint();
            container.revalidate();

            // If the done button is pressed, go back to the initial GUI
            doneButton.addActionListener(e1 -> paintInitUI());

            // Add functionality for the ADD button
            addButton.addActionListener(e1 -> {
                // get the info from the text fields
                String name = nameTextField.getText();
                String surname = surnameTextField.getText();
                String grade = gradeTextField.getText();
                String subject = null;

                Object t = subjectComboBox.getSelectedItem();

                if(t != null)
                    subject = t.toString();
                    // if the subject was not chose, it is safe to say that that field was left empty
                else
                    printErrorMessage(1);

                // If one of the fields was left empty, print an error message
                if(subject == null || (name.equals("")) || surname.equals("") || grade.equals("")) {
                    printErrorMessage(1);
                    return;
                }

                // Check if the grade introduced is valid, if not, print an error message

                addUpdateToDatabase(name, surname, subject, grade);

                // set the text fields to empty strings
                nameTextField.setText("");
                surnameTextField.setText("");
                gradeTextField.setText("");

                // Notify the user that the student's details were updated with success
                //printSuccessMessage(3);

            });
        });
    }

    private void paintInitUI() {
        container.removeAll();

        addComponentsToContainer();

        container.repaint();
    }

    private JSONObject existInDatabase(String name, String surname) {
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
                    return obj;
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        // if it gets till here it means that it either is an exception, or the user does not exist in the database
        return null;
    }

    private void addUpdateToDatabase(String name, String surname, String subject, String grade) {
        JSONObject obj;
        // Check if the student with the given name and surname exists in the database, and if no, print error message
        if((obj = existInDatabase(name, surname)) == null) {
            printErrorMessage(3);
            return;
        }

        // Add to the selected student, a new subject entry
        obj.put(subject, grade);

        // Add in the database the object
        writeToDatabase(obj);
    }
    private void writeToDatabase(JSONObject obj) {
        // Open the JSON file and search in it, and if the user with the same username is not found, continue
        try(FileWriter file = new FileWriter(databasePath, true)) {

            file.write(obj.toJSONString());
            file.write('\n');

            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method prints to the current frame different error messages.
     * @param error_number It represent the error code that will display different messages to the user <br>
     *                     1 -> One of the boxes was left empty <br>
     *                     2 -> The student was not found in the database <br>
     *                     3 -> The grade introduced is not valid
     */
    private void printErrorMessage(int error_number) {
        // create a JLabel above all of the information, make it red
        JLabel errorLabel = new JLabel();
        errorLabel.setForeground(Color.red);
        errorLabel.setBounds(60, 80, 300, 20);
        container.add(errorLabel);
        errorLabel.setOpaque(true);

        switch (error_number) {
            case 1 -> errorLabel.setText("*One of the fields was left empty");
            case 2 -> errorLabel.setText("*The student was not yet registered");
            case 3 -> errorLabel.setText("*The grade introduced is not valid");
            default -> errorLabel.setText("*Invalid error_number");
        }
    }
}
