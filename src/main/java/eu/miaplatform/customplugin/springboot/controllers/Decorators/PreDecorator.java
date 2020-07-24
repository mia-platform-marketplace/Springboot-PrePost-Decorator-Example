package eu.miaplatform.customplugin.springboot.controllers.Decorators;

import eu.miaplatform.customplugin.springboot.DecoratorUtils;
import eu.miaplatform.customplugin.springboot.models.Message;
import eu.miaplatform.decorators.DecoratorRequest;
import eu.miaplatform.decorators.DecoratorResponse;
import eu.miaplatform.decorators.DecoratorResponseFactory;
import eu.miaplatform.decorators.predecorators.PreDecoratorRequest;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import eu.miaplatform.customplugin.springboot.CPController;

@RestController
@RequestMapping("/pre")
public class PreDecorator {
    @PostMapping("/checkwho")
    @ApiOperation(value = "Change user name")
    @ResponseBody
    public ResponseEntity checkWho(@RequestBody PreDecoratorRequest<Message> request) {
        final String defaultWho = "John Doe";
        Message originalBody = request.getOriginalRequestBody();
        String newWho = originalBody.getWho();
        if (newWho == null) {
            newWho = request.getUserId() == null ? defaultWho : request.getUserId();
        }
        Message newBody = new Message(originalBody.getMymsg(), newWho);

        PreDecoratorRequest<Message> updatedRequest = request.changeOriginalRequest()
                .setBody(newBody)
                .build();
        DecoratorResponse<DecoratorRequest<Message>> response = DecoratorResponseFactory.makePreDecoratorResponse(updatedRequest);
        return DecoratorUtils.getResponseEntityFromDecoratorResponse(response);
    }
}
