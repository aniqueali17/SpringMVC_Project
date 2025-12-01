package ca.syst4806proj;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.ranges.Range;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MultipleChoiceQTest {
    MultipleChoiceQ choiceQnA;

    @BeforeEach
    void setUp() {
        choiceQnA = new MultipleChoiceQ("What country are you from?");
        String choiceOne = "Canada";
        String choiceTwo = "USA";
        String choiceThree = "England";
        String choiceFour = "Brazil";
        List<String> choices = new ArrayList<>();
    }

    @Test
    void getId() {
        Assertions.assertNull(choiceQnA.getId());
    }


    @Test
    void getQuestionText() {
        Assertions.assertNotNull(choiceQnA.getPrompt());
        Assertions.assertEquals("What country are you from?", choiceQnA.getPrompt());
    }

    @Test
    void setQuestionText() {
        choiceQnA.setPrompt("Test Text?");
        Assertions.assertEquals("Test Text?", choiceQnA.getPrompt());
    }





}