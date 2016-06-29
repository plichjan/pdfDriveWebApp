package cz.plichtanet.honza;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Jan.Plichta
 * @since 25.6.2016
 */
public class CreatePasswordHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(args[0]);
        System.out.println("hashedPassword = " + hashedPassword);
    }
}
