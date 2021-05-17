import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends GeneralFrame {
    private final JLabel nameLabel = new JLabel("NAME");
    private final JLabel surnameLabel = new JLabel("SURNAME");
    private final JLabel userLabel = new JLabel("USERNAME");
    private final JLabel passwordLabel = new JLabel("PASSWORD");
    private final JButton goBack = new JButton("Go Back to Sing In");
    private final JTextField nameTextField = new JTextField();
    private final JTextField surnameTextField = new JTextField();
    private final JTextField userTextField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JButton registerButton = new JButton("REGISTER");
    private final JCheckBox showPassword = new JCheckBox("Show Password");

    private final String[] userType = {"Student", "Company", "Administrator"};
    private final JLabel userTypeLabel = new JLabel("REGISTER AS");
    private final JComboBox<String> userTypeComboBox = new JComboBox<>(userType);


    RegisterFrame()
    {
        super();
        this.setTitle("Registration Form");
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();

    }

    protected void setLocationAndSize()
    {
        nameLabel.setBounds(50, 150, 100, 30);
        surnameLabel.setBounds(50, 180, 100, 30);
        nameTextField.setBounds(150, 150, 150, 30);
        surnameTextField.setBounds(150, 180, 150, 30);
        userTypeLabel.setBounds(50, 100, 100, 30);
        userLabel.setBounds(50,210,100,30);
        passwordLabel.setBounds(50,240,100,30);
        userTypeComboBox.setBounds(150, 100, 150, 30);
        userTextField.setBounds(150,210,150,30);
        passwordField.setBounds(150,240,150,30);
        showPassword.setBounds(150,270,150,30);
        registerButton.setBounds(150,300,100,30);
        goBack.setBounds(80,400,200,30);
        container.revalidate();
        container.repaint();
    }

    protected void addComponentsToContainer()
    {
        container.add(nameLabel);
        container.add(surnameLabel);
        container.add(nameTextField);
        container.add(surnameTextField);
        container.add(userLabel);
        container.add(passwordLabel);
        container.add(userTextField);
        container.add(passwordField);
        container.add(showPassword);
        container.add(registerButton);
        container.add(userTypeComboBox);
        container.add(userTypeLabel);
        container.add(goBack);
    }

    protected void addActionEvent()
    {
        // Adding Action listener to the showPassword checkbox
        showPassword.addActionListener(e -> {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('*');
            }
        });
        goBack.addActionListener(e -> dispose());

        // adding Action listener to the registerButton button
        registerButton.addActionListener(e -> {
            // save in some local variables the details needed to register a user

            // based on what the user chose to register as, save it in the type variable
            String type = null;
            Object t = userTypeComboBox.getSelectedItem();

            if(t != null)
                type = t.toString();

            // get the name introduced by the user
            String name = nameTextField.getText();

            // get the surname introduced by the user
            String surname = surnameTextField.getText();

            // get the username inputted by the user
            String username = userTextField.getText();

            // get the password of the user
            // don't forget to hash it later and save it as such in the json file
            String password = String.valueOf(passwordField.getPassword());

            // if all the fields are completed with information, check the information and register the user
            if(type != null && !(username.equals("")) && !(password.equals("")) && !(name.equals("")) &&
                    !(surname.equals(""))) {
                // if everything was ok, check if the username is not yet in the database and that it has only letters
                // numbers, dashes and points, add to the new 'database' as a new entry
                if(isValidUsername(username)) {

                    // if the username is valid, put it the json file if it is not already there
                    tryRegistering(type, username, password, name, surname);
                }
                else {
                    printErrorMessage(2);
                }
            }
            // if one of the boxes is not completed/chosen, display a message
            else {
                printErrorMessage(1);
            }
        });
    }

    // A username should have only letters, numbers, dashes and points
    private boolean isValidUsername(String username) {
        for(int i = 0; i < username.length(); ++i) {
            char c = username.charAt(i);

            // if it does NOT follow the username chosen standard, return false
            if(!(Character.isDigit(c) || Character.isLetter(c) || c == '-' || c == '.'))
                return false;
        }

        return true;
    }

    private void tryRegistering(String chosenUser, String username, String password, String name, String surname) {
        //Authentication a = new Authentication();

        // Try and register the user with the given credentials
        if(Authentication.registerUser(chosenUser, username, password, name, surname)) {
            // Print in the RegisterFrame the message that the account was created successfully
            JLabel successLabel = new JLabel();
            successLabel.setForeground(new Color(0, 200, 0));
            successLabel.setBounds(50, 80, 300, 20);
            container.add(successLabel);

            successLabel.setOpaque(true);
            successLabel.setText("Account created successfully.");
        }
        else {
            printErrorMessage(3);
        }
    }


    /**
     * This method will print various error messages based on the errors
     * @param error_number It represents the error code based on which error messages are printed in the gui<br>
     *                     1 -> one of the boxes was left empty, cannot register <br>
     *                     2 -> invalid username format <br>
     *                     3 -> the user with the given username already exists, cannot register user <br>
     */
    private void printErrorMessage(int error_number) {
        // create a JLabel above all of the information, make it red
        JLabel errorLabel = new JLabel();
        errorLabel.setForeground(Color.red);
        errorLabel.setBounds(50, 80, 300, 20);
        container.add(errorLabel);
        errorLabel.setOpaque(true);

        switch (error_number) {
            case 1 -> errorLabel.setText("*One of the fields was left empty");
            case 2 -> errorLabel.setText("*Invalid username: use a-z/A-z/0-9/-/.");
            case 3 -> errorLabel.setText("*Account already exists");
            default -> errorLabel.setText("*Invalid error_number");
        }
    }
}
