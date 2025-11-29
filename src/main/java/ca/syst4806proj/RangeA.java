package ca.syst4806proj;

import jakarta.persistence.*;

@Entity
public class RangeA {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    public RangeQ rangeQ;

    public int answer;

    public RangeA() {}

    public RangeA(int answer) {
        this.answer = answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public int getAnswer() {
        return answer;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public RangeQ getQuestion() {
        return rangeQ;
    }

    public void setQuestion(RangeQ question) {
        this.rangeQ = question;
    }
}
