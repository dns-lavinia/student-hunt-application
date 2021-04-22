import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SingInFrame frame = new SingInFrame();

        PasswordAuthentication pass = new PasswordAuthentication();

        //----- this part should be deleted afterwards, it s just temporary for testing purposes
        String hs = pass.hashPassword("12345");
        boolean b = pass.checkPassword("12345", hs);

        if(b == true)
            System.out.println("Correct password");
        else
            System.out.println("incorrect pass");
    }
}
