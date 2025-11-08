package ca.syst4806proj;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class SurveyControllerTest {

    @Autowired MockMvc mvc;

    @Test
    void listPageLoads() throws Exception {
        mvc.perform(get("/surveys"))
                .andExpect(status().isOk())
                .andExpect(view().name("surveys/list"));
    }
}
