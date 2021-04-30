import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import javax.swing.*;

class CompanyPrintPanel extends JFrame {

    private final String databasePath = "C:/Users/Liviu/Desktop/JAVA/Projectululu/studentDetails.ndjson";
    private Vector<String> v = new Vector<>();
    //private final Container container = getContentPane();
    private JPanel panel = new JPanel();



    CompanyPrintPanel() {
        this.setTitle("Company Frame");
        this.setVisible(true);
        this.setBounds(10, 10, 370, 600);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


    }

    private void display() {
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        for ( int i = 0 ; i < v.size() ; i++ )
        {
            JLabel label=new JLabel();
            label.setText(v.get(i)+"\n");
            label.setBounds(15,15+i*20,100,10);
            panel.add(label);
            panel.repaint();
            panel.revalidate();
        }
        JScrollPane jScrollPane = new JScrollPane(panel);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.getContentPane().add(jScrollPane);
    }
    /**
     * This method search in the data base for all students that meet certain conditions
     * and prints the result on another frame
     * @param userData data introduced by the user
     * @param searchingType type of search
     * @param subj "" if we searching by the average grade or a subject otherwise
     */
    public void searchData(String userData,String searchingType,String subj)
    {
        double grade = Double.parseDouble(userData);

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
                if ( searchingType.equals("Subject") ) {
                    String objectSearch = (String) obj.get(srch);
                    if ( objectSearch == null )
                        continue;
                    double gr = Double.parseDouble(objectSearch);
                    if (gr >= grade) {

                        v.add(obj.get("name") + " " + srch + " " + objectSearch);
                    }
                }
                else
                {
                    double gr=0;
                    int nr_subjects=0;
                    for (Object key : obj.keySet()) {
                        if(key.equals("name") || key.equals("surname") || key.equals("status") || key.equals("telephone"))
                            continue;
                        gr = gr + Double.parseDouble((String)obj.get(key));
                        nr_subjects++;
                    }
                    if ( gr/nr_subjects >= grade )
                        v.add(obj.get("name") + " " + gr/nr_subjects );
                }
            }
            display();

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

}