import javax.swing.*;
import java.awt.*;

public class AdministratorFrame extends JFrame {

    private final Container container = getContentPane();
    private final JButton addStudent = new JButton("ADD STUDENT");
    private final JButton updateStudentDetails = new JButton("UPDATE STUDENT DETAILS");

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
        addStudent.setBounds(60,150,250,30);
        updateStudentDetails.setBounds(60,200,250,30);
    }

    private void addComponentsToContainer()
    {
        container.add(addStudent);
        container.add(updateStudentDetails);
    }

    private void addActionEvent()
    {

    }

}
