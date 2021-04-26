import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JLabel;

class CompanyPrintPanel extends JFrame {

    private final String databasePath = "C:/Users/Liviu/Desktop/JAVA/Projectululu/studentDetails.ndjson";
    private Vector<String> v = new Vector<>();
    Container container = getContentPane();

    CompanyPrintPanel() {
        this.setTitle("Solutiaa boss Form");
        this.setVisible(true);
        this.setResizable(false);
        this.setBounds(10, 10, 370, 600);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        container.setLayout(null);
    }

    private void display() {
        JLabel label=new JLabel();
        for ( int i = 0 ; i < v.size() ; i++ )
        {
            label.setText(v.get(i));
            label.setBounds(15+i*10,15,100,10);
            container.add(label);
        }
    }
    /**
     * This method searchs in the data base for all students that meet certain conditions
     * and prints the result on another frame
     * @param userData data introduced by the user
     * @param searchingType type of search
     * @param subj "" if we seacrhing by the average grade or a subject otherwise
     * @return -
     */
    public void searchData(String userData,String searchingType,String subj)
    {
        double grade = userData.indexOf(0)*10 + userData.indexOf(1);

        String srch;
        if ( searchingType.equals("Subject") )
            srch = subj;
        else
            srch = searchingType;
        try (FileReader reader = new FileReader(databasePath)) {
            // Read from the .json file line by line -> .ndjson style
            BufferedReader buffReader =new BufferedReader(reader);
            String line;

            while((line = buffReader.readLine()) != null) {
                Object o = new JSONParser().parse(line);
                JSONObject obj = (JSONObject) o;

                String objectSearch = (String) obj.get(srch);
                if ( objectSearch == null )
                    continue;
                double gradeObject = objectSearch.indexOf(0) * 10 + objectSearch.indexOf(1);
                if (gradeObject >= grade) {

                    v.add(obj.get("name") + " " + srch + " " + objectSearch);
                }

            }
            display();

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

}