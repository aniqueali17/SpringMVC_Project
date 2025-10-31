package ca.syst4806proj;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner demo(UserRepository userRepo) {
        return (args) -> {
            // Create users by default
            User user1 = new User("One");
            User user2 = new User("Two");
            User user3 = new User("Three");
            User user4= new User("Four");


            // Save
            userRepo.save(user1);
            userRepo.save(user2);
            userRepo.save(user3);
            userRepo.save(user4);
        };
    }
}