import javax.swing.*;
import java.awt.*;

public abstract class GeneralFrame extends JFrame {
    protected final Container container = getContentPane();

    GeneralFrame() {
        this.setVisible(true);
        this.setResizable(false);
        this.setBounds(10,10,370,600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayoutManager();
    }

    private void setLayoutManager() {
        container.setLayout(null);
    }

    protected abstract void setLocationAndSize();
    protected abstract void addComponentsToContainer();
    protected abstract void addActionEvent();
}
