package ca.syst4806proj;

import jakarta.persistence.*;
import org.hibernate.usertype.UserType;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String password;
    private UserType type;


    public enum UserType {  ADMIN, STANDARD}

    public User() {}

    public User(String name, String password, UserType type) {
        this.name = name;
        this.type = type;
        this.password = password;
    }

    public User(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }
}
