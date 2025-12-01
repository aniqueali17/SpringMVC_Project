package ca.syst4806proj;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class SurveyController {

    private final SurveyRepository surveyRepo;
    private final UserRepository userRepo;
    private final TextQRepository textQRepo;
    private final TextARepository textARepo;
    private final RangeQRepository rangeQRepo;
    private final MultipleChoiceQRepository multipleChoiceQRepo;
    private final MultipleChoiceARepository multipleChoiceARepo;


    public SurveyController(SurveyRepository surveyRepo,
                            UserRepository userRepo,
                            TextQRepository textQRepo,
                            TextARepository textARepo,
                            RangeQRepository rangeQRepo,
                            MultipleChoiceQRepository multipleChoiceQRepo,
                            MultipleChoiceARepository multipleChoiceARepo) {
        this.surveyRepo = surveyRepo;
        this.userRepo = userRepo;
        this.textQRepo = textQRepo;
        this.textARepo = textARepo;
        this.rangeQRepo = rangeQRepo;
        this.multipleChoiceQRepo = multipleChoiceQRepo;
        this.multipleChoiceARepo = multipleChoiceARepo;
    }


    @GetMapping("/logout")
    public String userLogout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();
        }
        return "redirect:/surveys";
    }

    @PostMapping("/login")
    public String userLogin(HttpServletRequest request,
                            Model model,
                            @RequestParam("username") String username,
                            @RequestParam("password") String password) {
        System.out.println("Login attempt: " + username + " / " + password);

        List<User> users = (List<User>) userRepo.findAll();

        for (User user : users) {
            System.out.println("DB user: " + user.getName() + " / " + user.getPassword());
        }
        for(User user : users) {
            if(user.getName().equals(username) && user.getPassword().equals(password)) {
                HttpSession session = request.getSession();
                if(user.getType().equals(User.UserType.ADMIN)) {
                    session.setAttribute("userType", "ADMIN");
                } else if(user.getType().equals(User.UserType.STANDARD)) {
                    session.setAttribute("userType", "STANDARD");
                }
                return "redirect:/surveys";
            }
        }
        model.addAttribute("error", "Invalid username or password");
        return "login";
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
    public String createSurvey(HttpServletRequest request, @RequestParam String title, @RequestParam Long ownerId, Model model) {
        HttpSession session = request.getSession(false);
        if(session == null) {
            model.addAttribute("error", "Must login to access this functionality");
            return "login";
        } else if(session.getAttribute("userType") != "ADMIN") {
            model.addAttribute("error", "Must be admin to access this functionality");
            return "redirect:/surveys";
        }
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
    public String addTextQ(HttpServletRequest request, @PathVariable Long id,
                           @RequestParam String prompt,
                           @RequestParam(required = false) Integer ordinalIndex,
                           Model model) {
        HttpSession session = request.getSession(false);
        if(session == null) {
            model.addAttribute("error", "Must login to access this functionality");
            return "login";
        } else if(session.getAttribute("userType") != "ADMIN") {
            model.addAttribute("error", "Must be admin to access this functionality");
            return "redirect:/surveys";
        }
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
    public String deleteTextQ(HttpServletRequest request,@PathVariable Long id,
                              @RequestParam("surveyId") Long surveyId,
                              RedirectAttributes redirectAttributes, Model model) {
        HttpSession session = request.getSession(false);
        if(session == null) {
            model.addAttribute("error", "Must login to access this functionality");
            return "login";
        } else if(session.getAttribute("userType") != "ADMIN") {
            model.addAttribute("error", "Must be admin to access this functionality");
            return "redirect:/surveys";
        }
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

    @PostMapping("/saveMultipleChoiceAnswer")
    public String saveMultipleChoiceAnswer(HttpServletRequest request,
                                           @RequestParam("surveyId") Long surveyId,
                                           @RequestParam("mcqId") Long mcqId,
                                           @RequestParam("selectedOption") String selectedOption,
                                           Model model) {

        // Look up the question
        MultipleChoiceQ question = multipleChoiceQRepo.findById(mcqId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid MultipleChoiceQ ID: " + mcqId));

        // Create and save the answer
        MultipleChoiceA answer = new MultipleChoiceA();
        answer.setSelectedOption(selectedOption);
        answer.setQuestion(question);

        multipleChoiceARepo.save(answer);

        // if you later want a "view answers" page:
        // List<MultipleChoiceA> answers = multipleChoiceARepo.findByQuestion(question);
        // model.addAttribute("answers", answers);
        // model.addAttribute("question", question);
        // return "multipleChoiceAnswer-view";

        // For now just go back to the survey or survey detail
        return "redirect:/surveys/" + surveyId;
    }

    @PostMapping("/surveys/{id}/mcq/create")
    public String addMCQ(HttpServletRequest request,
                         @PathVariable Long id,
                         @RequestParam String prompt,
                         @RequestParam(required = false) Integer ordinalIndex,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        //Access Control Authentication
        HttpSession session = request.getSession(false);
        if(session == null) {
            model.addAttribute("error", "Must login to access this functionality");
            return "login";
        } else if(session.getAttribute("userType") != "ADMIN") {
            model.addAttribute("error", "Must be admin to access this functionality");
            return "redirect:/surveys";
        }
        Survey s = surveyRepo.findById(id).orElse(null);
        if (s == null) return "redirect:/surveys";

        if (prompt == null || prompt.isBlank()) {
            redirectAttributes.addFlashAttribute("message", "Prompt is required.");
            return "redirect:/surveys/" + id;
        }

        MultipleChoiceQ q = new MultipleChoiceQ();
        q.setPrompt(prompt.trim());
        q.setOrdinalIndex(ordinalIndex);
        q.setSurvey(s);

        multipleChoiceQRepo.save(q);

        redirectAttributes.addFlashAttribute("message", "Multiple-choice question added.");
        return "redirect:/surveys/" + id;
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
    public String removeSurvey(HttpServletRequest request,
                               @PathVariable Long id,
                               Model model) {
        //Access Control Authentication
        HttpSession session = request.getSession(false);
        if(session == null) {
            model.addAttribute("error", "Must login to access this functionality");
            return "login";
        } else if(session.getAttribute("userType") != "ADMIN") {
            model.addAttribute("error", "Must be admin to access this functionality");
            return "redirect:/surveys";
        }

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

    // Create rangeq
    @PostMapping("/surveys/{id}/rangeq/create")
    public String addRangeQ(HttpServletRequest request,
                            Model model,
                            @PathVariable Long id,
                            @RequestParam String prompt,
                            @RequestParam Integer minValue,
                            @RequestParam Integer maxValue,
                            @RequestParam(required = false) Integer ordinalIndex,
                            RedirectAttributes redirectAttributes) {

        //Access Control Authentication
        HttpSession session = request.getSession(false);
        if(session == null) {
            model.addAttribute("error", "Must login to access this functionality");
            return "login";
        } else if(session.getAttribute("userType") != "ADMIN") {
            model.addAttribute("error", "Must be admin to access this functionality");
            return "redirect:/surveys";
        }

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
    public String deleteRangeQ(HttpServletRequest request,
                               Model model,
                               @PathVariable Long id,
                               @RequestParam("surveyId") Long surveyId,
                               RedirectAttributes redirectAttributes) {

        //Access Control Authentication
        HttpSession session = request.getSession(false);
        if(session == null) {
            model.addAttribute("error", "Must login to access this functionality");
            return "login";
        } else if(session.getAttribute("userType") != "ADMIN") {
            model.addAttribute("error", "Must be admin to access this functionality");
            return "redirect:/surveys";
        }

        rangeQRepo.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Range question deleted.");
        return "redirect:/surveys/" + surveyId;
    }

}
