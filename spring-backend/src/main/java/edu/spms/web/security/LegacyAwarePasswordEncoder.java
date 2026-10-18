package edu.spms.web.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.util.Base64;

/** Reads the preserved database's salted PBKDF2 hashes and writes modern BCrypt hashes for new accounts. */
public final class LegacyAwarePasswordEncoder implements PasswordEncoder {
    private final PasswordEncoder bcrypt = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder(12);
    @Override public String encode(CharSequence raw) { return bcrypt.encode(raw); }
    @Override public boolean matches(CharSequence raw, String encoded) {
        if (encoded == null) return false;
        if (encoded.startsWith("$2a$") || encoded.startsWith("$2b$") || encoded.startsWith("$2y$")) return bcrypt.matches(raw,encoded);
        try {
            String[] parts=encoded.split("\\$");
            if(parts.length!=3||!parts[0].equals("pbkdf2"))return false;
            byte[] salt=Base64.getDecoder().decode(parts[1]),expected=Base64.getDecoder().decode(parts[2]);
            PBEKeySpec spec=new PBEKeySpec(raw.toString().toCharArray(),salt,210_000,256);
            try{return MessageDigest.isEqual(expected,SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded());}
            finally{spec.clearPassword();}
        } catch(Exception ignored){return false;}
    }
}
