package eu.miaplatform.customplugin.springboot.controllers;
import eu.miaplatform.customplugin.springboot.CPController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "controller")
public class Controller extends CPController  {
}
