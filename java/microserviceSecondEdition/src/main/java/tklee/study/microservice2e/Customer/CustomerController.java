package tklee.study.microservice2e.Customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class CustomerController {

    @Autowired
    CustomerRegister customerRegister;

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    Mono<Customer> register(@RequestBody Customer customer){
        return customerRegister.register(customer);
    }
}
