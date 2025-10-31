package ca.syst4806proj;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("insert attribute name here", userRepository.findAll());
        return "insert html file name here";
    }

    @GetMapping("/{id}")
    public String getUser(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElseThrow();
        model.addAttribute("insert attribute name here", user);
        return "insert html file name here";
    }

    @PostMapping("/create")
    public String createUser(@RequestParam String name, Model model) {
        User user = new User(name);

        userRepository.save(user);

        model.addAttribute("insert attribute name here", user);
        return "redirect:/insert html file name here/" + user.getId();
    }

}
