package ca.syst4806proj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

@Controller
public class SurveyController {

    private final SurveyRepository surveyRepo;
    private final UserRepository userRepo;
    private final TextQRepository textQRepo;
    private final TextARepository textARepo;
    private final RangeQRepository rangeQRepo;
    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private TextARepository textARepository;

    @Autowired
    private TextQRepository textQRepository;

    public SurveyController(SurveyRepository surveyRepo, UserRepository userRepo, TextQRepository textQRepo, TextARepository textARepo, RangeQRepository rangeQRepo) {
        this.surveyRepo = surveyRepo;
        this.userRepo = userRepo;
        this.textQRepo = textQRepo;
        this.textARepo = textARepo;
        this.rangeQRepo = rangeQRepo;
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

    @GetMapping("/surveys/{surveyId}/results")
    public String viewSurveyResults(@PathVariable Long surveyId, Model model) {
        Survey survey = surveyRepository.findById(surveyId).orElse(null);
        if (survey == null) {
            return "redirect:/surveys";
        }

        model.addAttribute("survey", survey);

        // collect answers per text question
        Map<Long, List<TextA>> answerMap = new HashMap<>();
        for (TextQ q : survey.getTextQuestions()) {
            List<TextA> answers = textARepository.findByTextQId(q.getId());
            answerMap.put(q.getId(), answers);
        }

        model.addAttribute("answerMap", answerMap);

        return "survey-results";
    }

    @PostMapping("/survey/submit")
    public String submitSurvey(
            @RequestParam("surveyId") Long surveyId,
            @RequestParam("questions") List<String> questions,
            @RequestParam("answers") List<String> answers) {

        Survey survey = surveyRepository.findById(surveyId).orElse(null);
        if (survey == null) {
            return "redirect:/surveys";
        }

        List<TextQ> textQs = survey.getTextQuestions();

        for (int i = 0; i < textQs.size(); i++) {
            TextQ q = textQs.get(i);
            String ans = answers.get(i);

            TextA newAnswer = new TextA();
            newAnswer.setAnswer(ans);
            newAnswer.setQuestion(q);   // correct

            textARepository.save(newAnswer);
        }

        return "redirect:/surveys/" + surveyId + "/results";
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
        model.addAttribute("textQ", q);
        return "redirect:/surveys/" + id;
    }

    // Delete text question
    @PostMapping("/textq/{id}/delete")
    public String deleteTextQ(@PathVariable Long id,
                              @RequestParam("surveyId") Long surveyId,
                              RedirectAttributes redirectAttributes) {

        textQRepo.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Text question deleted.");
        return "redirect:/surveys/" + surveyId;
    }

    //Create text question answer
    @GetMapping("/surveys/{surveyID}/textQs/{textQID}/createAnswer")
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
    public String saveTextAnswer(TextA ta, @RequestParam("surveyId") Long surveyID, @RequestParam("textQId") Long textQID, Model model) {
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
    @GetMapping("/textQs/{textQID}/viewAnswers")
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


    @GetMapping("/surveys/{id}/fill")
    public String fillSurvey(@PathVariable Long id, Model model) {
        Survey s = surveyRepo.findById(id).orElse(null);
        if (s == null) return "redirect:/surveys";

        model.addAttribute("survey", s);
        return "survey-fill";
    }


    // Create rangeq
    @PostMapping("/surveys/{id}/rangeq/create")
    public String addRangeQ(@PathVariable Long id,
                            @RequestParam String prompt,
                            @RequestParam Integer minValue,
                            @RequestParam Integer maxValue,
                            @RequestParam(required = false) Integer ordinalIndex,
                            RedirectAttributes redirectAttributes) {

        Optional<Survey> opt = surveyRepo.findById(id);
        if (opt.isEmpty()) {
            return "redirect:/surveys";
        }
        Survey s = opt.get();

        // basic validation
        if (prompt == null || prompt.isBlank()) {
            redirectAttributes.addFlashAttribute("message", "Prompt is required.");
            return "redirect:/surveys/" + id;
        }
        if (minValue == null || maxValue == null || minValue > maxValue) {
            redirectAttributes.addFlashAttribute("message", "Invalid range (min/max).");
            return "redirect:/surveys/" + id;
        }

        RangeQ q = new RangeQ();
        q.setPrompt(prompt.trim());
        q.setMinValue(minValue);
        q.setMaxValue(maxValue);
        q.setOrdinalIndex(ordinalIndex);
        q.setSurvey(s);      // owning side

        rangeQRepo.save(q);
        // optionally keep bidirectional list in sync:
        // s.addRangeQ(q);

        redirectAttributes.addFlashAttribute("message", "Range question added.");
        return "redirect:/surveys/" + id;
    }

    // Delete RangeQ question
    @PostMapping("/rangeq/{id}/delete")
    public String deleteRangeQ(@PathVariable Long id,
                               @RequestParam("surveyId") Long surveyId,
                               RedirectAttributes redirectAttributes) {

        rangeQRepo.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Range question deleted.");
        return "redirect:/surveys/" + surveyId;
    }

}
