package ca.syst4806proj;

import jakarta.persistence.*;

@Entity
public class TextA {
    public String Answer;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne
    public TextQ question;

    public TextA() {}

    public TextA(String answer) {
        this.Answer = answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public TextQ getQuestion() {
        return question;
    }

    public void setQuestion(TextQ question) {
        this.question = question;
    }
}
