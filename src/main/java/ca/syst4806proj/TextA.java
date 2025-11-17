package ca.syst4806proj;

import jakarta.persistence.*;

@Entity
public class TextA {
    public String answer;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    public TextQ textQ;

    public TextA() {}

    public TextA(String answer) {
        this.answer = answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public TextQ getQuestion() {
        return textQ;
    }

    public void setQuestion(TextQ question) {
        this.textQ = question;
    }
}
