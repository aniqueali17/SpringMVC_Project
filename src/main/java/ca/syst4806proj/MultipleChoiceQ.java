package ca.syst4806proj;

import jakarta.persistence.*;

@Entity
public class MultipleChoiceQ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String prompt;

    private Integer ordinalIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    private Survey survey;

    public MultipleChoiceQ() {
    }

    public MultipleChoiceQ(String prompt) {
        this.prompt = prompt;
    }

    // ----- getters / setters -----

    public Long getId() {
        return id;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public Integer getOrdinalIndex() {
        return ordinalIndex;
    }

    public void setOrdinalIndex(Integer ordinalIndex) {
        this.ordinalIndex = ordinalIndex;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }
}
