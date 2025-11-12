package ca.syst4806proj;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SurveyTextQPersistenceTest {

    @Autowired private TestEntityManager em;
    @Autowired private UserRepository userRepository;
    @Autowired private SurveyRepository surveyRepository;
    @Autowired private TextQRepository textQRepository;

    @Test
    void savingSurveyCascadesAndOrdersTextQByOrdinalIndex() {
        User owner = userRepository.save(new User("Owner 1"));

        Survey s = new Survey();
        s.setTitle("Survey Cascade Test");
        s.setOwner(owner);

        TextQ q1 = new TextQ(); q1.setPrompt("Q1"); q1.setOrdinalIndex(2);
        TextQ q2 = new TextQ(); q2.setPrompt("Q2"); q2.setOrdinalIndex(1);
        TextQ q3 = new TextQ(); q3.setPrompt("Q3"); q3.setOrdinalIndex(null);

        s.addTextQ(q1);
        s.addTextQ(q2);
        s.addTextQ(q3);

        surveyRepository.save(s);
        em.flush();
        em.clear();

        Survey reloaded = surveyRepository.findById(s.getId()).orElseThrow();
        List<TextQ> qs = reloaded.getTextQuestions();

        assertThat(qs).hasSize(3);
        assertThat(qs.get(0).getOrdinalIndex()).isEqualTo(1);
        assertThat(qs.get(1).getOrdinalIndex()).isEqualTo(2);
        assertThat(qs.get(2).getOrdinalIndex()).isNull();
        assertThat(textQRepository.count()).isEqualTo(3);
    }

    @Test
    void orphanRemovalDeletesTextQWhenRemovedFromSurvey() {
        User owner = userRepository.save(new User("Owner 2"));

        Survey s = new Survey();
        s.setTitle("Orphan Removal Test");
        s.setOwner(owner);

        TextQ q = new TextQ(); q.setPrompt("Will be removed");
        s.addTextQ(q);

        surveyRepository.save(s);
        em.flush();
        em.clear();

        Long qId = textQRepository.findAll().iterator().next().getId();
        assertThat(textQRepository.findById(qId)).isPresent();

        Survey loaded = surveyRepository.findById(s.getId()).orElseThrow();
        TextQ toRemove = loaded.getTextQuestions().get(0);
        loaded.removeTextQ(toRemove);
        surveyRepository.save(loaded);

        em.flush();
        em.clear();

        assertThat(textQRepository.findById(qId)).isNotPresent();
    }
}
