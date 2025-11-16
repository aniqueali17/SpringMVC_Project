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
    private final TextQRepository textQRepo;
    private final TextARepository textARepo;

    public SurveyController(SurveyRepository surveyRepo, UserRepository userRepo, TextQRepository textQRepo, TextARepository textARepo) {
        this.surveyRepo = surveyRepo;
        this.userRepo = userRepo;
        this.textQRepo = textQRepo;
        this.textARepo = textARepo;
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

    //Create text question answer
    @GetMapping("surveys/{surveyID}/textQs/{textQID}/createAnswer")
    public String createTextQAnswer(@PathVariable("surveyID") Long surveyID, @PathVariable("textQID") Long textQID, Model model) {
        Survey s = surveyRepo.findById(surveyID).get();
        TextQ tq = textQRepo.findById(textQID).get();
        TextA ta = new TextA();
        ta.setQuestion(tq);
        //expose model data for rendering on the client side/ post and get requests
        model.addAttribute("survey", s);
        model.addAttribute("textQ", tq);
        model.addAttribute("textA", ta);
        model.addAttribute("surveyID", surveyID);
        model.addAttribute("textQID", textQID);

        //return html file for rendering
        return "textAnswer-create";
    }

    //Save text question answer
    @PostMapping("/saveTextAnswer")
    public String saveTextAnswer(TextA ta, @RequestParam Long surveyID, @RequestParam Long textQID, Model model) {
        Survey s = surveyRepo.findById(surveyID).get();
        TextQ tq = textQRepo.findById(textQID).get();

        tq.addTextA(ta);
        ta.setQuestion(tq);

        surveyRepo.save(s);
        textQRepo.save(tq);
        textARepo.save(ta);

        return "redirect:/surveys/" + surveyID;
    }

    //View text answers
    @GetMapping("textQs/{textQID}/viewAnswers")
    public String viewTextQAnswers(@PathVariable Long textQID, Model model) {
        TextQ tq = textQRepo.findById(textQID).get();
        List<TextA> answers = tq.getTextA();

        model.addAttribute("answers", answers);
        model.addAttribute("textQ", tq);

        return "textAnswer-view";
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

}
