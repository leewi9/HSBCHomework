package org.example;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Utils {

    public static boolean checkToken(String token) throws UnsupportedEncodingException {
        String[] lst = token.split("\\$");
        byte[] base64decodedBytes2 = Base64.getDecoder().decode(lst[1]);
        Long timestamp = Long.parseLong(new String(base64decodedBytes2, "utf-8"));
        // check if token expired
        Long elapse = System.currentTimeMillis() - timestamp;
        if (elapse > 2 * 60 * 60 * 1000L) {
            return false;
        } else {
            return true;
        }
    }
}
