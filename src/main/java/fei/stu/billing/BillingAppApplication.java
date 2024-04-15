package fei.stu.billing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BillingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingAppApplication.class, args);
    }

}
