package eu.miaplatform.customplugin.springboot.Decorators;

import eu.miaplatform.customplugin.springboot.DecoratorUtils;
import eu.miaplatform.customplugin.springboot.models.Message;
import eu.miaplatform.decorators.DecoratorResponse;
import eu.miaplatform.decorators.DecoratorResponseFactory;
import eu.miaplatform.decorators.predecorators.PreDecoratorRequest;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

public class PreDecorator {
    @PostMapping("/checkwho")
    @ApiOperation(value = "Add token header")
    @ResponseBody
    public ResponseEntity checkWho(@RequestBody PreDecoratorRequest request) {
        final String defaultWho = "John Doe";
        Message originalBody = (Message) request.getOriginalRequestBody();
        String newWho = originalBody.getWho();
        if (newWho == null) {
            newWho = request.getUserId();
            if (newWho == null) {
                newWho = defaultWho;
            }
        }
        Message newBody = new Message(originalBody.getMymsg(), newWho);
        PreDecoratorRequest updatedRequest = request.changeOriginalRequest()
                .setBody(newBody)
                .build();
        DecoratorResponse response = DecoratorResponseFactory.makePreDecoratorResponse(updatedRequest);
        return DecoratorUtils.getResponseEntityFromDecoratorResponse(response);
    }
}
