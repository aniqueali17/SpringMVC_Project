package ca.syst4806proj;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.hibernate.StaleObjectStateException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final SurveyRepository surveyRepository;   // ← add this field


    public UserController(UserRepository userRepository,
                          SurveyRepository surveyRepository) {
        this.userRepository = userRepository;
        this.surveyRepository = surveyRepository;      // ← save it
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

    @PostMapping("/{id}/remove")   // <-- no consumes, no @RequestBody
    public String removeUser(@PathVariable Long id, RedirectAttributes ra) {
        try {
            userRepository.deleteById(id);
            ra.addFlashAttribute("message", "Removed user #" + id);
        } catch (DataIntegrityViolationException e) {
            ra.addFlashAttribute("message",
                    "Cannot remove user: they own surveys. Delete or reassign those surveys first.");
        }
        return "redirect:/users";
    }

    // NOTE: do NOT make this method static
    @GetMapping("/users/{id}")
    public String viewUser(@PathVariable Long id, Model model) {
        User u = userRepository.findById(id).orElse(null);
        if (u == null) return "redirect:/users";

        model.addAttribute("user", u);

        // Call the instance, not the class; and not from a static method
        model.addAttribute("surveys", surveyRepository.findByOwner_Id(id));
        return "user-detail";
    }



}
