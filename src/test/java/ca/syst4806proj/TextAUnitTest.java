package ca.syst4806proj;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TextAUnitTest {

    TextA textAnswer;
    TextQ textQ;

    @BeforeEach
    void setUp() {
        textAnswer = new TextA();
        textAnswer.setAnswer("I am text!");
        textQ = new TextQ();
        textQ.setPrompt("Who am I?");
        textAnswer.setQuestion(textQ);
    }

    @Test
    void getId() {
        Assertions.assertNull(textAnswer.getId());
    }

    @Test
    void setId() {
        textAnswer.setId(2);
        Assertions.assertEquals(2, textAnswer.getId());
    }

    @Test
    void getAnswer() {
        Assertions.assertEquals("I am text!", textAnswer.getAnswer());
    }

    @Test
    void setAnswer() {
        textAnswer.setAnswer("Texting!");
        Assertions.assertEquals("Texting!", textAnswer.getAnswer());
    }

    @Test
    void getQuestion() {
        Assertions.assertEquals("Who am I?", textAnswer.getQuestion().getPrompt());
    }

    @Test
    void setQuestion() {
        TextQ textQ1 = new TextQ();
        textQ1.setPrompt("Where am I?");
        textAnswer.setQuestion(textQ1);
        Assertions.assertEquals("Where am I?", textAnswer.getQuestion().getPrompt());
    }
}