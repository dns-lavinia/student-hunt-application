import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


class SingInFrame extends JFrame  {

    Container container = getContentPane();
    JLabel userLabel = new JLabel("USERNAME");
    JLabel passwordLabel = new JLabel("PASSWORD");
    JTextField userTextField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JButton signinButton = new JButton("SIGN IN");
    JButton RegisterButton = new JButton("REGISTER");
    JButton resetButton = new JButton("RESET");
    JCheckBox showPassword = new JCheckBox("Show Password");


    SingInFrame() {
        this.setTitle("Login Form");
        this.setVisible(true);
        this.setResizable(false);
        this.setBounds(10, 10, 370, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();

    }

    private void setLayoutManager() {
        container.setLayout(null);
    }

    private void setLocationAndSize() {
        userLabel.setBounds(50, 150, 100, 30);
        passwordLabel.setBounds(50, 220, 100, 30);
        userTextField.setBounds(150, 150, 150, 30);
        passwordField.setBounds(150, 220, 150, 30);
        showPassword.setBounds(150, 250, 150, 30);
        signinButton.setBounds(50, 300, 100, 30);
        resetButton.setBounds(200, 300, 100, 30);
        RegisterButton.setBounds(50,400,100,30);

    }

    private void addComponentsToContainer() {
        container.add(userLabel);
        container.add(passwordLabel);
        container.add(userTextField);
        container.add(passwordField);
        container.add(showPassword);
        container.add(signinButton);
        container.add(resetButton);
        container.add(RegisterButton);
    }

    private void setVisOnTrue()
    {
        this.setVisible(true);
    }

    private void addActionEvent() {
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

            userText = userTextField.getText();
            pwdText = String.valueOf(passwordField.getPassword());

            Authentication a = new Authentication();
            if ( a.VerifyData(userText,pwdText) )
            {
                this.setVisible(false);
                String type = a.searchType(userText,"userType");
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
                //else
                    //StudentFrame s1
                System.out.println(type);
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