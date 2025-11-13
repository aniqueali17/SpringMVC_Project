package ca.syst4806proj;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class SurveyController {

    private final SurveyRepository surveyRepo;
    private final UserRepository userRepo;

    public SurveyController(SurveyRepository surveyRepo, UserRepository userRepo) {
        this.surveyRepo = surveyRepo;
        this.userRepo = userRepo;
    }

    // LIST page
    @GetMapping("/surveys")
    public String listSurveys(Model model) {
        model.addAttribute("surveys", surveyRepo.findAll());
        model.addAttribute("users", userRepo.findAll()); // for owner dropdown
        return "surveys";
    }

    // CREATE survey (title + ownerId)
    @PostMapping("/surveys/create")
    public String createSurvey(@RequestParam String title, @RequestParam Long ownerId, Model model) {
        Optional<User> owner = userRepo.findById(ownerId);
        if (owner.isEmpty()) {
            model.addAttribute("message", "Owner not found");
            model.addAttribute("surveys", surveyRepo.findAll());
            model.addAttribute("users", userRepo.findAll());
            return "surveys";
        }
        Survey s = new Survey();
        s.setTitle(title);
        s.setOwner(owner.get());
        surveyRepo.save(s);
        return "redirect:/surveys";
    }

    // DETAIL page
    @GetMapping("/surveys/{id}")
    public String viewSurvey(@PathVariable Long id, Model model) {
        Survey s = surveyRepo.findById(id).orElse(null);
        if (s == null) return "redirect:/surveys";
        model.addAttribute("survey", s);
        return "survey-detail";
    }

    // ADD Text Question
    @PostMapping("/surveys/{id}/textq/create")
    public String addTextQ(@PathVariable Long id,
                           @RequestParam String prompt,
                           @RequestParam(required = false) Integer ordinalIndex,
                           Model model) {
        Survey s = surveyRepo.findById(id).orElse(null);
        if (s == null) return "redirect:/surveys";
        if (prompt == null || prompt.isBlank()) {
            model.addAttribute("survey", s);
            model.addAttribute("message", "Prompt is required");
            return "redirect:/survey-detail";
        }
        TextQ q = new TextQ();
        q.setPrompt(prompt.trim());
        q.setOrdinalIndex(ordinalIndex);
        s.addTextQ(q);            // sets both sides
        surveyRepo.save(s);       // cascades
        return "redirect:/surveys/" + id;
    }

    @PostMapping("/surveys/{id}/remove")
    public String removeSurvey(@PathVariable Long id) {
        surveyRepo.deleteById(id);
        return "redirect:/surveys";
    }


    @GetMapping("/users/{ownerId}/surveys")
    public String listSurveysForOwner(@PathVariable Long ownerId, Model model) {
        if (userRepo.findById(ownerId).isEmpty()) return "redirect:/users";
        model.addAttribute("surveys", surveyRepo.findByOwner_Id(ownerId));
        model.addAttribute("users", userRepo.findAll());
        model.addAttribute("selectedOwnerId", ownerId); // preselects owner on /surveys page
        return "surveys"; // uses surveys.html
    }


    @GetMapping("/surveys/{id}/fill")
    public String fillSurvey(@PathVariable Long id, Model model) {
        Survey s = surveyRepo.findById(id).orElse(null);
        if (s == null) return "redirect:/surveys";

        model.addAttribute("survey", s);
        return "survey-fill";
    }


    @PostMapping("/survey/submit")
    public String submitSurvey(
            @RequestParam Long surveyId,
            @RequestParam(required = false) List<String> questions,
            @RequestParam(required = false) List<String> answers) {

        // For now, just print out the results
        System.out.println("Survey ID: " + surveyId);
        if (questions != null && answers != null) {
            for (int i = 0; i < questions.size(); i++) {
                String qText = questions.get(i);
                String answer = i < answers.size() ? answers.get(i) : "(no answer)";
                System.out.println("Question: " + qText);
                System.out.println("Answer: " + answer);
                System.out.println();
            }
        }

        return "redirect:/surveys"; // redirect
    }

}
