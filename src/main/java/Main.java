import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        RegisterFrame frame = new RegisterFrame();
        frame.setTitle("Registration Form");
        frame.setVisible(true);

        //----- in Linux, setResizable works okay if it is put before setBounds
        frame.setResizable(false);

        //----- The exactly size will probably have to be set according to the Login page
        frame.setBounds(10,10,370,600);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}
