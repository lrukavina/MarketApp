package security;

import java.util.Base64;

public class PasswordEncoder {

    public PasswordEncoder() {
    }

    public String decodePassword(String password){
        byte[] decodedBytes = Base64.getDecoder().decode(password);
        String decodedString = new String(decodedBytes);
        return decodedString;
    }

    public static String encodePassword(String password){
        String encodedPassword = Base64.getEncoder().encodeToString(password.getBytes());
        return encodedPassword;
    }
}
