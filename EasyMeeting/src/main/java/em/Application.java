package em;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

//@EnableConfigurationProperties(User.class)

public class Application{
    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }
}
