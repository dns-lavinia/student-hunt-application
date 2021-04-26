import javax.swing.*;
import java.awt.*;

public class CompanyFrame extends JFrame {

    Container container = getContentPane();
    JTextField userTextField = new JTextField();
    private final String[] searchingType = {"Average grade","Subject"};
    private final String[] subjectType = {"PT", "SEF","ADA","OS","CO","CC"};
    private final JLabel userTypeLabel = new JLabel("Search by:");
    private final JComboBox<String> userTypeComboBox = new JComboBox<>(searchingType);
    private final JComboBox<String> userSubject = new JComboBox<>(subjectType);
    JButton okButton = new JButton("OK");

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
        userTextField.setBounds(100, 200, 150, 30);
        userTypeComboBox.setBounds(100, 100, 150, 30);
        okButton.setBounds(100, 300, 100, 30);
        userSubject.setBounds(100,150,150,30);
    }

    private void addComponentsToContainer()
    {
        container.add(userTextField);
        container.add(userTypeComboBox);
        container.add(userTypeLabel);
        container.add(okButton);
    }

    private void setLayoutManager() {
        container.setLayout(null);
    }

    private void addActionEvent() {
        okButton.addActionListener(e -> {
            String userText;
            userText = userTextField.getText();
            String tp = (String) userTypeComboBox.getSelectedItem();
            String subj = "";
            if ( tp.equals("Subject") )
                subj = (String) userSubject.getSelectedItem();
            System.out.println(tp + userText + '\n');
            CompanyPrintPanel c1 = new CompanyPrintPanel();
            c1.searchData(userText,tp,subj);
        });
        userTypeComboBox.addActionListener(e -> {
            String s = (String) userTypeComboBox.getSelectedItem();
            if ( s.equals("Subject") )
                container.add(userSubject);
            else
                container.remove(userSubject);
        });
    }

}
