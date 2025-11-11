package ca.syst4806proj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Survey {
    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;

    // Binds the survey to the creator (specific user)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id")
    @JsonIgnoreProperties({"surveys"})
    private User owner;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ordinalIndex ASC NULLS LAST, id ASC")
    @JsonIgnoreProperties({"survey"})
    private List<TextQ> textQuestions = new ArrayList<>();

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    public List<TextQ> getTextQuestions() { return textQuestions; }
    public void addTextQ(TextQ q) { q.setSurvey(this); textQuestions.add(q); }
    public void removeTextQ(TextQ q) { textQuestions.remove(q); q.setSurvey(null); }

    // (Optional) convenience for views/JSON
    @Transient
    public Long getOwnerId() { return owner != null ? owner.getId() : null; }
}
