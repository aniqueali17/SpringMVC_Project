package ca.syst4806proj;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.hibernate.StaleObjectStateException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
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
        try {
            userRepository.deleteById(id);     // single call
            ra.addFlashAttribute("message", "Removed user #" + id);
        } catch (EmptyResultDataAccessException
                 | ObjectOptimisticLockingFailureException
                 | StaleObjectStateException e) {
            ra.addFlashAttribute("message", "User #" + id + " not found or already removed");
        }
        return "redirect:/users";
    }
}
