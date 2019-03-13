package tklee.study.microservice2e_reciever;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class Microservice2eRecieverApplication {

    public static void main(String[] args) {
        SpringApplication.run(Microservice2eRecieverApplication.class, args);
    }

}

@Component
class Receiver{
    @Autowired
    Mailer mailer;

    @Bean
    Queue queue(){
        return new Queue("CustomerQ", false);
    }

    @RabbitListener(queues = "CustomerQ")
    public void processMessage(String email) {
        System.out.println(email);
        mailer.sendMail(email);
    }
}

@Component
class Mailer{

    @Autowired
    private JavaMailSender javaMailService;

    public Mailer() {
    }

    public void sendMail(String email) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Registration");
        mailMessage.setText("Successfully Registered");
        javaMailService.send(mailMessage);
    }
}
