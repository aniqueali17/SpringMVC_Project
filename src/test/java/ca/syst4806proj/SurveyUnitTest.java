package ca.syst4806proj;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class SurveyUnitTest {

    private Survey survey;
    private User user;

    @BeforeEach
    void setUp() {
        survey = new Survey();
        user = new User("TestUser");
        survey.setTitle("Feedback Form");
        survey.setOwner(user);
    }

    @Test
    void testSurveyProperties() {
        assertThat(survey.getTitle()).isEqualTo("Feedback Form");
        assertThat(survey.getOwner()).isEqualTo(user);
        assertThat(survey.getOwnerId()).isNull(); // because user not persisted
    }

    @Test
    void testAddAndRemoveTextQ() {
        TextQ q1 = new TextQ();
        q1.setPrompt("Question 1?");
        q1.setOrdinalIndex(1);

        survey.addTextQ(q1);
        assertThat(survey.getTextQuestions()).contains(q1);
        assertThat(q1.getSurvey()).isEqualTo(survey);

        survey.removeTextQ(q1);
        assertThat(survey.getTextQuestions()).doesNotContain(q1);
        assertThat(q1.getSurvey()).isNull();
    }
}
