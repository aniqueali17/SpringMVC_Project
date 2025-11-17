package ca.syst4806proj;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class TextQ {
    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 1000)
    private String prompt;

    private Integer ordinalIndex;

    @OneToMany(mappedBy = "textQ", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TextA> textAList;
    
    @JsonIgnore // avoids lazy-serialization recursion in REST responses
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Survey survey;

    public Long getId() { return id; }
    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }
    public Integer getOrdinalIndex() { return ordinalIndex; }
    public void setOrdinalIndex(Integer ordinalIndex) { this.ordinalIndex = ordinalIndex; }
    public Survey getSurvey() { return survey; }
    public void setSurvey(Survey survey) { this.survey = survey; }
    public void addTextA(TextA textA) { this.textAList.add(textA); }
    public List<TextA> getTextA(){ return textAList; }
    
    @Transient
    public Long getSurveyId() { return survey != null ? survey.getId() : null; }
}
