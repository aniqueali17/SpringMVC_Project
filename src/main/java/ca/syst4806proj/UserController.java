package ca.syst4806proj;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "users";
    }

    @GetMapping("/{id}")
    public String getUser(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElseThrow();
        model.addAttribute("user", user);
        return "user-detail";
    }

    @PostMapping("/create")
    public String createUser(@RequestParam String name, Model model) {
        User user = new User(name);

        userRepository.save(user);

        model.addAttribute("user", user);
        return "redirect:/users/" + user.getId();
    }

    @PostMapping("/{id}/remove")
    public String removeUser(@PathVariable Long id, RedirectAttributes ra) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            ra.addFlashAttribute("message", "Removed user #" + id);
        } else {
            ra.addFlashAttribute("message", "User #" + id + " not found");
        }
        return "redirect:/users";
    }
}
