package ca.syst4806proj;

import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface TextARepository extends CrudRepository<TextA, Integer> {
    List<TextA> findByTextQId(Long textQId);
}