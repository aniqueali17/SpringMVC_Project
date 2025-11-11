package ca.syst4806proj;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SurveyRestController {

    private final SurveyRepository surveyRepo;
    private final TextQRepository textQRepo;
    private final UserRepository userRepo;

    public SurveyRestController(SurveyRepository surveyRepo,
                                TextQRepository textQRepo,
                                UserRepository userRepo) {
        this.surveyRepo = surveyRepo;
        this.textQRepo = textQRepo;
        this.userRepo = userRepo;
    }

    // Create a survey bound to an existing user (owner)
    // POST /api/surveys  { "title": "TA Feedback", "ownerId": 1 }
    @PostMapping("/surveys")
    public ResponseEntity<?> createSurvey(@RequestBody Map<String, Object> body) {
        String title = String.valueOf(body.getOrDefault("title", "Untitled Survey"));
        Object ownerIdObj = body.get("ownerId");
        if (ownerIdObj == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "ownerId is required"));
        }
        Long ownerId = Long.valueOf(ownerIdObj.toString());
        User owner = userRepo.findById(ownerId).orElse(null);
        if (owner == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "owner not found"));
        }

        Survey s = new Survey();
        s.setTitle(title);
        s.setOwner(owner);
        return ResponseEntity.ok(surveyRepo.save(s));
    }

    // GET /api/surveys
    @GetMapping(value = "/surveys", produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Survey> listSurveys() {
        return surveyRepo.findAll();
    }

    // GET /api/surveys/{id}
    @GetMapping(value = "/surveys/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Survey> getSurvey(@PathVariable Long id) {
        return surveyRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping("/surveys/{surveyId}/textq")
    public ResponseEntity<?> addTextQ(@PathVariable Long surveyId, @RequestBody Map<String, Object> body) {
        String prompt = (String) body.get("prompt");
        if (prompt == null || prompt.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "prompt is required"));
        }

        return surveyRepo.findById(surveyId).map(survey -> {
            Integer ord = (body.get("ordinalIndex") == null)
                    ? null
                    : Integer.valueOf(body.get("ordinalIndex").toString());

            TextQ q = new TextQ();
            q.setPrompt(prompt.trim());
            q.setOrdinalIndex(ord);
            survey.addTextQ(q);
            surveyRepo.save(survey);
            return ResponseEntity.ok(q);
        }).orElse(ResponseEntity.notFound().build());
    }


    // (Optional) fetch a single TextQ
    // GET /api/textq/{qid}
    @GetMapping("/textq/{qid}")
    public ResponseEntity<?> getTextQ(@PathVariable Long qid) {
        return textQRepo.findById(qid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
