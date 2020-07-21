package eu.miaplatform.customplugin.springboot.controllers;

import eu.miaplatform.customplugin.ServiceClientFactory;
import eu.miaplatform.service.EnvConfiguration;
import eu.miaplatform.service.InitServiceOptions;
import eu.miaplatform.service.Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import okhttp3.Response;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@Api(value = "rateController")
public class RateController {
    @PostMapping("/counter/")
    @ApiOperation(value = "Get counter value from CRUD")
    @ResponseBody
    public String getCounter() throws IOException {
        InitServiceOptions initServiceOptions = InitServiceOptions.builder().build();
        final String CRUD_PATH = EnvConfiguration.getAndRequire("CRUD_PATH");
        Service service = ServiceClientFactory.getDirectServiceProxy(CRUD_PATH, initServiceOptions);
        Response response = service.get("/counter/", "", null);
        return response.body().string();
    }
}
