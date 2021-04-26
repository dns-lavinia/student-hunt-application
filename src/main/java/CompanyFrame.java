import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CompanyFrame extends JFrame {

    Container container = getContentPane();
    JTextField userTextField = new JTextField();
    private final String[] searchingType = {"Average grade","Subject"};
    private final String[] subjectType = {"CN", "SEF","ADA","OS","CO","CC"};
    private final JLabel userTypeLabel = new JLabel("Search by:");
    JLabel userLabel = new JLabel("Grade");
    private final JComboBox<String> userTypeComboBox = new JComboBox<>(searchingType);
    private final JComboBox<String> userSubject = new JComboBox<>(subjectType);
    JButton okButton = new JButton("OK");
    private final JButton logoutButton = new JButton("LOGOUT");

    CompanyFrame ()  {
        this.setTitle("Company searching form");
        this.setVisible(true);
        this.setResizable(false);
        this.setBounds(10, 10, 370, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();
    }

    private void setLocationAndSize() {
        userTextField.setBounds(140, 200, 150, 30);
        userTypeComboBox.setBounds(100, 100, 150, 30);
        okButton.setBounds(100, 300, 100, 30);
        userSubject.setBounds(100,150,150,30);
        logoutButton.setBounds(60, 500, 250, 30);
        userLabel.setBounds(50,200,150,30);
    }

    private void addComponentsToContainer()
    {
        container.add(userTextField);
        container.add(userTypeComboBox);
        container.add(userTypeLabel);
        container.add(okButton);
        container.add(logoutButton);
        container.add(userLabel);
    }

    private void setLayoutManager() {
        container.setLayout(null);
    }
    private void setVisOnTrue()
    {
        this.setVisible(true);
    }

    private void addActionEvent() {
        okButton.addActionListener(e -> {
            String userText;
            userText = userTextField.getText();
            if ( verify(userText) == false )
                return;
            String tp = (String) userTypeComboBox.getSelectedItem();
            String subj = "";
            if ( tp.equals("Subject") )
                subj = (String) userSubject.getSelectedItem();
            this.setVisible(false);
            CompanyPrintPanel c1 = new CompanyPrintPanel();
            c1.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    setVisOnTrue();
                }
            });
            c1.searchData(userText,tp,subj);
        });
        userTypeComboBox.addActionListener(e -> {
            String s = (String) userTypeComboBox.getSelectedItem();
            if ( s.equals("Subject") )
                container.add(userSubject);
            else {
                container.remove(userSubject);
                container.repaint();
                container.revalidate();
            }
        });
        logoutButton.addActionListener( e -> this.dispose());
    }
    private boolean verify(String grade)
    {
        try {
            double gr = Double.parseDouble(grade);
            // Check if the grade is between 0 and 10
            if ( gr > 10.0 || gr < 0.0 )
                printError();
            return 0.0 <= gr && gr <= 10.0;
        } catch(NumberFormatException e) {
            return false;
        }
    }
    private void printError()
    {
        JLabel errorLabel = new JLabel();
        errorLabel.setForeground(Color.red);
        errorLabel.setBounds(50, 80, 300, 20);
        container.add(errorLabel);
        errorLabel.setOpaque(true);
        errorLabel.setText("Introduced text is not a number");
    }

}
