package tklee.study.microservice2e.greet;

import org.springframework.hateoas.ResourceSupport;

public class Greet extends ResourceSupport {
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
