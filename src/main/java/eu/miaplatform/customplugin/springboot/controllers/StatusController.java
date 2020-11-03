package eu.miaplatform.customplugin.springboot.controllers;

import eu.miaplatform.customplugin.springboot.CPStatusController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "controller")
public class StatusController extends CPStatusController {
}
