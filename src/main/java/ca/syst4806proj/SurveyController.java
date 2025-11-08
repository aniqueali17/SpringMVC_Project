package ca.syst4806proj;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/surveys")
public class SurveyController {

    private final SurveyRepository repo;

    public SurveyController(SurveyRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("surveys", repo.findAll());
        model.addAttribute("surveyForm", new Survey());
        return "surveys/list";
    }

    @PostMapping
    public String create(@ModelAttribute("surveyForm") @Valid Survey form,
                         BindingResult br,
                         Model model) {
        if (br.hasErrors()) {
            model.addAttribute("surveys", repo.findAll());
            return "surveys/list";
        }
        repo.save(form);
        return "redirect:/surveys";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        Survey s = repo.findById(id).orElseThrow();
        model.addAttribute("survey", s);
        return "surveys/details";
    }

    @PostMapping("/{id}/close")
    public String close(@PathVariable Long id) {
        Survey s = repo.findById(id).orElseThrow();
        s.setClosed(true);
        repo.save(s);
        return "redirect:/surveys";
    }
}
