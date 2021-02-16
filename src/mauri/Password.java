package mauri;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Password {

    /**
     *
     * @param salt Byte Array
     * @param pass Password
     * @return SHA256 encrypted Password
     */

    public static String genPassword(byte[] salt, String pass) {
        String hashedPass = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] bytes = md.digest(pass.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            hashedPass = sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedPass;
    }

    /**
     *
     * @return Byte Array
     */

    public static byte[] genSalt() {
            try {
                SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
                byte[] salt = new byte[16];
                sr.nextBytes(salt);
                return salt;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return null;
        }


}
