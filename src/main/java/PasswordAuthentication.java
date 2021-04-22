import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class PasswordAuthentication {
    private final int cost;

    public PasswordAuthentication() {
       cost = 16;
    }

    /**
     * This method uses the PBKDF2 algorithm for hashing a user's password
     * @param pass_plaintext The plaintext of the password introduced by the user
     * @return On success, a byte array is returned with the hashed password, otherwise null is returned
     */
    public String hashPassword(String pass_plaintext) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[cost];
        random.nextBytes(salt);

        KeySpec spec = new PBEKeySpec(pass_plaintext.toCharArray(), salt, 1000, 128);

        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            byte[] hash = factory.generateSecret(spec).getEncoded();

            Base64.Encoder enc_salt = Base64.getUrlEncoder().withoutPadding();
            Base64.Encoder enc_hash = Base64.getUrlEncoder().withoutPadding();
            return 1000 + ":" + enc_salt.encodeToString(salt) + ":" + enc_hash.encodeToString(hash);

        } catch(NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        // if it gets till here, something went wrong
        return null;
    }


    public boolean checkPassword(String pass_plaintext, String hashedPassword) {
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
}
