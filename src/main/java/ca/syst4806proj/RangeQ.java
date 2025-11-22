package ca.syst4806proj;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;



@Entity
public class RangeQ {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 1000)
    private String prompt;

    private Integer ordinalIndex;

    @Column(nullable = false)
    private Integer minValue;

    @Column(nullable = false)
    private Integer maxValue;

    private Integer stepValue;

    // Relationships

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Survey survey;

    // Getters and Setter

    public Long getId(){
        return id;
    }
    public String getPrompt(){
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

    public Integer getMinValue() {
        return minValue;
    }

    public void setMinValue(Integer minValue) {
        this.minValue = minValue;
    }

    public Integer getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Integer maxValue) {
        this.maxValue = maxValue;
    }

    public Integer getStepValue() {
        return stepValue;
    }

    public void setStepValue(Integer stepValue) {
        this.stepValue = stepValue;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    // convenience for views / JSON
    @Transient
    public Long getSurveyId() {
        return survey != null ? survey.getId() : null;
    }
}