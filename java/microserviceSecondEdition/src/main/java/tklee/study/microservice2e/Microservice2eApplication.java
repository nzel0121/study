package tklee.study.microservice2e;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tklee.study.microservice2e.Customer.Customer;
import tklee.study.microservice2e.Customer.CustomerRepository;

@EnableSwagger2
@SpringBootApplication
public class Microservice2eApplication {

    public static void main(String[] args) {
        SpringApplication.run(Microservice2eApplication.class, args);
    }

    @Bean
    CommandLineRunner init(CustomerRepository customerRepository) {
        return (evt) -> {
            customerRepository.save(new Customer("Adam", "adam@boot.com"));
            customerRepository.save(new Customer("John", "john@boot.com"));
            customerRepository.save(new Customer("Smith","smith@boot.com"));
            customerRepository.save(new Customer("Edgar","edgar@boot.com"));
            customerRepository.save(new Customer("Martin","martin@boot.com"));
            customerRepository.save(new Customer("Tom","tom@boot.com"));
            customerRepository.save(new Customer("Sean","sean@boot.com"));
        };
    }

}
