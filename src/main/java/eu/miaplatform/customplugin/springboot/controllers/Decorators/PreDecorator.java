package eu.miaplatform.customplugin.springboot.controllers.Decorators;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import eu.miaplatform.customplugin.springboot.DecoratorUtils;
import eu.miaplatform.customplugin.springboot.models.Message;
import eu.miaplatform.decorators.DecoratorResponse;
import eu.miaplatform.decorators.DecoratorResponseFactory;
import eu.miaplatform.decorators.predecorators.PreDecoratorRequest;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/pre")
public class PreDecorator {
    @PostMapping("/checkwho")
    @ApiOperation(value = "Change user name")
    @ResponseBody
    public ResponseEntity<Serializable> checkWho(@RequestBody PreDecoratorRequest request) throws JsonProcessingException {
        final String defaultWho = "John Doe";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        Gson gson = new Gson();
        String jsonRequestBody = gson.toJson(request.getOriginalRequestBody(), LinkedHashMap.class);
        Message originalBody = objectMapper.readValue(jsonRequestBody, Message.class);
        String newWho = originalBody.getWho();
        if (newWho == null) {
            newWho = ((request.getOriginalRequestHeaders() == null) || (request.getUserId() == null)) ? defaultWho
                    : request.getUserId();
        }
        Message newBody = new Message(newWho, originalBody.getMymsg());
        PreDecoratorRequest updatedRequest = request.changeOriginalRequest()
                .setBody(newBody)
                .build();
        DecoratorResponse response = DecoratorResponseFactory.makePreDecoratorResponse(updatedRequest);
        return DecoratorUtils.getResponseEntityFromDecoratorResponse(response);
    }
}
