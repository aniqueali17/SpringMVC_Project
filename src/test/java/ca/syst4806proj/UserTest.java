package ca.syst4806proj;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    User user;

    @BeforeEach
    void setUp() {
        user = new User("Justice");
    }

    @AfterEach
    void tearDown() {
        user = null;
    }

    @Test
    void setId() {
        user.setId(14L);
        Assertions.assertEquals(14L, user.getId());
    }

    @Test
    void getId() {
        assertNull(user.getId());
    }

    @Test
    void getName() {
        assertEquals("Justice", user.getName());
    }

    @Test
    void setName() {
        user.setName("Justice");
    }
}