package ca.syst4806proj;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface SurveyRepository extends CrudRepository<Survey, Long> {
    List<Survey> findByOwner_Id(Long ownerId); // <- use underscore to reach owner.id
}
