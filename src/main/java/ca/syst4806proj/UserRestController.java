package ca.syst4806proj;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserRestController {

    private final UserRepository userRepo;

    public UserRestController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // GET /api/users  → list all users as JSON
    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<User> listUsers() {
        return userRepo.findAll();
    }

    // GET /api/users/{id} → single user as JSON
    @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return userRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST /api/users  (body: {"name":"Alice"})
    @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@RequestBody User u) {
        if (u.getName() == null || u.getName().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userRepo.save(u));
    }
}