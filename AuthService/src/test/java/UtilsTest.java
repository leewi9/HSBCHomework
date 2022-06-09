import org.example.Utils;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import static org.junit.Assert.assertFalse;

public class UtilsTest {

    @Test
    public void testCheckToken() throws UnsupportedEncodingException {
        String twoHoursAgo = Long.toString(System.currentTimeMillis() - (2 * 60 * 60 * 1000L));
        String token1 = Base64.getEncoder().encodeToString(("u1").getBytes("utf-8")) + "$" + Base64.getEncoder().encodeToString(twoHoursAgo.getBytes("utf-8"));
        boolean rst = Utils.checkToken(token1);
        assertFalse(rst);
    }
}