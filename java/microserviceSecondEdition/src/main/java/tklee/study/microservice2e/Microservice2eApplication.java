package tklee.study.microservice2e;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class Microservice2eApplication {

    public static void main(String[] args) {
        SpringApplication.run(Microservice2eApplication.class, args);
    }

}

@RestController
class GreetingController {
    @GetMapping("/")
    Greet greet(){
        return new Greet("Hello World");
    }
}

class Greet{
    private String message;

    public Greet(){}

    public Greet(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
