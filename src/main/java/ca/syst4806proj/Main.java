package ca.syst4806proj;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    // JSON - Hibernate proxy support (fixes /api/surveys serialization)
    @Bean
    public Module hibernateModule() {
        Hibernate6Module m = new Hibernate6Module();
        // don’t auto-fetch lazy relations just for JSON
        m.enable(Hibernate6Module.Feature.FORCE_LAZY_LOADING);
        // Optional: if a lazy relation isn’t loaded, serialize only its id
        // m.enable(Hibernate6Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS);
        return m;
    }

    // Seed users (kept exactly as you want, with a small guard to avoid duplicates)
    @Bean
    public CommandLineRunner demo(UserRepository userRepo) {
        return args -> {
            if (userRepo.count() == 0) { // prevents re-seeding on every restart
                userRepo.save(new User("admin", "adminpassword", User.UserType.ADMIN));
                userRepo.save(new User("Two"));
                userRepo.save(new User("Three"));
                userRepo.save(new User("Four"));
            }
        };
    }
}
