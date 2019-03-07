package tklee.study.microservice2e.Customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CustomerRegister {

    CustomerRepository customerRepository;

    Sender sender;

    @Autowired
    CustomerRegister(CustomerRepository customerRepository, Sender sender){
        this.customerRepository = customerRepository;
        this.sender = sender;
    }

    public Mono<Customer> register(Customer customer){
        if(customerRepository.findByName(customer.getName()).isPresent()){
            System.out.println("Duplicate Customer. No Action required");
        }else{
            customerRepository.save(customer);
            sender.send(customer.getEmail());
        }
        return Mono.just(customer);
    }
}
