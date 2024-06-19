package feature;

import java.security.SecureRandom;

public class RandomLogin {
    static String symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
    static SecureRandom rnd = new SecureRandom();

    public String randomText(int len){
        StringBuilder login = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            login.append(symbols.charAt(rnd.nextInt(symbols.length())));
        }
        return login.toString();
    }
}
