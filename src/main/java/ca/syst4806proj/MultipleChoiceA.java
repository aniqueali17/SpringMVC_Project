package ca.syst4806proj;

import jakarta.persistence.*;

@Entity
public class MultipleChoiceA {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    // The option text/value that was selected
    @Column(nullable = false)
    private String selectedOption;

    // Link back to the question this answer belongs to
    @ManyToOne(fetch = FetchType.EAGER)
    private MultipleChoiceQ question;

    // ----- constructors -----

    public MultipleChoiceA() {
    }

    public MultipleChoiceA(String selectedOption, MultipleChoiceQ question) {
        this.selectedOption = selectedOption;
        this.question = question;
    }

    // ----- getters / setters -----

    public Integer getId() {
        return id;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    public MultipleChoiceQ getQuestion() {
        return question;
    }

    public void setQuestion(MultipleChoiceQ question) {
        this.question = question;
    }
}
