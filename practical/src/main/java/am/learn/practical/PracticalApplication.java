package am.learn.practical;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PracticalApplication {

    public static void main(String[] args) {
        SpringApplication.run(PracticalApplication.class, args);
    }
}
