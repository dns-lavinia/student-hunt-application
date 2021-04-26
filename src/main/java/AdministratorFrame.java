import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class AdministratorFrame extends JFrame {

    private final Container container = getContentPane();
    private final JButton addStudentButton = new JButton("ADD STUDENT");
    private final JButton updateDetailsButton = new JButton("UPDATE STUDENT DETAILS");
    private final JButton logoutButton = new JButton("LOGOUT");
    private final JButton deleteButton = new JButton("DELETE STUDENT");
    private final String databasePath = "/run/media/2021/SEF/PROJECT/user_info/studentDetails.ndjson";


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
        deleteButton.setBounds(60, 250, 250, 30);
        logoutButton.setBounds(60, 500, 250, 30);
    }

    private void addComponentsToContainer()
    {
        container.add(addStudentButton);
        container.add(updateDetailsButton);
        container.add(deleteButton);
        container.add(logoutButton);
    }

    private void addActionEvent()
    {
        // adding Action listener to the addStudentButton button
        addStudentButton.addActionListener(e -> {

            // make the button invisible for now
            container.remove(updateDetailsButton);
            container.remove(addStudentButton);
            container.remove(deleteButton);

            // create some new fields etc
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

            // Repaint the current container to properly show the new components
            container.repaint();
            container.revalidate();


            // add action listeners for buttons and for the TextFields
            // add an action listener for the add button and for the done button
            addButton.addActionListener(e1 -> {
                // if the button is pressed, check in the database if the student already exists, and if
                // not, add it to the database
                String name = nameTextField.getText();
                String surname = surnameTextField.getText();

                // if one or neither of the text fields for name and surname doesn't contain data, print error message
                if(name.equals("") || surname.equals("")) {
                    printErrorMessage(1);
                    return;
                }


                addToDatabase(name, surname);

                // set the text fields to empty strings
                nameTextField.setText("");
                surnameTextField.setText("");

            });

            // If the done button is pressed, go back to the initial GUI
            doneButton.addActionListener(e1 -> paintInitUI());
        });

        // add Action listener for the updateStudentDetails button
        updateDetailsButton.addActionListener(e -> {

            // make the button invisible for now
            container.remove(updateDetailsButton);
            container.remove(addStudentButton);
            container.remove(deleteButton);

            // create some new fields etc
            JLabel surnameLabel = new JLabel("SURNAME");
            JLabel nameLabel = new JLabel("NAME");
            JLabel subjectLabel = new JLabel("SUBJECT");
            JLabel gradeLabel = new JLabel("GRADE");
            JTextField nameTextField = new JTextField();
            JTextField surnameTextField = new JTextField();
            JButton updateForButton = new JButton("UPDATE FOR THIS STUDENT");

            String[] userType = {"ADA", "CC", "CO", "SEF", "OS", "CN"};
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
            gradeTextField.setBounds(185, 360, 125, 30);
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
                if(!validGrade(grade)) {
                    printErrorMessage(4);
                    return;
                }
                
                addUpdateToDatabase(name, surname, subject, grade);

                // set the text fields to empty strings
                nameTextField.setText("");
                surnameTextField.setText("");
                gradeTextField.setText("");
                
            });
        });

        deleteButton.addActionListener(e -> {
            // make the button invisible for now
            container.remove(updateDetailsButton);
            container.remove(addStudentButton);
            container.remove(deleteButton);

            // create some new fields etc
            JLabel surnameLabel = new JLabel("SURNAME");
            JLabel nameLabel = new JLabel("NAME");
            JTextField nameTextField = new JTextField();
            JTextField surnameTextField = new JTextField();
            JButton deleteStudentButton = new JButton("DELETE");
            JButton doneButton = new JButton("DONE");


            // set the location and size
            nameLabel.setBounds(60, 150, 100, 30);
            nameTextField.setBounds(60, 180, 250, 30);
            surnameLabel.setBounds(60, 210, 250, 30);
            surnameTextField.setBounds(60, 240, 250, 30);
            deleteStudentButton.setBounds(60, 270, 125, 30);
            doneButton.setBounds(185, 270, 125, 30);
            container.add(nameTextField);
            container.add(nameLabel);
            container.add(surnameLabel);
            container.add(surnameTextField);
            container.add(deleteStudentButton);
            container.add(doneButton);

            // Repaint the current container to properly show the new components
            container.repaint();
            container.revalidate();


            // add action listeners for buttons and for the TextFields
            // add an action listener for the add button and for the done button
            deleteStudentButton.addActionListener(e1 -> {
                // if the button is pressed, check in the database if the student already exists, and if
                // not, add it to the database
                String name = nameTextField.getText();
                String surname = surnameTextField.getText();

                System.out.println(name + " " + surname);

                // if one or neither of the text fields for name and surname doesn't contain data, print error message
                if(name.equals("") || surname.equals("")) {
                    printErrorMessage(1);
                    return;
                }

                if(existInDatabase(name, surname, true) == null) {
                    printErrorMessage(3);
                }

                // set the text fields to empty strings
                nameTextField.setText("");
                surnameTextField.setText("");

            });

            // If the done button is pressed, go back to the initial GUI
            doneButton.addActionListener(e1 -> paintInitUI());
        });

        // Logout the user if the user presses the corresponding button, also closing the Administrator window
        logoutButton.addActionListener( e -> this.dispose());
    }

    /**
     * This method checks if a given grade has a valid format of a double
     * @param grade Grade of a student
     * @return It returns true if the inputted grade is valid
     */
    private boolean validGrade(String grade) {
        try {
            double gr = Double.parseDouble(grade);

            // Check if the grade is between 0 and 10
            return 0.0 <= gr && gr <= 10.0;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private void addUpdateToDatabase(String name, String surname, String subject, String grade) {
        JSONObject obj;
        // Check if the student with the given name and surname exists in the database, and if no, print error message
        if((obj = existInDatabase(name, surname, true)) == null) {
            printErrorMessage(3);
            return;
        }

        // Add to the selected student, a new subject entry
        obj.put(subject, grade);



        // Open the JSON file and search in it, and if the user with the same username is not found, continue
        try(FileWriter file = new FileWriter(databasePath, true)) {

            file.write(obj.toJSONString());
            file.write('\n');

            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void paintInitUI() {
        container.removeAll();

        addComponentsToContainer();

        container.repaint();
    }

    @SuppressWarnings("unchecked")
    private void addToDatabase(String name, String surname) {
        if((existInDatabase(name, surname, false)) != null) {
            printErrorMessage(2);
        }
        else {
            JSONObject studentDetails = new JSONObject();
            studentDetails.put("name", name);
            studentDetails.put("surname", surname);

            // Open the JSON file and search in it, and if the user with the same username is not found, continue
            try(FileWriter file = new FileWriter(databasePath, true)) {

                file.write(studentDetails.toJSONString());
                file.write('\n');

                file.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method checks if a certain Student is already registered in the database
     * @param name The name of the student
     * @param surname The surname of the student
     * @param flagRemove This flag is set if it is needed to remove the found Student in the database
     * @return The method returns a JSONObject containing the entry of the student with the given name and surname,
     * and null otherwise
     */
    private JSONObject existInDatabase(String name, String surname, boolean flagRemove) {
        try (FileReader reader = new FileReader(databasePath)) {
            // Read from the .json file line by line -> .ndjson style
            BufferedReader buffReader = new BufferedReader(reader);
            String line;

            while((line = buffReader.readLine()) != null) {
                Object o = new JSONParser().parse(line);
                JSONObject obj = (JSONObject) o;

                String objectName = (String) obj.get("name");
                String objectSurname = (String) obj.get("surname");

                if(name.equals(objectName) && surname.equals(objectSurname)) {
                    if(flagRemove)
                        removeEntry(obj);

                    return obj;
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        // if it gets till here it means that it either is an exception, or the user does not exist in the database
        return null;
    }

    /**
     * This method removes one entry in the ndjson file
     * @param obj Entry of the student that is to be removed from the database
     */
    private void removeEntry(JSONObject obj) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(databasePath));

            // Buffer to store contents of the database
            StringBuilder sb = new StringBuilder();
            String line;

            while((line = br.readLine()) != null)
            {
                // If the line to be deleted is found, skip it
                if(line.equals(obj.toString()))
                    continue;

                sb.append(line).append("\n");
            }

            br.close();
            FileWriter fw = new FileWriter(databasePath);

            // Write into the file
            fw.write(sb.toString());
            fw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * This method prints to the current frame different error messages.
     * @param error_number It represent the error code that will display different messages to the user <br>
     *                     1 -> One of the boxes was left empty, cannot add a student to the database <br>
     *                     2 -> The student already exists in the database <br>
     *                     3 -> The student was not found in the database <br>
     *                     4 -> The grade introduced is not valid
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
            case 2 -> errorLabel.setText("*Student already exists in the database");
            case 3 -> errorLabel.setText("*The student was not yet registered");
            case 4 -> errorLabel.setText("* The grade introduced is not valid");
            default -> errorLabel.setText("*Invalid error_number");
        }


    }
}
