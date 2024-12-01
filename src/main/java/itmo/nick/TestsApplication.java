package itmo.nick;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "itmo.nick")
public class TestsApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestsApplication.class, args);
    }
}
