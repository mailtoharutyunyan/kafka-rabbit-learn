package am.learn.realworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RealWorldApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealWorldApplication.class, args);
    }
}
