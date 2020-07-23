package eu.miaplatform.customplugin.springboot.Decorators;

import eu.miaplatform.customplugin.ServiceClientFactory;
import eu.miaplatform.customplugin.springboot.DecoratorUtils;
import eu.miaplatform.customplugin.springboot.models.Message;
import eu.miaplatform.decorators.DecoratorResponse;
import eu.miaplatform.decorators.DecoratorResponseFactory;
import eu.miaplatform.decorators.postdecorators.PostDecoratorRequest;
import eu.miaplatform.service.InitServiceOptions;
import eu.miaplatform.service.Service;
import eu.miaplatform.service.environment.EnvConfiguration;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import okhttp3.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Api(value = "postDecorator")
public class PostDecorator {
    @PostMapping("/notify")
    @ApiOperation(value = "Notify user on Slack")
    @ResponseBody
    public ResponseEntity checkWho(@RequestBody PostDecoratorRequest request) {
        final String serviceName = EnvConfiguration.getInstance().get("SERVICE_NAME");
        Message body = (Message) request.getOriginalRequestBody();
        String mymsg = body.getMymsg();
        String who = body.getWho();
        InitServiceOptions initServiceOptions = new InitServiceOptions();
        Service service = ServiceClientFactory.getDirectServiceProxy(serviceName, initServiceOptions);
        Response response;
        try {
            response = service.post("/notify-slack", "{ text: " + who + " says: " + mymsg + "}", null, null);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        System.out.println("Slack service response: " + response.code());
        PostDecoratorRequest unmodifiedRequest = request.leaveOriginalResponseUnmodified();
        DecoratorResponse decoratorResponse = DecoratorResponseFactory.makePostDecoratorResponse(unmodifiedRequest);
        return DecoratorUtils.getResponseEntityFromDecoratorResponse(decoratorResponse);
    }
}
