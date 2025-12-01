package ca.syst4806proj;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class RangeQTest {
    RangeQ q;

    @BeforeEach
    void setUp() {
        q = new RangeQ();
        RangeA rangeAnswerOne = new RangeA();
        List<RangeA> answerList = new ArrayList<>();
        rangeAnswerOne.setAnswer(7);
        answerList.add(rangeAnswerOne);
        q.setMinValue(5);
        q.setMaxValue(10);
        q.setStepValue(2);
        q.setPrompt("Does this work?");
    }

    @Test
    void getId() {
        Assertions.assertNull(q.getId());
    }



    @Test
    void getQuestionText() {
        Assertions.assertNotNull(q.getPrompt());
        Assertions.assertEquals("Does this work?", q.getPrompt());
    }

    @Test
    void setQuestionText() {
        q.setPrompt("Test Text?");
        Assertions.assertEquals("Test Text?", q.getPrompt());
    }

    @Test
    void getMinimum() {
        Assertions.assertNotNull(q.getMinValue());
        Assertions.assertEquals(5, q.getMinValue());
    }

    @Test
    void setMinimum() {
        q.setMinValue(7);
        Assertions.assertEquals(7, q.getMinValue());
    }

    @Test
    void getMaximum() {
        Assertions.assertNotNull(q.getMaxValue());
        Assertions.assertEquals(10, q.getMaxValue());
    }

    @Test
    void setMaximum() {
        q.setMaxValue(17);
        Assertions.assertEquals(17, q.getMaxValue());
    }

    @Test
    void getStep() {
        Assertions.assertNotNull(q.getStepValue());
        Assertions.assertEquals(2, q.getStepValue());
    }

    @Test
    void setStep() {
        q.setStepValue(4);
        Assertions.assertEquals(4, q.getStepValue());
    }

}