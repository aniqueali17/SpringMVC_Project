package ca.syst4806proj;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class TextQUnitTest {

    private TextQ textQ;

    @BeforeEach
    void setUp() {
        textQ = new TextQ();
        textQ.setPrompt("How are you?");
        textQ.setOrdinalIndex(5);
    }

    @Test
    void testBasicProperties() {
        assertThat(textQ.getPrompt()).isEqualTo("How are you?");
        assertThat(textQ.getOrdinalIndex()).isEqualTo(5);
    }

    @Test
    void testSurveyBinding() {
        Survey survey = new Survey();
        textQ.setSurvey(survey);
        assertThat(textQ.getSurvey()).isEqualTo(survey);
        assertThat(textQ.getSurveyId()).isNull(); // not persisted, so id null
    }
}
