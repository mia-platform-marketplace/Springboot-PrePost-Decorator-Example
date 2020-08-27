package eu.miaplatform.customplugin.springboot.controllers.Decorators;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = PostDecorator.class)
public class PostDecoratorTest {
    @Autowired
    private MockMvc mvc;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(3001);

    @Test
    public void notifySlackTest() throws Exception {
        String responseBody = "{\"foo\":\"bar\"}";
        String mockedService = "/notify-slack";
        String JSONRequest = "{"+
            "\"request\":{"+
            "\"method\":null,"+
            "\"path\":null,"+
            "\"headers\":null,"+
            "\"query\":null,"+
            "\"body\":{"+
            "\"who\":\"foo\","+
            "\"mymsg\":\"bar\""+
            "}},"+
            "\"response\":{"+
            "\"body\":{"+
            "\"who\":\"foo\","+
            "\"mymsg\":\"bar\""+
            "},"+
            "\"headers\":null,"+
            "\"statusCode\":200"+
            "}}";
        stubFor(WireMock.post(urlPathEqualTo(mockedService))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(responseBody)));

        mvc.perform(MockMvcRequestBuilders
                .post("/post/notify")
                .contentType("application/json")
                .characterEncoding("utf-8")
                .content(JSONRequest))
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        verify(postRequestedFor(urlPathEqualTo(mockedService)));
    }

}
