import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class SingInFrame extends GeneralFrame  {
    private final String databasePath = "userCredentials.ndjson";
    JLabel userLabel = new JLabel("USERNAME");
    JLabel passwordLabel = new JLabel("PASSWORD");
    JTextField userTextField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JButton signinButton = new JButton("SIGN IN");
    JButton RegisterButton = new JButton("REGISTER");
    JButton resetButton = new JButton("RESET");
    JCheckBox showPassword = new JCheckBox("Show Password");


    SingInFrame() {
        super();
        this.setTitle("Login Form");
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();
    }

    protected void setLocationAndSize() {
        userLabel.setBounds(50, 150, 100, 30);
        passwordLabel.setBounds(50, 220, 100, 30);
        userTextField.setBounds(150, 150, 150, 30);
        passwordField.setBounds(150, 220, 150, 30);
        showPassword.setBounds(150, 250, 150, 30);
        signinButton.setBounds(50, 300, 100, 30);
        resetButton.setBounds(200, 300, 100, 30);
        RegisterButton.setBounds(50,400,100,30);

    }

    protected void addComponentsToContainer() {
        container.add(userLabel);
        container.add(passwordLabel);
        container.add(userTextField);
        container.add(passwordField);
        container.add(showPassword);
        container.add(signinButton);
        container.add(resetButton);
        container.add(RegisterButton);
        container.revalidate();
        container.repaint();
    }

    private void setVisOnTrue()
    {
        this.setVisible(true);
    }

    protected void addActionEvent() {
        showPassword.addActionListener(e -> {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('*');
            }
        });
        RegisterButton.addActionListener(e -> {
            this.setVisible(false);
            RegisterFrame frame = new RegisterFrame();
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    setVisOnTrue();
                }
            }
            );

        });
        signinButton.addActionListener(e -> {
            String userText;
            String pwdText;
            String nm="",surnm="";

            userText = userTextField.getText();
            pwdText = String.valueOf(passwordField.getPassword());

            //Authentication a = new Authentication();
            if ( Authentication.VerifyData(userText,pwdText) )
            {
                this.setVisible(false);
                String type = Authentication.searchType(userText,"userType");
                if ( type.equals("Company") )
                {
                    CompanyFrame c1 = new CompanyFrame();
                    c1.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            setVisOnTrue();
                        }
                    }
                    );
                }
                else if ( type.equals("Administrator") )
                {
                    AdministratorFrame a1 = new AdministratorFrame();
                    a1.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            setVisOnTrue();
                        }
                    }
                    );
                }
                else
                {

                    try (FileReader reader = new FileReader(databasePath)) {
                        // Read from the .json file line by line -> .ndjson style
                        BufferedReader buffReader =new BufferedReader(reader);
                        String line;

                        while((line = buffReader.readLine()) != null) {
                            Object o = new JSONParser().parse(line);
                            JSONObject obj = (JSONObject) o;

                            String objectUsername = (String) obj.get("username");

                            if(userText.equals(objectUsername))
                            {
                                nm = (String) obj.get("name");
                                surnm = (String) obj.get("surname");
                            }
                        }

                    } catch (IOException | ParseException e1) {
                        e1.printStackTrace();
                    }
                    StudentFrame s1 = new StudentFrame(nm,surnm);
                    s1.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            setVisOnTrue();
                        }
                    });
                }
            }
            else
            {
                printError();
            }
        });
        resetButton.addActionListener(e -> {
            userTextField.setText("");
            passwordField.setText("");
        });
    }
    private void printError()
    {
        JLabel errorLabel = new JLabel();
        errorLabel.setForeground(Color.red);
        errorLabel.setBounds(50, 80, 300, 20);
        container.add(errorLabel);
        errorLabel.setOpaque(true);
        errorLabel.setText("Incorrect username or password");
    }

}