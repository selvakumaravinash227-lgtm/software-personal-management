package spms.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "spms")
public class SpmsApiApplication {
    public static void main(String[] args) { SpringApplication.run(SpmsApiApplication.class, args); }
}
