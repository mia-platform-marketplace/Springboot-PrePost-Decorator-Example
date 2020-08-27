package eu.miaplatform.customplugin.springboot.controllers.Decorators;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import eu.miaplatform.customplugin.ServiceClientFactory;
import eu.miaplatform.customplugin.springboot.DecoratorUtils;
import eu.miaplatform.customplugin.springboot.models.Message;
import eu.miaplatform.decorators.DecoratorResponse;
import eu.miaplatform.decorators.DecoratorResponseFactory;
import eu.miaplatform.decorators.postdecorators.PostDecoratorRequest;
import eu.miaplatform.service.InitServiceOptions;
import eu.miaplatform.service.Protocol;
import eu.miaplatform.service.Service;
import eu.miaplatform.service.environment.EnvConfiguration;
import io.swagger.annotations.ApiOperation;
import okhttp3.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/post")
public class PostDecorator {
    @PostMapping("/notify")
    @ApiOperation(value = "Notify user on Slack")
    @ResponseBody
    public ResponseEntity<Serializable> notify(@RequestBody PostDecoratorRequest request) throws JsonProcessingException {
        final String serviceName = EnvConfiguration.getInstance().get("SERVICE_NAME");
        Message body = getBodyFromRequest(request);
        String mymsg = body.getMymsg();
        String who = body.getWho();
        InitServiceOptions initServiceOptions = new InitServiceOptions(3001, Protocol.HTTP, Collections.emptyMap(), null);
        Service service = ServiceClientFactory.getDirectServiceProxy(serviceName, initServiceOptions);
        Response slackResponse = getSlackResponse(service, who, mymsg);
        if (slackResponse != null) {
            System.out.println("Slack service response: " + slackResponse.code());
        }
        PostDecoratorRequest unmodifiedRequest = request.leaveOriginalResponseUnmodified();
        DecoratorResponse decoratorResponse = DecoratorResponseFactory.makePostDecoratorResponse(unmodifiedRequest);
        return DecoratorUtils.getResponseEntityFromDecoratorResponse(decoratorResponse);
    }

    public Message getBodyFromRequest(PostDecoratorRequest request) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        Gson gson = new Gson();
        String jsonRequestBody = gson.toJson(request.getOriginalRequestBody(), LinkedHashMap.class);
        return objectMapper.readValue(jsonRequestBody, Message.class);
    }

    public Response getSlackResponse(Service service, String who, String mymsg) {
        Response response;
        try {
            response = service.post("/notify-slack", "{ text: " + who + " says: " + mymsg + "}", null, null);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return response;
    }
}
