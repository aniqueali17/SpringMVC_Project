package ca.syst4806proj;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SurveyController {

    private final UserRepository userRepository;

    public SurveyController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


}
