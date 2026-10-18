package spms.service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/** Salted PBKDF2-HMAC-SHA256 password hashing. */
public final class PasswordService {
    private static final int ITERATIONS=210_000;
    private PasswordService() {}
    public static String hash(char[] password) {
        byte[] salt=new byte[16]; new SecureRandom().nextBytes(salt);
        return "pbkdf2$"+Base64.getEncoder().encodeToString(salt)+"$"+Base64.getEncoder().encodeToString(derive(password,salt));
    }
    public static boolean verify(char[] password,String encoded) {
        try { String[] p=encoded.split("\\$"); return p.length==3&&p[0].equals("pbkdf2")&&MessageDigest.isEqual(Base64.getDecoder().decode(p[2]),derive(password,Base64.getDecoder().decode(p[1]))); }
        catch (RuntimeException e) { return false; }
    }
    private static byte[] derive(char[] password,byte[] salt) {
        PBEKeySpec spec=new PBEKeySpec(password,salt,ITERATIONS,256);
        try { return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded(); }
        catch(Exception e){throw new IllegalStateException("Password security service is unavailable.",e);} finally {spec.clearPassword();}
    }
}
