import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class StudentFrame extends JFrame{
    private final Container container = getContentPane();
    private final JButton updateDetailsButton = new JButton("UPDATE DETAILS");
    private final JButton viewGradesButton = new JButton("VIEW GRADES");
    private final JButton logoutButton = new JButton("LOGOUT");


    private final String name;
    private final String surname;
    private final String databasePath = "/run/media/2021/SEF/PROJECT/user_info/studentDetails.ndjson";


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
