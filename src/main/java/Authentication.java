import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class Authentication {
    private final String databasePath = "C:/Users/Liviu/Desktop/JAVA/Projectululu/userCredentials.json";

    public Authentication() {
    }

    /**
     * This method uses the PBKDF2 algorithm for hashing a user's password
     * @param pass_plaintext The plaintext of the password introduced by the user
     * @return On success, a byte array is returned with the hashed password, otherwise null is returned
     */
    private String hashPassword(String pass_plaintext) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        KeySpec spec = new PBEKeySpec(pass_plaintext.toCharArray(), salt, 1000, 128);

        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            byte[] hash = factory.generateSecret(spec).getEncoded();

            Base64.Encoder enc= Base64.getUrlEncoder().withoutPadding();
            return 1000 + ":" + enc.encodeToString(salt) + ":" + enc.encodeToString(hash);

        } catch(NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        // if it gets till here, something went wrong
        return null;
    }
    /**
     * This method checks if a user already exists with the given data
     * @param username and password introduced by the user
     * @return True if the user was found in the database
     */
    public boolean VerifyData(String username,String password)
    {
        String pas = searchPassword(username);
        if ( pas.equals("") )
             return false;
        return checkPassword(password, pas);

    }

    private String searchPassword(String username)
    {
        try (FileReader reader = new FileReader(databasePath)) {
            // Read from the .json file line by line -> .ndjson style
            BufferedReader buffReader =new BufferedReader(reader);
            String line;

            while((line = buffReader.readLine()) != null) {
                Object o = new JSONParser().parse(line);
                JSONObject obj = (JSONObject) o;

                String objectUsername = (String) obj.get("username");

                if(username.equals(objectUsername))
                    return (String) obj.get("password");
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    private boolean checkPassword(String pass_plaintext, String hashedPassword) {
        if(hashedPassword == null)
            throw new java.lang.IllegalArgumentException("Invalid hash provided for comparison");

        String[] parts = hashedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);
        Base64.Decoder dec = Base64.getUrlDecoder();
        byte[] salt = dec.decode(parts[1]);
        byte[] hash = dec.decode(parts[2]);


        PBEKeySpec spec = new PBEKeySpec(pass_plaintext.toCharArray(), salt, iterations, hash.length * 8);

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] testHash = skf.generateSecret(spec).getEncoded();

            int diff = hash.length ^ testHash.length;
            for(int i = 0; i < hash.length && i < testHash.length; i++)
            {
                diff |= hash[i] ^ testHash[i];
            }
            return diff == 0;

        } catch(NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * This method tries to register a user in the database
     * @param chosenUser It can be a Student, Company, or Administrator
     * @param username This is the username introduced by the user
     * @param password This is the plain text password introduced by the user
     * @return Method returns true if the account was successfully created and false if the account already exists
     */
    @SuppressWarnings("unchecked")
    public boolean registerUser(String chosenUser, String username, String password) {
        // generate the hashed password using PBKDF2
        String hashedPassword = hashPassword(password);

        if(hashedPassword == null)
            return false;

        JSONObject userDetails = new JSONObject();
        userDetails.put("password", hashedPassword);
        userDetails.put("username", username);
        userDetails.put("userType", chosenUser);

        // Check if the user with the given username already exists in the database
        // If it does, return false, because the user cannot register with the same username
        if(userExists(username))
            return false;

        // Open the JSON file and search in it at first, and if the user with the same username is not found, continue
        try(FileWriter file = new FileWriter(databasePath, true)) {

            file.write(userDetails.toJSONString());
            file.write('\n');

            file.flush();
            file.close();

            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }

        // the account was created successfully, return true
        return true;
    }

    private boolean userExists(String username) {

        try (FileReader reader = new FileReader(databasePath)) {
            // Read from the .json file line by line -> .ndjson style
            BufferedReader buffReader =new BufferedReader(reader);
            String line;

            while((line = buffReader.readLine()) != null) {
                Object o = new JSONParser().parse(line);
                JSONObject obj = (JSONObject) o;

                String objectUsername = (String) obj.get("username");

                if(username.equals(objectUsername))
                    return true;
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        // if it gets till here it means that it either is an exception, or the user does not exist in the database
        return false;
    }
}
