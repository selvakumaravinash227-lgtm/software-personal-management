package edu.spms.web.security;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LegacyAwarePasswordEncoderTest {
    private final LegacyAwarePasswordEncoder encoder=new LegacyAwarePasswordEncoder();
    @Test void verifiesPreservedPbkdf2DemoAccounts(){
        assertTrue(encoder.matches("Admin@123","pbkdf2$a5At1d47w9+pIPj3AmTRGg==$oMF/qHKPWWK6Er09R/sqnAXUjk87rjMQU6CzppisoEY="));
        assertTrue(encoder.matches("Hr@123","pbkdf2$XK+l2pxBZkcHhN2yvXtvVQ==$BVwB1NYTiqK8byasxAy+bx3/MTGNAdnD3BTyto/u034="));
        assertTrue(encoder.matches("Employee@123","pbkdf2$XS6r5panUdu+th+NkBP7XA==$JkQuHVHZP3WGqZYS9RZX6dwjHSaKhQr91RLXkCn8d1Y="));
        assertFalse(encoder.matches("wrong","pbkdf2$a5At1d47w9+pIPj3AmTRGg==$oMF/qHKPWWK6Er09R/sqnAXUjk87rjMQU6CzppisoEY="));
    }
    @Test void writesBcryptHashesForNewCredentials(){String hash=encoder.encode("LongSecret!23");assertTrue(hash.startsWith("$2"));assertTrue(encoder.matches("LongSecret!23",hash));assertFalse(encoder.matches("wrong",hash));}
}
