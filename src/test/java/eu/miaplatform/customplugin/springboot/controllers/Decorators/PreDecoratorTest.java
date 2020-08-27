package eu.miaplatform.customplugin.springboot.controllers.Decorators;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = PreDecorator.class)
public class PreDecoratorTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void providedUserIdTest() throws Exception {
       mvc.perform(MockMvcRequestBuilders
       .post("/pre/checkwho")
       .contentType("application/json")
       .characterEncoding("utf-8")
       .content("{\"body\":{\"who\":\"foo\",\"mymsg\":\"bar\"}}"))
               .andExpect(status().isOk())
               .andExpect(content().json("{\"body\":{\"who\":\"foo\",\"mymsg\":\"bar\"}}"));
    }

    @Test
    public void defaultUserIdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post("/pre/checkwho")
                .contentType("application/json")
                .characterEncoding("utf-8")
                .content("{\"body\":{\"mymsg\":\"bar\"}}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"body\":{\"who\":\"John Doe\",\"mymsg\":\"bar\"}}"));
    }
}
